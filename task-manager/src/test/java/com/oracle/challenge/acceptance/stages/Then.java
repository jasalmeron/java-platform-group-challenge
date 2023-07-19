package com.oracle.challenge.acceptance.stages;


import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.http.HttpStatus;

import java.util.Set;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Then extends Stage<Then> {

    @ExpectedScenarioState
    private Response response;

    @ExpectedScenarioState
    private String updatedTaskJson;

    @ExpectedScenarioState
    private Set<String> jsonTasks;

    @ProvidedScenarioState
    private String taskId;

    public Then the_task_is_created(final String taskJsonBody, int port) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        var taskId = JsonPath.from(response.getBody().asString()).getString("id");
        assertThat(taskId).isNotEmpty();
        this.taskId = taskId;
        return this;
    }

    public Then the_created_task_is_found(final String taskJsonBody, int port) {
        final Response getResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/tasks/" + taskId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        final String retrievedTaskJson = getResponse.body().asString();

        assertThatJson(retrievedTaskJson)
                .whenIgnoringPaths("id")
                .isEqualTo(taskJsonBody);
        return this;
    }

    public Then the_task_is_deleted(final int port) {
        assertThat(this.response.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        final Response getResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/tasks/" + taskId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        return this;
    }

    public Then the_task_is_updated(final int port) {
        assertThat(this.response.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        final Response getResponse = RestAssured.given()
                .when()
                .port(port)
                .get("/tasks/" + taskId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        final String retrievedTaskJson = getResponse.body().asString();

        assertThatJson(retrievedTaskJson)
                .whenIgnoringPaths("id")
                .isEqualTo(updatedTaskJson);
        return this;
    }

    public Then the_tasks_are_retrieved() {
        assertThat(this.response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThatJson(this.response.getBody().asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(
                """
                    [
                        {"id": '${json-unit.any-string}', "task": "first task", "date": "2023-07-20T12:30:10Z"},
                        {"id": '${json-unit.any-string}', "task": "second task", "date": "2023-07-22T00:00:00Z"}
                    ]
                """
                );
        return this;
    }
}
