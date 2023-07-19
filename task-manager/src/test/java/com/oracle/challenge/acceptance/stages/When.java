package com.oracle.challenge.acceptance.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class When extends Stage<When> {


    @ExpectedScenarioState
    String taskJson;

    @ProvidedScenarioState
    String updatedTaskJson;;

    @ExpectedScenarioState
    String taskId;

    @ProvidedScenarioState
    private Response response;

    public When the_client_creates_the_task(int port) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .port(port)
                .post("/tasks");
        return this;
    }

    public When the_client_deletes_the_task(final int port) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .port(port)
                .delete("/tasks/" + taskId);
        return this;
    }

    public When the_client_updates_the_task(final int port) {

        final String toUpdateTaskJson = String.format("""
                {"task":  "Finish Java Platform Group challenge", "date": "2023-07-20T00:00:00Z"}
                """, taskId);
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(toUpdateTaskJson)
                .when()
                .port(port)
                .put("/tasks/" + taskId);
        updatedTaskJson = toUpdateTaskJson;
        return this;
    }

    public When the_client_gets_all_tasks(int port) {
        response = RestAssured.given()
                .when()
                .port(port)
                .get("/tasks/");
        return this;
    }
}
