package com.oracle.challenge.core;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TaskShould {

    @Test
    public void should_fail_if_id_is_null_or_blank() {
        Throwable throwable = catchThrowable(
                () -> new Task(null, "a name", OffsetDateTime.now()));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("task id is mandatory");

        throwable = catchThrowable(
                () -> new Task(new TaskId(""), "a name", OffsetDateTime.now()));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("task id is mandatory");
    }

    @Test
    public void should_fail_if_name_is_null_or_blank() {
        Throwable throwable = catchThrowable(
                () -> new Task(new TaskId(UUID.randomUUID().toString()), null, OffsetDateTime.now()));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("task name is mandatory");

        throwable = catchThrowable(
                () -> new Task(new TaskId(UUID.randomUUID().toString()), "", OffsetDateTime.now()));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("task name is mandatory");
    }

    @Test
    public void should_fail_if_date_is_null() {
        Throwable throwable = catchThrowable(
                () -> new Task(null, "a name", null));

        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("task date is mandatory");
    }
}
