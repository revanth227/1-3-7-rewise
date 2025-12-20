package com.example.rewise.repo;

import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {
    boolean existsByTopicIdAndNotifyDate(Long id, LocalDate revise3Date);

    Page<Notification> findByNotifyDateAndIsSent(LocalDate now, boolean b, Pageable pageable);
    List<Notification> findByNotifyDateAndIsSent(LocalDate now, boolean b);

    List<Notification> findByActiveAndIsSent(boolean active,boolean isSent);

    Page<Notification> findByIsSent(boolean b,Pageable pageable);

    Topic findByTopicId(Long topicId);
}
