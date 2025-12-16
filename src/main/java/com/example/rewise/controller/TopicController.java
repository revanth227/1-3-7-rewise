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


    @GetMapping("/topics")
    public List<ResponseDto> getAllTopics() {
        return topicService.getAll();
    }


    @PostMapping("/add")
    public ResponseDto createTopic(@RequestBody RequestDto requestDto) {
        return topicService.create(requestDto);
    }

    @GetMapping("/today")
    public List<ResponseDto> findTodayPending() {
        return topicService.getTodayTasks();
    }

    @PutMapping("/topics/{id}/revision/{day}")
    public ResponseDto update(@PathVariable Long id, @PathVariable int day) {
        return topicService.updateIsRevised(id, day);     //put/id
    }

    @DeleteMapping("delete/{id}")
    public String deleteById(@PathVariable long id) {
        return topicService.removeById(id);
    }

    @GetMapping("/id/{id}")
    public ResponseDto getTheNotification(@PathVariable Long id) {
        return topicService.getTheTopic(id);
    }

    @GetMapping("/go")
    public List<ResponseDto> todayPending(){
        return topicService.allPendingList();

    }
}
