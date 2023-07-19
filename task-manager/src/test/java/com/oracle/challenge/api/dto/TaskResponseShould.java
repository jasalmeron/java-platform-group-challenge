package com.oracle.challenge.api.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

class TaskResponseShould {

    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void should_deserialize_to_json_a_task_response() throws JsonProcessingException {
        var date = OffsetDateTime.of(2023, 07, 15, 0, 0, 0, 0, ZoneOffset.UTC);
        var task = new TaskResponse("1af7bfe9-409e-419d-8245-5c462a44f614", "a task", date);

        var taskJson = mapper.writeValueAsString(task);

        assertThatJson(taskJson).isEqualTo(
                """
                {
                    "id": "1af7bfe9-409e-419d-8245-5c462a44f614",
                    "task": "a task",
                    "date": "2023-07-15T00:00:00Z"
                }
                """
        );
    }
}
