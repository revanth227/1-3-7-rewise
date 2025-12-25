package com.example.rewise.repo;

import com.example.rewise.entity.Topic;
import com.example.rewise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepo extends JpaRepository<Topic, Long> {
    List<Topic> findByUserId(Long userId);

    List<Topic> findByUser(User user);
}
