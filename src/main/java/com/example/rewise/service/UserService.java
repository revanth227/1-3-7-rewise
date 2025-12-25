package com.example.rewise.service;

import com.example.rewise.entity.User;
import com.example.rewise.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Transactional
    public User savingUser(User user) {
        return userRepo.save(user);
    }
}
