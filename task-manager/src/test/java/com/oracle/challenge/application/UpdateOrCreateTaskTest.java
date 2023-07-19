package com.oracle.challenge.application;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateOrCreateTaskTest {

    private TaskRepository repository = mock(TaskRepository.class);
    private UpdateOrCreateTask updateOrCreateTask = new UpdateOrCreateTask(repository);
    private Task task = new Task(new TaskId("ebdf62b5-7d34-4607-9455-9fad44c28ad0"), "a name", OffsetDateTime.now());

    @Test
    public void should_update_task() {
        given(repository.update(task)).willReturn(true);

        final boolean updated = updateOrCreateTask.execute(task.id().value(), task.name(), task.date());

        verify(repository).update(eq(task));
        assertThat(updated).isTrue();
    }

    @Test
    public void should_create_task() {
        given(repository.update(task)).willReturn(false);

        final boolean updated = updateOrCreateTask.execute(task.id().value(), task.name(), task.date());

        verify(repository).update(eq(task));
        verify(repository).saveNew(eq(task));
        assertThat(updated).isFalse();
    }

}