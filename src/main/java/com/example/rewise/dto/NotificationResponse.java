package com.example.rewise.dto;

import java.time.LocalDate;

public class NotificationResponse {
    private String topicTitle;
    private String subject;
    private LocalDate sentAt;
    private LocalDate notifyDate;

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String message;

    public LocalDate getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDate sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDate getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(LocalDate notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
