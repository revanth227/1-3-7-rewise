package com.example.rewise.controller;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/today/notifications")
    public Page<Notification> getAll(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        return notificationService.getTodayNotifications( pageable);
    }

    @GetMapping("/notifications/history")
    public Page<NotificationResponse> notificationList(
                                                       @RequestParam(required = false) Long topicId,
                                                       @RequestParam(required = false) LocalDate date,
                                                       @org.springdoc.core.annotations.ParameterObject Pageable pageable

    ) throws AccessDeniedException {
        if (topicId != null) {
            return notificationService.history( topicId,pageable);
        }
        if (date != null) {
            return notificationService.history( date,pageable);
        }
        return notificationService.history( pageable);
    }
}
