package com.bridgelabz.bookstoreapp.utility;

import com.bridgelabz.bookstoreapp.dto.EmailDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RabbitMq {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    // PRODUCER
    public void sendMessageToQueue(EmailDto emailDto) {
        final String exchange = "rabbitExchange";
        final String routingKey = "rabbitKey";
        rabbitTemplate.convertAndSend(exchange, routingKey, emailDto);
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void send(EmailDto email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setFrom(email.getFrom());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        javaMailSender.send(message);
        System.out.println("Mail Sent Succesfully");
    }

    //LISTENER
   //@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receiveMessage(EmailDto email) {
        send(email);
    }
}