package com.StudyCafe_R.modules.notification;

import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AccountService accountService;

    @GetMapping("/notifications")
    public ResponseEntity<String> getNotifications(@RequestHeader("X-User-Email") String email) {
        Account account = accountService.getAccount(email);
        List<NotificationDto> unReadNotification = notificationService.getUnReadNotification(account);
        ApiResponse<List<NotificationDto>> apiResponse = new ApiResponse<>("mark as read succeed", HttpStatus.OK, unReadNotification);
        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }

    @PostMapping("/mark-notification-checked")
    public ResponseEntity<String> markNotificationChecked(@RequestParam Long notificationId) {
        boolean success = notificationService.markAsChecked(notificationId);
        if (success) {
            ApiResponse<String> apiResponse = new ApiResponse<>("mark as check succeed", HttpStatus.OK, null);
            return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse<>("notification doesn't exist", HttpStatus.BAD_REQUEST, null);
            return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/notifications/old")
//    public String getOldNotifications(@CurrentAccount Account account, Model model) {
//        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);
//        long numberOfNotChecked = notificationRepository.countByAccountAndChecked(account, false);
//        putCategorizedNotifications(model, notifications, notifications.size(), numberOfNotChecked);
//        model.addAttribute("isNew", false);
//        return "notification/list";
//    }
//
//    @DeleteMapping("/notifications")
//    public String deleteNotifications(@CurrentAccount Account account) {
//        notificationRepository.deleteByAccountAndChecked(account, true);
//        return "redirect:/notifications";
//    }


    private void putCategorizedNotifications(Model model, List<Notification> notifications, long numberOfChecked, long numberOfNotChecked) {
        List<Notification> newStudyNotifications = new ArrayList<>();
        List<Notification> eventEnrollmentNotifications = new ArrayList<>();
        List<Notification> watchingStudyNotifications = new ArrayList<>();

        for (Notification notification : notifications) {
            switch (notification.getNotificationType()) {
                case STUDY_CREATED:
                    newStudyNotifications.add(notification);
                    break;
                case EVENT_ENROLLMENT:
                    eventEnrollmentNotifications.add(notification);
                    break;
                case STUDY_UPDATED:
                    watchingStudyNotifications.add(notification);
                    break;
            }
        }
        model.addAttribute("numberOfNotChecked", numberOfNotChecked);
        model.addAttribute("numberOfChecked", numberOfChecked);
        model.addAttribute("notifications", notifications);
        model.addAttribute("newStudyNotifications", newStudyNotifications);
        model.addAttribute("eventEnrollmentNotifications", eventEnrollmentNotifications);
        model.addAttribute("watchingStudyNotifications", watchingStudyNotifications);
    }

}
