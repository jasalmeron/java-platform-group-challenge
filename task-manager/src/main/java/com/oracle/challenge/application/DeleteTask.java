package com.oracle.challenge.application;

import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;

public class DeleteTask {

    private TaskRepository repository;

    public DeleteTask(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @param taskId
     * @return true if the task was deleted or false if it didn't exist
     */
    public boolean execute(final String taskId) {
        return repository.deleteBy(new TaskId(taskId));
    }
}
