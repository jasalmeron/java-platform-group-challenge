package com.oracle.challenge.acceptance;

import com.oracle.challenge.acceptance.stages.Given;
import com.oracle.challenge.acceptance.stages.Then;
import com.oracle.challenge.acceptance.stages.When;
import org.junit.jupiter.api.Test;

public class UpdateTaskAcceptance extends AcceptanceTest<Given, When, Then> {

    @Test
    public void should_update_task() {
        final int port = APP.getLocalPort();
        given()
                .a_task("""
                        {"task":  "Start Java Platform Group challenge", "date": "2023-07-15T00:00:00Z"}
                        """)
                        .and()
                .the_task_is_created(port);
        when()
                .the_client_updates_the_task(port);
        then()
                .the_task_is_updated(port);
    }

    @Test
    public void should_create_task_if_not_exists_when_updated() {
        final int port = APP.getLocalPort();
        final String taskJson = """
                {"task":  "Start Java Platform Group challenge", "date": "2023-07-15T00:00:00Z"}
                """;
        given()
                .a_task(taskJson)
                .and()
                .a_task_id();
        when()
                .the_client_updates_the_task(port);
        then()
                .the_task_is_created(taskJson, port);
    }

}
