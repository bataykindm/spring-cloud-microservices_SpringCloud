package com.javastart.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.notification.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class DepositMessageHandler {

    private final JavaMailSender javaMailSender;

    @Autowired
    public DepositMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DEPOSIT)
    public void receive(Message message) throws JsonProcessingException {
        System.out.println(message);
        byte[] body = message.getBody();
        String jsonBody = new String(body);
        ObjectMapper objectMapper = new ObjectMapper();
        DepositResponseDTO depositResponseDTO = objectMapper.readValue(jsonBody, DepositResponseDTO.class);
        System.out.println(depositResponseDTO);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(depositResponseDTO.getEmail());      Need to activate for real work
        mailMessage.setTo("bataykinjava@gmail.com");  //         Need to delete for real work
        mailMessage.setFrom("bataykinjava@yandex.ru");
        mailMessage.setSubject("Deposit");
        mailMessage.setText("Deposit of " + depositResponseDTO.getAmount() + " is done");

        javaMailSender.send(mailMessage);
    }
}
