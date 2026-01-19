package com.example.rewise.service;

import com.example.rewise.dto.NotificationResponse;
import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.entity.User;
import com.example.rewise.exceptions.NoItems;
import com.example.rewise.exceptions.TopicNotFound;
import com.example.rewise.exceptions.UserNotFound;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.TopicRepo;
import com.example.rewise.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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


    public Page<Notification> getTodayNotifications(Pageable pageable) {

        User user = getCurrentUser();

        LocalDate today = LocalDate.now(clock);

        Page<Notification> notificationPage =
                notificationRepo.findByUserAndNotifyDateAndIsSent(
                        user,
                        today,
                        false,
                        pageable
                );

        if (notificationPage.isEmpty()) {
            throw new NoItems("No Notifications to display");
        }

        return notificationPage;
    }


    public Page<NotificationResponse> history(Long topicId, Pageable pageable)
            throws AccessDeniedException {

        User user = getCurrentUser();

        Topic findTopic = topicRepo.findById(topicId)
                .orElseThrow(() ->
                        new TopicNotFound("No Topic Found By The Id " + topicId));

        Page<Notification> notifications =
                notificationRepo.findByUserAndIsSentAndTopic(
                        user,
                        true,
                        findTopic,
                        pageable
                );

        if (!findTopic.getUser().getName().equals(user.getName())) {
            throw new AccessDeniedException("Topic not owned by user");
        }

        return mapToResponsePage(notifications, pageable);
    }


    public Page<NotificationResponse> history(LocalDate date, Pageable pageable) {

        User user = getCurrentUser();

        Page<Notification> notifications =
                notificationRepo.findByUserAndIsSentAndSentAt(
                        user,
                        true,
                        date,
                        pageable
                );

        return mapToResponsePage(notifications, pageable);
    }


    public Page<NotificationResponse> history(Pageable pageable) {

        User user = getCurrentUser();

        Page<Notification> notifications =
                notificationRepo.findByUserAndIsSent(
                        user,
                        true,
                        pageable
                );

        return mapToResponsePage(notifications, pageable);
    }


    private User getCurrentUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByName(username);

        if (user == null) {
            throw new UserNotFound("No User Found");
        }

        return user;
    }

    private Page<NotificationResponse> mapToResponsePage(
            Page<Notification> notifications,
            Pageable pageable
    ) {

        List<NotificationResponse> responseList = new ArrayList<>();

        for (Notification notification : notifications.getContent()) {
            Topic topic = notification.getTopic();

            NotificationResponse response = new NotificationResponse();
            response.setTopicTitle(topic.getTitle());
            response.setSubject(topic.getSubject());
            response.setSentAt(notification.getSentAt());
            response.setNotifyDate(notification.getNotifyDate());
            response.setMessage(notification.getMessage());

            responseList.add(response);
        }

        return new PageImpl<>(
                responseList,
                pageable,
                notifications.getTotalElements()
        );
    }
}
