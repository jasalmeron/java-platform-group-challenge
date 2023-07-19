package com.oracle.challenge.resources;

import com.oracle.challenge.api.dto.ErrorKey;
import com.oracle.challenge.api.dto.ErrorResponse;
import com.oracle.challenge.api.dto.TaskResponse;
import com.oracle.challenge.application.GetTasks;
import com.oracle.challenge.core.Task;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Path("tasks")
public class GetTasksResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(GetTasksResource.class);
    private GetTasks getTasks;

    public GetTasksResource(final GetTasks getTasks) {
        this.getTasks = getTasks;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask() {
        try {
            final List<Task> tasks = this.getTasks.execute();
            final List<TaskResponse> tasksResponse = tasks.stream().map(task -> new TaskResponse(task.id().value(), task.name(), task.date()))
                    .collect(Collectors.toList());
            return Response.ok().entity(tasksResponse).build();

        } catch (Exception exception) {
            LOGGER.error("Unexpected server exception while retrieving all tasks", exception);
            return Response
                    .serverError()
                    .entity(new ErrorResponse(ErrorKey.INTERNAL_SERVER_ERROR, "Internal Server Error"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}

