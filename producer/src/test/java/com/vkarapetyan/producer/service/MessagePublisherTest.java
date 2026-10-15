package com.vkarapetyan.producer.service;

import com.vkarapetyan.producer.dto.MessagePayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessagePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void testPublishMessage() {
        String exchange = "ha.direct.exchange";
        String routingKey = "ha.routing.key";
        MessagePublisher publisher = new MessagePublisher(rabbitTemplate, exchange, routingKey);

        publisher.publishMessage();

        ArgumentCaptor<MessagePayload> payloadCaptor = ArgumentCaptor.forClass(MessagePayload.class);
        verify(rabbitTemplate).convertAndSend(org.mockito.ArgumentMatchers.eq(exchange),
                org.mockito.ArgumentMatchers.eq(routingKey),
                payloadCaptor.capture());

        MessagePayload captured = payloadCaptor.getValue();
        assertNotNull(captured);
        assertEquals(1, captured.id());
        assertNotNull(captured.timestamp());
    }
}
