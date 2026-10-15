package com.vkarapetyan.listener.service;

import com.vkarapetyan.listener.dto.MessagePayload;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MessageConsumerTest {

    @Test
    void testHandleMessage() {
        MessageConsumer consumer = new MessageConsumer();
        MessagePayload payload = new MessagePayload(100L, "Test Content", Instant.now());

        assertDoesNotThrow(() -> consumer.handleMessage(payload));
    }
}
