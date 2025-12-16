package com.example.rewise.repo;

import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {
    boolean existsByTopicIdAndNotifyDate(Long id, LocalDate revise3Date);

    List<Notification> findByNotifyDateAndIsSent(LocalDate now, boolean b);

    List<Notification> findByActiveAndIsSent(boolean active,boolean isSent);

    List<Notification> findByIsSent(boolean b);

    Topic findByTopicId(Long topicId);
}
