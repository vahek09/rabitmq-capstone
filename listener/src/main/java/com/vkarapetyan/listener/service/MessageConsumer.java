package com.vkarapetyan.listener.service;

import com.vkarapetyan.listener.dto.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleMessage(MessagePayload message) {
        log.info("[LISTENER] Received message #{}: content='{}', timestamp='{}'",
                message.id(), message.content(), message.timestamp());
    }
}
