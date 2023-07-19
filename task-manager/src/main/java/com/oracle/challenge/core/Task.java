package com.oracle.challenge.core;

import java.time.OffsetDateTime;

public record Task(TaskId id, String name, OffsetDateTime date){

    public Task {
        if(id == null) {
            throw new IllegalArgumentException("task id is mandatory");
        }
        if(name == null) {
            throw new IllegalArgumentException("task name is mandatory");
        }
        if(date == null) {
            throw new IllegalArgumentException("task date is mandatory");
        }
    }
}
