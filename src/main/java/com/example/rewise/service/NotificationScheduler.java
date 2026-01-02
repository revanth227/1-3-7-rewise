package com.example.rewise.service;

import com.example.rewise.dto.MailDto;
import com.example.rewise.dto.ResponseDto;
import com.example.rewise.entity.Notification;
import com.example.rewise.repo.NotificationRepo;
import com.example.rewise.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.LocalDate;
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
            if (notification.getUser() != null && notification.getUser().getEmail() != null) {
                MailDto mailDto = new MailDto(notification.getUser().getEmail(), "Remainder", notification.getMessage());
                String url = "http://localhost:8081/email/send";
                ResponseEntity<ResponseDto> response =
                        restTemplate.postForEntity(
                                url,
                                mailDto,
                                ResponseDto.class
                        );
                if (response.getStatusCode().is2xxSuccessful()) {
                    notification.setSentAt(today);
                    notification.setActive(false);
                    notification.setSent(true);
                    notificationRepo.save(notification);
                }
            }

            System.out.println(
                    "Mail sent to " + notification.getUser().getEmail()
            );


            System.out.println("Message Sent to User " + notification.getUser().getName());
        }


    }


}
