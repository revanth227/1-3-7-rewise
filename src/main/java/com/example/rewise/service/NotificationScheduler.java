package com.example.rewise.service;

import com.example.rewise.dto.MailDto;
import com.example.rewise.dto.MailResponseDto;
import com.example.rewise.entity.Notification;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Configuration
public class NotificationScheduler {

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private Clock clock;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepo userRepo;
    private static final Logger logger =
            LoggerFactory.getLogger(NotificationScheduler.class);
    @Value("${email.service.url}")
    private String emailServiceUrl;


    @Scheduled(cron = "0 5 0 * * *", zone = "UTC")
    public void activateTodayNotifications() {
        LocalDate today = LocalDate.now(clock);
        List<Notification> pending = notificationRepo
                .findByNotifyDateAndIsSent(today, false);

        for (Notification n : pending) {
            if (!n.getTopic().isCompleted()) {
                n.setActive(true);
                notificationRepo.save(n);
                System.out.println("Today's notifications activated");
            }
        }

    }


    @Scheduled(cron = "0 */15 7-23 * * *", zone = "UTC")
    public void sendNotifications() {

        LocalDate today = LocalDate.now(clock);

        List<Notification> notifications =
                notificationRepo.findByNotifyDateAndActiveAndIsSent(today, true, false);

        for (Notification notification : notifications) {

            if (notification.getTopic().isCompleted()) {
                notification.setActive(false);
                notificationRepo.save(notification);
                continue;
            }

            if (notification.getRetryCount() >= 3) {
                notification.setActive(false);
                notification.setSent(false);
                notificationRepo.save(notification);
                logger.warn("Max retry reached for notification {}", notification.getId());
                continue;
            }

            if (notification.getUser() == null || notification.getUser().getEmail() == null) {
                logger.error("User/email missing for notification {}", notification.getId());
                notification.setActive(false);
                notificationRepo.save(notification);
                continue;
            }

            MailDto mailDto = new MailDto(
                    notification.getUser().getEmail(),
                    "Reminder",
                    notification.getMessage()
            );

            try {
                ResponseEntity<MailResponseDto> response =
                        restTemplate.postForEntity(emailServiceUrl, mailDto, MailResponseDto.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    notification.setSent(true);
                    notification.setActive(false);
                    notification.setSentAt(today);
                    notificationRepo.save(notification);

                    logger.info("Mail sent successfully to {} for notification {}",
                            notification.getUser().getEmail(),
                            notification.getId());
                } else {
                    handleRetry(notification);
                }

            } catch (Exception ex) {
                logger.error("Mail sending failed for notification {}",
                        notification.getId(), ex);
                handleRetry(notification);
            }
        }

        logger.info("Notification scheduler finished");
    }

    private void handleRetry(Notification notification) {
        notification.setRetryCount(notification.getRetryCount() + 1);
        notification.setLastAttempt(LocalDateTime.now(clock));

        if (notification.getRetryCount() >= 3) {
            notification.setActive(false);
            notification.setSent(false);
            logger.warn("Giving up notification {} after max retries", notification.getId());
        } else {
            notification.setActive(true);
            notification.setSent(false);
            logger.info("Retrying notification {} (attempt {})",
                    notification.getId(),
                    notification.getRetryCount());
        }

        notificationRepo.save(notification);
    }



}
//git checkout -b newBranch
//# work
//git add .
//git commit -m "message"
//git push origin newBranch
//# PR → review → merge (GitHub)
//git checkout main
//git pull origin main
//git branch -d newBranch