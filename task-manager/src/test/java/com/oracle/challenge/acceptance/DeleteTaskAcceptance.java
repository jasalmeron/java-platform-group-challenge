package com.oracle.challenge.acceptance;

import com.oracle.challenge.acceptance.stages.Given;
import com.oracle.challenge.acceptance.stages.Then;
import com.oracle.challenge.acceptance.stages.When;
import org.junit.jupiter.api.Test;

public class DeleteTaskAcceptance extends AcceptanceTest<Given, When, Then> {

    @Test
    public void should_delete_task() {
        final int port = APP.getLocalPort();
        given()
                .a_task("""
                        {"task":  "Start Java Platform Group challenge", "date": "2023-07-15T00:00:00Z"}
                        """)
                        .and()
                .the_task_is_created(port);
        when()
                .the_client_deletes_the_task(port);
        then()
                .the_task_is_deleted(port);
    }

}
