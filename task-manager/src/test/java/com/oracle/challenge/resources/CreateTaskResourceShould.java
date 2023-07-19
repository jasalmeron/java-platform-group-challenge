package com.oracle.challenge.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.challenge.api.dto.TaskRequest;
import com.oracle.challenge.application.CreateTask;
import com.oracle.challenge.core.TaskId;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CreateTaskResourceShould {

    private CreateTask createTask = mock(CreateTask.class);
    private CreateTaskResource resource = new CreateTaskResource(createTask);
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void create_task() throws JsonProcessingException {
        var date = OffsetDateTime.now().plusDays(2);
        var createTaskRequest = new TaskRequest("a task", date);
        String expectedTaskId = UUID.randomUUID().toString();
        given(createTask.execute("a task", date)).willReturn(new TaskId(expectedTaskId));

        final Response response = resource.create(createTaskRequest);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
        assertThatJson(mapper.writeValueAsString(response.getEntity()))
                .isEqualTo(String.format("""
                        {"id": "%s"}
                        """, expectedTaskId));
    }

    @Test
    public void return_internal_server_error_if_create_task_failed_unexpectedly() throws JsonProcessingException {
        var date = OffsetDateTime.now().plusDays(2);
        var createTaskRequest = new TaskRequest("a task", date);
        given(createTask.execute(any(), any())).willThrow(new RuntimeException("unexpected error"));

        final Response response = resource.create(createTaskRequest);

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
