package com.example.rewise.service;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private TopicRepo topicRepo;

    public Page<Notification> getTodayNotifications(Pageable pageable) {
        LocalDate localDate = LocalDate.now();
        return notificationRepo.findByNotifyDateAndIsSent(localDate, false,pageable);
    }

    public Page<NotificationResponse> history(Pageable pageable) {
        Page<Notification> notifications = notificationRepo.findByIsSent(true,pageable);
        List<NotificationResponse> notificationList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationResponse notificationResponse = new NotificationResponse();
            Optional<Topic> topic = topicRepo.findById(notification.getTopicId());
            if(topic.isEmpty()){
                throw new RuntimeException("No topic found");
            }
            notificationResponse.setTopicTitle(topic.get().getTitle());
            notificationResponse.setSubject(topic.get().getSubject());
            notificationResponse.setSentAt(notification.getSentAt());
            notificationResponse.setNotifyDate(notification.getNotifyDate());
            notificationResponse.setMessage(notification.getMessage());
            notificationList.add(notificationResponse);

        }
        return new PageImpl<NotificationResponse>(notificationList,pageable,notifications.getTotalElements());
    }
}
