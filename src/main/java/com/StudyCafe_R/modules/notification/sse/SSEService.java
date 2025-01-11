package com.StudyCafe_R.modules.notification.sse;

import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.notification.Notification;
import com.StudyCafe_R.modules.study.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SSEService {

    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

    public SseEmitter getConnection(Account account) {
        SseEmitter sseEmitter = clients.get(account.getEmail());
        if (sseEmitter == null) {
            sseEmitter = new SseEmitter(600000L);
            clients.put(account.getEmail(), sseEmitter);
            sseEmitter.onCompletion(() -> clients.remove(account.getEmail()));
            sseEmitter.onTimeout(() -> clients.remove(account.getEmail()));
            sseEmitter.onError((e) -> clients.remove(account.getEmail()));
        }
        try {
            sseEmitter.send(SseEmitter.event().data("test"));
        } catch (IOException e) {
            e.printStackTrace();
            clients.remove(account.getEmail());
        }
        return sseEmitter;
    }

    public void notifyClientsStudyCreate(Notification notification , Study study) {
        Account account = notification.getAccount();
        String eventName = switch (notification.getNotificationType()) {
            case STUDY_CREATED -> "StudyCreated";
            case EVENT_ENROLLMENT -> "EventEnrollment";
            case STUDY_UPDATED -> "StudyUpdated";
        };

        SseEmitter sseEmitter = clients.get(account.getEmail());
        if (sseEmitter != null) {
            try {
                log.info(account.getEmail() + " "+eventName +" event sse send");
                sseEmitter.send(SseEmitter.event().name(eventName).data(study.getEncodedPath()));
            } catch (IOException e) {
                e.printStackTrace();
                sseEmitter.completeWithError(e);
                clients.remove(account.getEmail());
            }
        }
    }

}
