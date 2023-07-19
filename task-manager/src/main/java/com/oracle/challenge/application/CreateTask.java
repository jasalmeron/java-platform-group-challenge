package com.oracle.challenge.application;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateTask {

    private TaskRepository taskRepository;

    public CreateTask(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }

    public TaskId execute(final String taskName, final OffsetDateTime taskDate) {
        final TaskId id = new TaskId(UUID.randomUUID().toString());
        taskRepository.saveNew(new Task(id, taskName, taskDate));
        return id;
    }
}
