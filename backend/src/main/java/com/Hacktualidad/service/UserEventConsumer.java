package com.Hacktualidad.service;

import com.Hacktualidad.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "user-registration-topic", groupId = "hacktualidad-group")
    public void handleUserRegistrationEvent(UserResponseDTO user){
        System.out.println("Evento kafka para Email:" + user.getEmail());
        emailService.sendWelcomeEmail(user);
    }
}
