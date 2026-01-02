package com.example.rewise.service;

import com.example.rewise.config.JWTService;
import com.example.rewise.entity.User;
import com.example.rewise.exceptions.NoDuplicateException;
import com.example.rewise.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Lazy
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JWTService jwtService;


    @Transactional
    public User savingUser(User user) {
        User user1 = userRepo.findByName(user.getName());
        if (user1 != null) {
            throw new NoDuplicateException("Duplicate Users Not Allowed");
        }
        String newPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(newPassword);
        user.setRole("USER");
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.rewise.entity.User user =
                userRepo.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("NO users found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getName())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user);
        }
        return "Fail";

    }
}
