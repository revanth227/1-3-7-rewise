package com.example.rewise.service;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.entity.User;
import com.example.rewise.exceptions.NoItems;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.TopicRepo;
import com.example.rewise.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private TopicRepo topicRepo;

    @Autowired
    private UserRepo userRepo;

    private final Clock clock;

    public NotificationService(Clock clock) {
        this.clock = clock;
    }

    public Page<Notification> getTodayNotifications(Long userId, Pageable pageable) {
        LocalDate today = LocalDate.now(clock);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Notification> notificationPage = notificationRepo
                .findByUserAndNotifyDateAndIsSent(user, today, false, pageable);

        if (notificationPage.isEmpty()) {
            throw new NoItems("No Notifications to display");
        }

        return notificationPage;
    }

    public Page<NotificationResponse> history(Long userId, Pageable pageable) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Notification> notifications = notificationRepo
                .findByUserAndIsSent(user, true, pageable);

        List<NotificationResponse> notificationList = new ArrayList<>();
        for (Notification notification : notifications.getContent()) {
            Topic topic = notification.getTopic();
            NotificationResponse response = new NotificationResponse();
            response.setTopicTitle(topic.getTitle());
            response.setSubject(topic.getSubject());
            response.setSentAt(notification.getSentAt());
            response.setNotifyDate(notification.getNotifyDate());
            response.setMessage(notification.getMessage());

            notificationList.add(response);
        }

        return new PageImpl<>(notificationList, pageable, notifications.getTotalElements());
    }
}
