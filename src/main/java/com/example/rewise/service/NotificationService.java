package com.example.rewise.service;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private TopicRepo topicRepo;

    public List<Notification> getTodayNotifications() {
        LocalDate localDate = LocalDate.now();
        List<Notification> notificationList = notificationRepo.findByNotifyDateAndIsSent(localDate, false);
        List<Notification> notificationList1 = new ArrayList<>();

        notificationList1.addAll(notificationList);
        return notificationList1;
    }

    public List<NotificationResponse> history() {
        List<Notification> notifications = notificationRepo.findByIsSent(true);
        List<NotificationResponse> notificationList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationResponse notificationResponse = new NotificationResponse();
            Topic topic = topicRepo.findByTopicId(notification.getTopicId());
            notificationResponse.setTopicTitle(topic.getTitle());
            notificationResponse.setSubject(topic.getSubject());
            notificationResponse.setSentAt(notification.getSentAt());
            notificationResponse.setNotifyDate(notification.getNotifyDate());
            notificationResponse.setMessage(notification.getMessage());
            notificationList.add(notificationResponse);

        }
        return notificationList;
    }
}
