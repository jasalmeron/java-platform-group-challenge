package com.oracle.challenge.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.challenge.application.DeleteTask;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class DeleteTaskResourceShould {

    private DeleteTask deleteTask = mock(DeleteTask.class);
    private DeleteTaskResource resource = new DeleteTaskResource(deleteTask);
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void delete_task() {
        String taskId = "871deda8-4dde-418d-8416-ce6750b93208";
        given(deleteTask.execute(taskId)).willReturn(true);

        final Response response = resource.deleteTask(taskId);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void delete_task_that_doesn_not_exist_returns_not_found() {
        String taskId = "871deda8-4dde-418d-8416-ce6750b93208";
        given(deleteTask.execute(taskId)).willReturn(false);

        final Response response = resource.deleteTask(taskId);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void return_internal_server_error_if_delete_task_failed_unexpectedly() throws JsonProcessingException {
        doThrow(new RuntimeException("unexpected error")).when(deleteTask).execute(any());

        final Response response = resource.deleteTask("871deda8-4dde-418d-8416-ce6750b93208");

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
