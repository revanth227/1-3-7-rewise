package com.example.rewise.service;

import com.example.rewise.entity.Notification;
import com.example.rewise.repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@Configuration
public class NotificationScheduler {

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private Clock clock;

    @Scheduled(cron = "0 0 6 * * *")
    public void activateTodayNotifications() {
        LocalDate today = LocalDate.now(clock);
        List<Notification> pending = notificationRepo
                .findByNotifyDateAndIsSent(today, false);

        for (Notification n : pending) {
            if (!n.getTopic().isCompleted()) {
                n.setActive(true);
                notificationRepo.save(n);
            }
        }
        System.out.println("Today's notifications activated");
    }


    @Scheduled(cron = "0 */1 * * * * ")
    public void sentNotifications() {
        LocalDate today = LocalDate.now(clock);
        List<Notification> notifications =
                notificationRepo.findByNotifyDateAndActiveAndIsSent(today, true, false);
        for (Notification notification : notifications) {
            if (!notification.getTopic().isCompleted()) {
                notification.setSentAt(today);
                notification.setActive(false);
                notification.setSent(true);
                notificationRepo.save(notification);
            }
        }
        System.out.println("Message Sent to User");
    }


}
