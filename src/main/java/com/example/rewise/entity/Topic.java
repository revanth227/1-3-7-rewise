package com.example.rewise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.catalina.User;

import java.time.LocalDate;

@Entity
public class Topic {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    private String subject;
    private LocalDate createdDate;
    private LocalDate revise3Date;
    private LocalDate revise7Date;
    private boolean isRevised3;
    private boolean isRevised7;
    private boolean isCompleted;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Topic() {
    }

    public Topic(String title, String subject, LocalDate createdDate, LocalDate revise3Date, LocalDate revise7Date, boolean isRevised3, boolean isRevised7, boolean isCompleted) {
        this.title = title;
        this.subject = subject;
        this.createdDate = createdDate;
        this.revise3Date = revise3Date;
        this.revise7Date = revise7Date;
        this.isRevised3 = isRevised3;
        this.isRevised7 = isRevised7;
        this.isCompleted = isCompleted;
    }

    public @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getRevise3Date() {
        return revise3Date;
    }

    public void setRevise3Date(LocalDate revise3Date) {
        this.revise3Date = revise3Date;
    }

    public LocalDate getRevise7Date() {
        return revise7Date;
    }

    public void setRevise7Date(LocalDate revise7Date) {
        this.revise7Date = revise7Date;
    }

    public boolean isRevised3() {
        return isRevised3;
    }

    public void setRevised3(boolean revised3) {
        isRevised3 = revised3;
    }

    public boolean isRevised7() {
        return isRevised7;
    }

    public void setRevised7(boolean revised7) {
        isRevised7 = revised7;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
