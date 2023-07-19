package com.oracle.challenge.acceptance;

import com.oracle.challenge.acceptance.stages.Given;
import com.oracle.challenge.acceptance.stages.Then;
import com.oracle.challenge.acceptance.stages.When;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CreateTaskAcceptance extends AcceptanceTest<Given, When, Then> {

    @ParameterizedTest
    @CsvSource(delimiterString = "ø", textBlock = """
            {"task":  "Start Java Platform Group challenge", "date": "2023-07-15T00:00:00Z"}
            {"task":  "Deploy challenge to OCP", "date": "2023-07-16T00:00:00Z"}
            {"task":  "Send completed challenge", "date": "2023-07-17T00:00:00Z"}
            """)
    public void should_create_task(String taskJsonBody) {
        final int localPort = APP.getLocalPort();
        given()
                .a_task(taskJsonBody);
        when()
                .the_client_creates_the_task(localPort);
        then()
                .the_task_is_created(taskJsonBody, localPort)
                .and()
                .the_created_task_is_found(taskJsonBody, localPort);
    }

}
