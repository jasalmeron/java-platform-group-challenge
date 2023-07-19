package com.oracle.challenge.application;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;

import java.time.OffsetDateTime;

public class UpdateOrCreateTask {

    private TaskRepository repository;

    public UpdateOrCreateTask(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @param taskId
     * @param taskName
     * @param date
     * @return true if the task has been update or false if it was created because it didn't exist
     */
    public boolean execute(final String taskId, final String taskName, final OffsetDateTime date) {
        final Task task = new Task(new TaskId(taskId), taskName, date);
        boolean updated = this.repository.update(task);
        if (!updated) {
            this.repository.saveNew(task);
        }
        return updated;
    }
}
