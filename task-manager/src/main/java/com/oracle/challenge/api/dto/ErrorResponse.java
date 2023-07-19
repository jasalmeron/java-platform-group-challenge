package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(@JsonProperty("errorKey") ErrorKey errorKey, @JsonProperty("message") String message) {
}
