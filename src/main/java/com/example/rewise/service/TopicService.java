package com.example.rewise.service;

import com.example.rewise.dto.RequestDto;
import com.example.rewise.dto.ResponseDto;
import com.example.rewise.entity.Notification;
import com.example.rewise.entity.Topic;
import com.example.rewise.exceptions.NoPageFound;
import com.example.rewise.exceptions.TopicNotFound;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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


    public Page<ResponseDto> getAll(int page, int size, String sortField, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Topic> topicPage = topicRepo.findAll(pageable);
        if (page >= topicPage.getTotalPages() && topicPage.getTotalPages() > 0) {
            throw new NoPageFound("Requested page does not exist");
        }

        List<ResponseDto> dtoList = new ArrayList<>();

        for (Topic topic : topicPage.getContent()) {
            ResponseDto dto = new ResponseDto();
            dto.setTitle(topic.getTitle());
            dto.setSubject(topic.getSubject());
            dto.setCreatedDate(topic.getCreatedDate());
            dto.setRevise3Date(topic.getRevise3Date());
            dto.setRevise7Date(topic.getRevise7Date());
            dto.setRevised3(topic.isRevised3());
            dto.setRevised7(topic.isRevised7());
            dto.setCompleted(topic.isCompleted());
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, pageable, topicPage.getTotalElements());
    }


    public ResponseDto create(RequestDto requestDto) {
        Topic topic = new Topic();
        topic.setTitle(requestDto.getTitle());
        topic.setSubject(requestDto.getSubject());
        topic.setCreatedDate(LocalDate.now());
        topic.setRevise3Date(LocalDate.now().plusDays(3));
        topic.setRevise7Date(LocalDate.now().plusDays(7));
        topic.setRevised3(false);
        topic.setRevised7(false);
        topic.setCompleted(false);
        Topic topic1 = topicRepo.save(topic);
        Notification notification3 = createNotification(topic1.getId(), topic1.getRevise3Date(), "3 - day revision : " + topic1.getTitle() + " " + topic1.getRevise3Date());

        notificationRepo.save(notification3);

        Notification notification7 = createNotification(topic1.getId(), topic1.getRevise7Date(), "7 - day revision :" + topic1.getTitle() + " " + topic1.getRevise7Date());

        notificationRepo.save(notification7);
        return getResponseDto(topic1);
    }

    public Page<ResponseDto> getTodayTasks(Pageable pageable) {
        List<ResponseDto> responseDtoList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        Page<Topic> topic = topicRepo.findAll(pageable);
        for (Topic topics : topic) {
            if (topics.getRevise3Date().equals(currentDate) || topics.getRevise7Date().equals(currentDate)) {
                ResponseDto responseDto = getResponseDto(topics);
                responseDtoList.add(responseDto);
            }
        }
        return new PageImpl<>(responseDtoList, pageable, topic.getTotalElements());
    }

    private static ResponseDto getResponseDto(Topic topics) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setTitle(topics.getTitle());
        responseDto.setSubject(topics.getSubject());
        responseDto.setCreatedDate(topics.getCreatedDate());
        responseDto.setRevise3Date(topics.getRevise3Date());
        responseDto.setRevise7Date(topics.getRevise7Date());
        responseDto.setRevised3(topics.isRevised3());
        responseDto.setRevised7(topics.isRevised7());
        responseDto.setCompleted(topics.isCompleted());
        return responseDto;
    }

    private Notification createNotification(Long topicId, LocalDate date, String message) {
        Notification n = new Notification();
        n.setTopicId(topicId);
        n.setNotifyDate(date);
        n.setMessage(message);
        n.setSent(false);
        notificationRepo.save(n);
        return n;
    }


    public ResponseDto updateIsRevised(Long id, int day) {
        Optional<Topic> updatedDate = topicRepo.findById(id);
        if (updatedDate.isEmpty()) {
            throw new TopicNotFound("No topic Found " + id);
        }
        Topic topic = updatedDate.get();
        if (day == 3) {
            topic.setRevised3(true);
            topicRepo.save(topic);
        }
        if (day == 7) {
            topic.setRevised7(true);
        }
        if (topic.isRevised3() && topic.isRevised7()) {
            topic.setCompleted(true);
        }


        return getResponseDto(updatedDate.get());

    }

    public String removeById(long id) {
        Optional<Topic> topic = topicRepo.findById(id);
        if (topic.isEmpty()) {
            throw new RuntimeException("No Topic Found To Delete");
        }
        return " Deleted Successfully";
    }


    public ResponseDto getTheTopic(Long id) {
        Optional<Topic> topicOptional = topicRepo.findById(id);
        if (topicOptional.isEmpty()) {
            throw new TopicNotFound("No topic found with the id " + id);
        }
        Topic topic = topicOptional.get();
        return getResponseDto(topic);

    }

    public List<ResponseDto> allPendingList() {
        List<Topic> topicList = topicRepo.findAll();
        LocalDate today = LocalDate.now();
        List<ResponseDto> responseDtos = new ArrayList<>();
        for (Topic topic : topicList) {
            boolean threeDayPending =
                    (topic.getRevise3Date().isBefore(today) || topic.getRevise3Date().isEqual(today))
                            && !topic.isRevised3();

            boolean sevenDayPending =
                    (topic.getRevise7Date().isBefore(today) || topic.getRevise7Date().isEqual(today))
                            && !topic.isRevised7();

            if (threeDayPending || sevenDayPending) {
                ResponseDto responseDto = getResponseDto(topic);
                responseDtos.add(responseDto);
            }
        }
        return responseDtos;
    }
}







