package com.oracle.challenge.core;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    void saveNew(Task task);

    Optional<Task> findBy(TaskId id);

    boolean deleteBy(TaskId taskId);

    boolean update(Task task);

    List<Task> getAll();
}
