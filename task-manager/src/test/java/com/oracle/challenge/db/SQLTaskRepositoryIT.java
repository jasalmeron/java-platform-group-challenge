package com.oracle.challenge.db;


import com.oracle.challenge.BaseIntegrationTest;
import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;
import io.dropwizard.jdbi3.JdbiFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class SQLTaskRepositoryIT extends BaseIntegrationTest {

    private TaskRepository repository;

    @BeforeEach
    public void setUp() {
        final int random = new Random().nextInt(100000);
        repository = new SQLTaskRepository(new JdbiFactory().build(APP.getEnvironment(), APP.getConfiguration().getDataSourceFactory(), "database-test-" + random));
    }

    @Test
    public void should_create_task() {
        var id = new TaskId(UUID.randomUUID().toString());
        var task = new Task(
                id, "some task name", OffsetDateTime.of(2023, 07, 15, 0, 0, 0, 0, ZoneOffset.UTC)
        );

        repository.saveNew(task);

        final Optional<Task> foundTask = repository.findBy(id);
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get()).isEqualTo(task);
    }

    @Test
    public void should_delete_task() {
        var id = new TaskId(UUID.randomUUID().toString());
        var task = new Task(
                id, "some task name", OffsetDateTime.of(2023, 07, 15, 0, 0, 0, 0, ZoneOffset.UTC)
        );
        repository.saveNew(task);

        repository.deleteBy(id);

        final Optional<Task> notFoundTask = repository.findBy(id);
        assertThat(notFoundTask).isEmpty();
    }

    @Test
    public void should_update_task() {
        var id = new TaskId(UUID.randomUUID().toString());
        var task = new Task(
                id, "some task name", OffsetDateTime.of(2023, 07, 15, 0, 0, 0, 0, ZoneOffset.UTC)
        );
        repository.saveNew(task);
        var changedTask = new Task(task.id(), "changed task name", OffsetDateTime.of(2023, 07, 20, 0, 0, 0, 0, ZoneOffset.UTC));

        repository.update(changedTask);

        final Optional<Task> retrievedChangedTask = repository.findBy(id);
        assertThat(retrievedChangedTask).isPresent();
        assertThat(retrievedChangedTask.get())
                .isEqualTo(changedTask);
    }
}
