package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateTaskResponse(@JsonProperty("id") String id) {
}
