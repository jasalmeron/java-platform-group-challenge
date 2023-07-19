package com.oracle.challenge.acceptance.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Given extends Stage<Given> {

    @ProvidedScenarioState
    private String taskJson;

    @ProvidedScenarioState
    private String taskId;

    @ProvidedScenarioState
    private Set<String> jsonTasks;

    public Given a_task(final String taskJson) {
        this.taskJson = taskJson;
        return this;
    }

    public Given the_task_is_created(int port) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when()
                .port(port)
                .post("/tasks");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        var taskId = JsonPath.from(response.getBody().asString()).getString("id");
        assertThat(taskId).isNotEmpty();
        this.taskId = taskId;
        return this;
    }

    public Given a_task_id() {
        this.taskId = UUID.randomUUID().toString();
        return this;
    }

    public Given a_set_of_tasks_are_created() {
        jsonTasks = Set.of("""
                {"task": "first task", "date": "2023-07-20T12:30:10Z"}
            """,
            """
                {"task": "second task", "date": "2023-07-22T00:00:00Z"}
            """);
        jsonTasks.stream()
                .forEach(taskJson ->
                        RestAssured.given()
                                .contentType(ContentType.JSON)
                                .body(taskJson)
                                .when()
                                .post("/tasks")
                                .then()
                                .statusCode(HttpStatus.SC_CREATED)
                                .extract().response()
                );
        return this;
    }
}
