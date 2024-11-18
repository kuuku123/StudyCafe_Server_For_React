package com.StudyCafe_R.modules.event.sse;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.study.domain.Study;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/sse")
public class SSEController {

    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@CurrentAccount Account account) {
        SseEmitter emitter = new SseEmitter();
        clients.put(account.getEmail(), emitter);
        emitter.onCompletion(() -> clients.remove(account));
        emitter.onTimeout(() -> clients.remove(account));
        emitter.onError((e) -> clients.remove(account));
        return emitter;
    }

    public void notifyClientsStudyCreate(Account account, Study study) {
        SseEmitter sseEmitter = clients.get(account.getEmail());
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("studyCreate").data(study.getEncodedPath()));
            } catch (IOException e) {
                e.printStackTrace();
                clients.remove(account.getEmail());
            }
        }
    }
}
