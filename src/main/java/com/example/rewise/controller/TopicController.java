package com.example.rewise.controller;

import com.example.rewise.dto.RequestDto;
import com.example.rewise.dto.ResponseDto;
import com.example.rewise.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TopicController {
    @Autowired
    private TopicService topicService;


    @GetMapping("/topics/me")
    public List<ResponseDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdDate") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must be >= 0");
        }
        if (size <= 0 || size > 50) {
            throw new IllegalArgumentException("Page size must be between 1 and 50");
        }
        return topicService.getAllByUserId();
    }

//    @GetMapping("/topics/me")
//    public List<ResponseDto> getAllByUserId() {
//        return topicService.getAllByUserId();
//    }


    @PostMapping("/topics")
    public ResponseDto createTopic(@RequestBody RequestDto requestDto) {
        return topicService.create(requestDto);
    }

    @GetMapping("/topics/today")
    public List<ResponseDto> findTodayPending() {
        return topicService.getTodayTasks();
    }

    @PutMapping("/topics/{id}/revision/{day}")
    public ResponseDto update(@PathVariable Long id, @PathVariable int day) {
        return topicService.updateIsRevised(id, day);
    }

    @DeleteMapping("/topics/{id}")
    public String deleteById(@PathVariable long id) {
        return topicService.removeById(id);
    }

    @GetMapping("/topics/{id}")
    public ResponseDto getTheNotification(@PathVariable Long id) {
        return topicService.getTheTopic(id);
    }

    @GetMapping("/topics/pending")
    public List<ResponseDto> todayPending() {
        return topicService.allPendingList();

    }

    @GetMapping("/topics/missed")
    public List<ResponseDto> missedTopics(){
        return topicService.missedTopicsService();
    }

    @GetMapping("/topics/completed")
    public List<ResponseDto> allCompleted(){
        return topicService.allCompletedService();
    }
}
