package com.example.rewise.service;

import com.example.rewise.dto.RequestDto;
import com.example.rewise.dto.ResponseDto;
import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.entity.User;
import com.example.rewise.exceptions.TopicNotFound;
import com.example.rewise.exceptions.UserNotFound;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.TopicRepo;
import com.example.rewise.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepo topicRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserRepo userRepo;

    private final Clock clock;

    public TopicService(Clock clock) {
        this.clock = clock;
    }

    public List<ResponseDto> getAllByUserId() {
        User user = userRepo.findById(1L)
                .orElseThrow(() -> new UserNotFound("User Not Found"));

        return topicRepo.findByUser(user)
                .stream()
                .map(this::getResponseDto)
                .toList();
    }

    @Transactional
    public ResponseDto create(RequestDto requestDto) {
        LocalDate today = LocalDate.now(clock);

        User user = userRepo.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Topic topic = new Topic();
        topic.setUser(user);
        topic.setTitle(requestDto.getTitle());
        topic.setSubject(requestDto.getSubject());
        topic.setCreatedDate(today);
        topic.setRevise3Date(today.plusDays(3));
        topic.setRevise7Date(today.plusDays(7));
        topic.setRevised3(false);
        topic.setRevised7(false);
        topic.setCompleted(false);
        Topic savedTopic = topicRepo.save(topic);
        Optional<Notification> notification = notificationRepo.findByTopicAndNotifyDate(savedTopic, today.plusDays(3));
        Optional<Notification> notification2 = notificationRepo.findByTopicAndNotifyDate(savedTopic, today.plusDays(7));
        if (notification.isEmpty()) {
            Notification notification3 = createNotification(savedTopic, today.plusDays(3),
                    "3 - day revision : " + savedTopic.getTitle(), user);
            notificationRepo.save(notification3);
        }
        if (notification2.isEmpty()) {
            Notification notification7 = createNotification(savedTopic, today.plusDays(7),
                    "7 - day revision : " + savedTopic.getTitle(), user);

            notificationRepo.save(notification7);
        }
        return getResponseDto(savedTopic);
    }

    private Notification createNotification(Topic topic, LocalDate date, String message, User user) {
        Notification n = new Notification();
        n.setTopic(topic);
        n.setUser(user);
        n.setNotifyDate(date);
        n.setMessage(message);
        n.setSent(false);
        n.setActive(false);
        return n;
    }

    public List<ResponseDto> getTodayTasks() {
        LocalDate today = LocalDate.now();

        User users = userRepo.findById(1L)
                .orElseThrow(() -> new UserNotFound("User Not Found"));
        List<Topic> topics = topicRepo.findByUser(users);
        List<ResponseDto> responseDtos = new ArrayList<>();
        for (Topic topic : topics) {
            if ((topic.getRevise3Date().equals(today) && !topic.isRevised3()) ||
                    (topic.getRevise7Date().equals(today) && !topic.isRevised7())) {
                responseDtos.add(getResponseDto(topic));
            }
        }
        return responseDtos;
    }


    private ResponseDto getResponseDto(Topic topic) {
        ResponseDto dto = new ResponseDto();
        dto.setTitle(topic.getTitle());
        dto.setSubject(topic.getSubject());
        dto.setCreatedDate(topic.getCreatedDate());
        dto.setRevise3Date(topic.getRevise3Date());
        dto.setRevise7Date(topic.getRevise7Date());
        dto.setRevised3(topic.isRevised3());
        dto.setRevised7(topic.isRevised7());
        dto.setCompleted(topic.isCompleted());
        return dto;
    }

    public ResponseDto updateIsRevised(Long id, int day) {
        Optional<Topic> updatedDate = topicRepo.findById(id);
        if (updatedDate.isEmpty()) {
            throw new TopicNotFound("No topic Found " + id);
        }
        Topic topic = updatedDate.get();
        if (day == 3) {
            if (!topic.isRevised3()) {
                topic.setRevised3(true);
                topicRepo.save(topic);
            }
        }
        if (day == 7) {
            if (!topic.isRevised7()) {
                topic.setRevised7(true);
                topicRepo.save(topic);
            }
        }
        if (topic.isRevised3() && topic.isRevised7()) {
            topic.setCompleted(true);
            topicRepo.save(topic);
        }
        List<Notification> pendingNotifications =
                notificationRepo.findByTopicAndIsSent(topic, false);

        for (Notification n : pendingNotifications) {
            n.setActive(false);
            n.setSent(true);
        }

        notificationRepo.saveAll(pendingNotifications);
        return getResponseDto(updatedDate.get());

    }

    public ResponseDto getTheTopic(Long id) {
        Optional<Topic> topicOptional = topicRepo.findById(id);
        if (topicOptional.isEmpty()) {
            throw new TopicNotFound("No topic found with the id " + id);
        }
        Topic topic = topicOptional.get();
        return getResponseDto(topic);

    }

    public String removeById(long id) {
        Optional<Topic> topic = topicRepo.findById(id);
        if (topic.isEmpty()) {
            throw new TopicNotFound("No Topic Found To Delete");
        }
        return " Deleted Successfully";
    }

    public List<ResponseDto> allPendingList() {
        User user = userRepo.findById(1L)
                .orElseThrow(() -> new UserNotFound("User Not Found"));

        LocalDate today = LocalDate.now(clock);
        List<Topic> topics = topicRepo.findByUser(user);
        List<ResponseDto> responseDtos = new ArrayList<>();

        for (Topic topic : topics) {

            boolean threeDayPending =
                    (topic.getRevise3Date().isEqual(today) || topic.getRevise3Date().isAfter(today))
                            && !topic.isRevised3();

            boolean sevenDayPending =
                    (topic.getRevise7Date().isEqual(today) || topic.getRevise7Date().isAfter(today))
                            && !topic.isRevised7();

            if (threeDayPending || sevenDayPending) {
                responseDtos.add(getResponseDto(topic));
            }
        }
        return responseDtos;
    }


    public List<ResponseDto> missedTopicsService() {
        List<ResponseDto> responseDtos = new ArrayList<>();
        User user = userRepo.findById(1L)
                .orElseThrow(() -> new UserNotFound("User not found"));
        List<Topic> topics = topicRepo.findByUser(user);

        for (Topic topic : topics) {
            if (topic.getRevise3Date().isBefore(LocalDate.now(clock)) && !topic.isRevised3() || topic.getRevise7Date().isBefore(LocalDate.now(clock)) && !topic.isRevised7()) {
                ResponseDto responseDto = getResponseDto(topic);
                responseDtos.add(responseDto);
            }
        }
        return responseDtos;
    }

    public List<ResponseDto> allCompletedService() {
        User user = userRepo.findById(1L)
                .orElseThrow(() -> new UserNotFound("No user Found"));
        List<ResponseDto> responseDtos = new ArrayList<>();
        List<Topic> topics = topicRepo.findByUserAndIsCompleted(user, true);
        for (Topic topic : topics) {
            ResponseDto responseDto = getResponseDto(topic);
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }
}








