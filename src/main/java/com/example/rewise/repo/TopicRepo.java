package com.example.rewise.repo;

import com.example.rewise.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepo extends JpaRepository<Topic, Long> {
    Topic findByTopicId(Long topicId);
}
