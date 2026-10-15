package com.vkarapetyan.listener.dto;

import java.io.Serializable;
import java.time.Instant;

public record MessagePayload(
    long id,
    String content,
    Instant timestamp
) implements Serializable {}
