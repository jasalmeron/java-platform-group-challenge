package com.oracle.challenge.application;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;

import java.util.Optional;

public class GetTask {
    private TaskRepository taskRepository;

    public GetTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<Task> execute(final String taskId) {
        return taskRepository.findBy(new TaskId(taskId));
    }
}
