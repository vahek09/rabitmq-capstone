package com.vkarapetyan.producer.service;

import com.vkarapetyan.producer.dto.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(MessagePublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;
    private final AtomicLong counter = new AtomicLong(0);

    public MessagePublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.name}") String exchangeName,
            @Value("${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    // Rate: 10 messages per second (1 message every 100 ms)
    @Scheduled(fixedRate = 100)
    public void publishMessage() {
        long id = counter.incrementAndGet();
        MessagePayload payload = new MessagePayload(id, "HA-RabbitMQ-Message #" + id, Instant.now());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, payload);
        log.info("[PRODUCER] Published message #{}: to exchange '{}' with routing key '{}'", id, exchangeName, routingKey);
    }
}
