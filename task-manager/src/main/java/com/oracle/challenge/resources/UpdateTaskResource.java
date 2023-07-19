package com.oracle.challenge.resources;

import com.oracle.challenge.api.dto.CreateTaskResponse;
import com.oracle.challenge.api.dto.ErrorKey;
import com.oracle.challenge.api.dto.ErrorResponse;
import com.oracle.challenge.api.dto.TaskRequest;
import com.oracle.challenge.application.UpdateOrCreateTask;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("tasks")
public class UpdateTaskResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(UpdateTaskResource.class);
    private UpdateOrCreateTask updateOrCreateTask;

    public UpdateTaskResource(final UpdateOrCreateTask updateOrCreateTask) {
        this.updateOrCreateTask = updateOrCreateTask;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{taskId}")
    public Response updateOrCreateTask(@PathParam("taskId") final String taskId, TaskRequest updateTaskRequest) {
        try {
            boolean updated = this.updateOrCreateTask.execute(taskId, updateTaskRequest.task(), updateTaskRequest.date());
            if (updated) {
                return Response.noContent().build();
            } else {
                return Response.status(201)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .entity(new CreateTaskResponse(taskId))
                        .build();
            }
        } catch (Exception exception) {
            LOGGER.error("Unexpected server exception while updating task with id: {}", taskId, exception);
            return Response
                    .serverError()
                    .entity(new ErrorResponse(ErrorKey.INTERNAL_SERVER_ERROR, "Internal Server Error"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
