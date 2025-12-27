package com.example.rewise.repo;

import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    boolean existsByTopicIdAndNotifyDate(Long id, LocalDate revise3Date);

    Page<Notification> findByNotifyDateAndIsSent(LocalDate now, boolean b, Pageable pageable);

    List<Notification> findByNotifyDateAndIsSent(LocalDate now, boolean b);


    Page<Notification> findByIsSent(boolean b, Pageable pageable);

    Page<Notification> findByUserAndNotifyDateAndIsSent(User user, LocalDate date, boolean isSent, Pageable pageable);

    Topic findByTopicId(Long topicId);

    //Notification findByTopicAndDate(Topic savedTopic, LocalDate today);

    Optional<Notification> findByTopicAndNotifyDate(Topic savedTopic, LocalDate today);

    List<Notification> findByTopicAndIsSent(Topic topic, boolean b);

    Page<Notification> findByUserAndIsSentAndTopic(User user, boolean b, Pageable pageable, Topic topicId);

    Page<Notification> findByUserAndIsSent(User user, boolean isSent, Pageable pageable);


    List<Notification> findByNotifyDateAndActiveAndIsSent(LocalDate today, boolean b, boolean b1);

    Page<Notification> findByUserAndIsSenAndSentAt(User user, boolean b, Pageable pageable, LocalDate date);
}
