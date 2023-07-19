package com.oracle.challenge.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.challenge.api.dto.TaskRequest;
import com.oracle.challenge.application.UpdateOrCreateTask;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateTaskResourceShould {

    private UpdateOrCreateTask updateOrCreateTask = mock(UpdateOrCreateTask.class);
    private UpdateTaskResource resource = new UpdateTaskResource(updateOrCreateTask);
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void update_task() {
        var date = OffsetDateTime.now().plusDays(2);
        var updateTaskRequest = new TaskRequest("a task", date);
        String taskId = UUID.randomUUID().toString();
        given(updateOrCreateTask.execute(taskId, updateTaskRequest.task(), date)).willReturn(true);

        final Response response = resource.updateOrCreateTask(taskId, updateTaskRequest);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void put_a_task_resource_that_does_not_exists_creates_a_new_one() throws JsonProcessingException {
        var date = OffsetDateTime.now().plusDays(2);
        var updateTaskRequest = new TaskRequest("a task", date);
        String taskId = UUID.randomUUID().toString();
        given(updateOrCreateTask.execute(taskId, updateTaskRequest.task(), date)).willReturn(false);

        final Response response = resource.updateOrCreateTask(taskId, updateTaskRequest);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
        assertThatJson(mapper.writeValueAsString(response.getEntity()))
                .isEqualTo(String.format("""
                        {"id": "%s"}
                        """, taskId));
    }

    @Test
    public void return_internal_server_error_if_update_task_failed_unexpectedly() throws JsonProcessingException {
        var updateTaskRequest = new TaskRequest("a task", OffsetDateTime.now());
        doThrow(new RuntimeException("unexpected error")).when(updateOrCreateTask).execute(any(), any(), any());

        final Response response = resource.updateOrCreateTask(UUID.randomUUID().toString(), updateTaskRequest);

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
