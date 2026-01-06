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


    @Scheduled(cron = "0 */1 * * * *")
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


    @Scheduled(cron = "0 */1 * * * * ")
    public void sentNotifications() {

        LocalDate today = LocalDate.now(clock);

        List<Notification> notifications =
                notificationRepo.findByNotifyDateAndActiveAndIsSent(today, true, false);

        for (Notification notification : notifications) {

            if (notification.getTopic().isCompleted()) {
                continue;
            }

            if (notification.getRetryCount() >= 3) {
                notification.setActive(false);
                notification.setLastAttempt(LocalDateTime.now(clock));
                notificationRepo.save(notification);
                continue;
            }

            notification.setLastAttempt(LocalDateTime.now(clock));

            try {
                if (notification.getUser() == null || notification.getUser().getEmail() == null) {
                    throw new IllegalStateException("User or email missing");
                }
                MailDto mailDto = new MailDto(
                        notification.getUser().getEmail(),
                        "Reminder",
                        notification.getMessage()
                );

                ResponseEntity<MailResponseDto> response =
                        restTemplate.postForEntity(
                                emailServiceUrl,
                                mailDto,
                                MailResponseDto.class
                        );

                if (response.getStatusCode().is2xxSuccessful()) {
                    notification.setSentAt(today);
                    notification.setSent(true);
                    notification.setActive(false);
                    logger.info(
                            "Mail sent successfully to {} for notification {}",
                            notification.getUser().getEmail(),
                            notification.getId()
                    );

                } else {
                    handleRetry(notification);
                }
            } catch (Exception ex) {
                handleRetry(notification);
                logger.error(
                        "Failed to send mail to {} for notification {}",
                        notification.getUser().getEmail(),
                        notification.getId(),
                        ex);
            }
            notificationRepo.save(notification);
        }
        logger.info("Notification scheduler finished");
    }

    private void handleRetry(Notification notification) {
        notification.setRetryCount(notification.getRetryCount() + 1);
        notification.setSent(false);
        notification.setActive(notification.getRetryCount() < 3);
    }


}
