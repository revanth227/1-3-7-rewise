package com.example.rewise.dto;

import java.time.LocalDate;

public class ResponseDto {
    private String title;
    private String subject;
    private LocalDate createdDate;
    private LocalDate revise3Date;
    private LocalDate revise7Date;
    private boolean isRevised3;
    private boolean isRevised7;
    private boolean isCompleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
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
