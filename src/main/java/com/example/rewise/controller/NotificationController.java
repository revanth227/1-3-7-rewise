package com.example.rewise.controller;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/today/notifications")
    public Page<Notification> getAll(Pageable pageable){
        return notificationService.getTodayNotifications(pageable);
    }

    @GetMapping("/notifications/history")
    public List<NotificationResponse> notificationList(){
        return notificationService.history();
    }
}
