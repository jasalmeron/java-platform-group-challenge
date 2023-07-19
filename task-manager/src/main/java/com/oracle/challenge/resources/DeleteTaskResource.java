package com.oracle.challenge.resources;

import com.oracle.challenge.api.dto.ErrorKey;
import com.oracle.challenge.api.dto.ErrorResponse;
import com.oracle.challenge.application.DeleteTask;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("tasks")
public class DeleteTaskResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeleteTaskResource.class);
    private DeleteTask deleteTask;

    public DeleteTaskResource(final DeleteTask deleteTask) {
        this.deleteTask = deleteTask;
    }

    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") final String taskId) {
        try {
            boolean deleted = this.deleteTask.execute(taskId);
            if(deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(404).build();
            }

        } catch (Exception exception) {
            LOGGER.error("Unexpected server exception while deleting task with id: {}", taskId, exception);
            return Response
                    .serverError()
                    .entity(new ErrorResponse(ErrorKey.INTERNAL_SERVER_ERROR, "Internal Server Error"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
