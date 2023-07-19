package com.oracle.challenge.core;

public record TaskId(String value) {

    public TaskId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("task id is mandatory");
        }
    }
}