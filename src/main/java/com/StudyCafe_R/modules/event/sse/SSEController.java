package com.StudyCafe_R.modules.event.sse;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.notification.Notification;
import com.StudyCafe_R.modules.notification.NotificationType;
import com.StudyCafe_R.modules.study.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/sse")
public class SSEController {

    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@CurrentAccount Account account) {
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
        String eventName = "";
        switch(notification.getNotificationType()) {
            case STUDY_CREATED:
                eventName = "StudyCreated";
                break;
            case EVENT_ENROLLMENT:
                eventName = "EventEnrollment";
                break;
            case STUDY_UPDATED:
                eventName = "StudyUpdated";
                break;
        }

        SseEmitter sseEmitter = clients.get(account.getEmail());
        if (sseEmitter != null) {
            try {
                log.info("StudyCrate event sse send");
                sseEmitter.send(SseEmitter.event().name("StudyCreated").data(study.getEncodedPath()));
            } catch (IOException e) {
                e.printStackTrace();
                clients.remove(account.getEmail());
            }
        }
    }
}
