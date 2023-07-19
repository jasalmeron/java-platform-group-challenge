package com.oracle.challenge.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oracle.challenge.application.GetTask;
import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetTaskResourceShould {

    private GetTask createTask = mock(GetTask.class);
    private GetTaskResource resource = new GetTaskResource(createTask);
    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void create_task() throws JsonProcessingException {
        var date = OffsetDateTime.of(2023, 07, 15, 0, 0, 0, 0, ZoneOffset.UTC);
        String taskId = "871deda8-4dde-418d-8416-ce6750b93208";
        given(createTask.execute(taskId))
                .willReturn(Optional.of(new Task(new TaskId(taskId), "task name", date)));

        final Response response = resource.getTask(taskId);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        assertThatJson(mapper.writeValueAsString(response.getEntity()))
                .isEqualTo("""
                        {
                            "id": "871deda8-4dde-418d-8416-ce6750b93208",
                            "task": "task name",
                            "date": "2023-07-15T00:00:00Z"
                        }
                        """);
    }

    @Test
    public void return_internal_server_error_if_get_task_failed_unexpectedly() throws JsonProcessingException {
        given(createTask.execute(any())).willThrow(new RuntimeException("unexpected error"));

        final Response response = resource.getTask("871deda8-4dde-418d-8416-ce6750b93208");

        assertThat(response.getStatus()).isEqualTo(500);
        assertThatJson(mapper.writeValueAsString(response.getEntity()))
                .isEqualTo("""
                        {
                         "errorKey": "task-manager.internal-server-error",
                         "message": "Internal Server Error"
                        }
                        """);
    }
}