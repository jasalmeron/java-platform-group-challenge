package com.oracle.challenge.acceptance;

import com.oracle.challenge.acceptance.stages.Given;
import com.oracle.challenge.acceptance.stages.Then;
import com.oracle.challenge.acceptance.stages.When;
import org.junit.jupiter.api.Test;

public class GetAllTasksAcceptance extends AcceptanceTest<Given, When, Then>{

    @Test
    public void should_get_all_offers() {
        given()
                .a_set_of_tasks_are_created();
        when()
                .the_client_gets_all_tasks(APP.getLocalPort());
        then()
                .the_tasks_are_retrieved();
    }
}
