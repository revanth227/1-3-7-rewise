package com.example.rewise.controller;

import com.example.rewise.dto.RequestDto;
import com.example.rewise.dto.ResponseDto;
import com.example.rewise.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TopicController {
    @Autowired
    private TopicService topicService;


    @GetMapping("/topics")
    public Page<ResponseDto> getAll(
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
        return topicService.getAll(page, size, sort, direction);
    }

    @GetMapping("/user")
    public List<ResponseDto> getAllByUserId() {
        return topicService.getAllByUserId();
    }


    @PostMapping("/add")
    public ResponseDto createTopic(@RequestBody RequestDto requestDto) {
        return topicService.create(requestDto);
    }

    @GetMapping("/today")
    public Page<ResponseDto> findTodayPending(Pageable pageable) {


        return topicService.getTodayTasks(pageable);
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
    public List<ResponseDto> todayPending() {
        return topicService.allPendingList();

    }
}
