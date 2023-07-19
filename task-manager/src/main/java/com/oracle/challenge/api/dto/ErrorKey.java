package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorKey {

    INTERNAL_SERVER_ERROR("task-manager.internal-server-error");

    private String errorKey;

    ErrorKey(final String errorKey) {

        this.errorKey = errorKey;
    }

    @JsonValue
    @JsonProperty("errorKey")
    public String getErrorKey() {
        return errorKey;
    }
}
