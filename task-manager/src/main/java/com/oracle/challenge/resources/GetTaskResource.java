package com.oracle.challenge.resources;

import com.oracle.challenge.api.dto.ErrorKey;
import com.oracle.challenge.api.dto.ErrorResponse;
import com.oracle.challenge.api.dto.TaskResponse;
import com.oracle.challenge.application.GetTask;
import com.oracle.challenge.core.Task;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Path("tasks")
public class GetTaskResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(GetTaskResource.class);
    private GetTask getTask;

    public GetTaskResource(final GetTask getTask) {
        this.getTask = getTask;
    }

    @GET
    @Path("/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("taskId") final String taskId) {
        LOGGER.debug("Get task resource with id {}", taskId);
        try {
            final Optional<Task> task = this.getTask.execute(taskId);
            if (task.isPresent()) {
                var retrievedTask = task.get();
                var taskResponse = new TaskResponse(retrievedTask.id().value(), retrievedTask.name(), retrievedTask.date());
                return Response.ok().entity(taskResponse).build();
            } else {
                return Response.status(404).build();
            }
        } catch (Exception exception) {
            LOGGER.error("Unexpected server exception while retrieving task with id: {}", taskId, exception);
            return Response
                    .serverError()
                    .entity(new ErrorResponse(ErrorKey.INTERNAL_SERVER_ERROR, "Internal Server Error"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
