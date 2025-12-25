package com.example.rewise.controller;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/today/notifications{id}")
    public Page<Notification> getAll(@PathVariable Long id, Pageable pageable){
        return notificationService.getTodayNotifications(id,pageable);
    }

    @GetMapping("/notifications/history{id}")
    public Page<NotificationResponse> notificationList(@PathVariable Long id,Pageable pageable){
        return notificationService.history(id,pageable);
    }
}
