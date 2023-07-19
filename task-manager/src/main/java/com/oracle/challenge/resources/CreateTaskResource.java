package com.oracle.challenge.resources;

import com.oracle.challenge.api.dto.TaskRequest;
import com.oracle.challenge.api.dto.CreateTaskResponse;
import com.oracle.challenge.api.dto.ErrorKey;
import com.oracle.challenge.api.dto.ErrorResponse;
import com.oracle.challenge.application.CreateTask;
import com.oracle.challenge.core.TaskId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("tasks")
public class CreateTaskResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTaskResource.class);
    private CreateTask createTask;

    public CreateTaskResource(final CreateTask createTask) {

        this.createTask = createTask;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response create(@NotNull @Valid final TaskRequest createTaskRequest) {
        try {
            TaskId taskId = this.createTask.execute(createTaskRequest.task(), createTaskRequest.date());
            return Response.status(201)
                    .entity(new CreateTaskResponse(taskId.value()))
                    .build();
        } catch (Exception exception) {
            LOGGER.error("Unexpected server exception while saving new task:", createTaskRequest, exception);
            return Response
                    .serverError()
                    .entity(new ErrorResponse(ErrorKey.INTERNAL_SERVER_ERROR, "Internal Server Error"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
