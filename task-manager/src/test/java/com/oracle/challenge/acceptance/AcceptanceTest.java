package com.oracle.challenge.acceptance;

import com.oracle.challenge.framework.configuration.TaskManagerApplication;
import com.oracle.challenge.framework.configuration.TaskManagerConfiguration;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.jgiven.junit5.ScenarioTest;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ExtendWith({DropwizardExtensionsSupport.class, JGivenExtension.class})
public class AcceptanceTest<GIVEN, WHEN, THEN> extends ScenarioTest<GIVEN, WHEN, THEN> {

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.33"));

    private static final String CONFIG = "config.yml";

    public static final DropwizardAppExtension<TaskManagerConfiguration> APP = new DropwizardAppExtension<>(
            TaskManagerApplication.class, CONFIG, new ResourceConfigurationSourceProvider(),
            ConfigOverride.config("database.url", MY_SQL_CONTAINER::getJdbcUrl),
            ConfigOverride.config("database.user", MY_SQL_CONTAINER::getUsername),
            ConfigOverride.config("database.password", MY_SQL_CONTAINER::getPassword)
    );

    @BeforeAll
    public static void migrateDb() throws Exception {
        APP.getApplication().run("db", "migrate", ResourceHelpers.resourceFilePath(CONFIG));
    }
}
