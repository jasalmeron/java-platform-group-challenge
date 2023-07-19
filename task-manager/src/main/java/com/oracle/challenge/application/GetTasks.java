package com.oracle.challenge.application;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskRepository;

import java.util.List;

public class GetTasks {

    private TaskRepository repository;

    public GetTasks(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> execute() {
        return this.repository.getAll();
    }
}
