package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record TaskRequest(
        @NotBlank
        @Size(max = 255)
        @JsonProperty("task")
        String task,
        @NotNull
        @JsonProperty("date") OffsetDateTime date
) {}
