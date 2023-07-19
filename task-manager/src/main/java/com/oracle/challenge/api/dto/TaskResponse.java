package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record TaskResponse(
        @JsonProperty
        String id,
        @JsonProperty("task")
        String task,
        @JsonProperty("date")
        OffsetDateTime date) {
}