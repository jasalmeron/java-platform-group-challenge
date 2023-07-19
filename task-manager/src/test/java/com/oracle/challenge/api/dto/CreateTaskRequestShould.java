package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTaskRequestShould {

    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void serializeValidTask() throws JsonProcessingException {
        var createTaskRequest = mapper.readValue("""
                {
                    "task": "a new task",
                    "date": "2023-07-15T00:00:00Z"
                }
                """, TaskRequest.class);

        assertThat(createTaskRequest)
                .isEqualTo(
                        new TaskRequest(
                                "a new task",
                                OffsetDateTime.of(2023, 07, 15, 0, 0, 0, 0, ZoneOffset.UTC)
                        )
                );
    }
}
