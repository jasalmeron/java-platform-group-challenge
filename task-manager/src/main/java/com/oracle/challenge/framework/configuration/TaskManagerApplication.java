package com.oracle.challenge.framework.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oracle.challenge.application.CreateTask;
import com.oracle.challenge.application.DeleteTask;
import com.oracle.challenge.application.GetTask;
import com.oracle.challenge.application.GetTasks;
import com.oracle.challenge.application.UpdateOrCreateTask;
import com.oracle.challenge.core.TaskRepository;
import com.oracle.challenge.db.SQLTaskRepository;
import com.oracle.challenge.resources.CreateTaskResource;
import com.oracle.challenge.resources.DeleteTaskResource;
import com.oracle.challenge.resources.GetTaskResource;
import com.oracle.challenge.resources.GetTasksResource;
import com.oracle.challenge.resources.UpdateTaskResource;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.JdbiHealthCheck;
import io.dropwizard.migrations.MigrationsBundle;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManagerApplication extends Application<TaskManagerConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManagerApplication.class);

    public static void main(final String[] args) throws Exception {
        new TaskManagerApplication().run(args);
    }

    @Override
    public String getName() {
        return "TaskManager";
    }

    @Override
    public void initialize(final Bootstrap<TaskManagerConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(final TaskManagerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        enableConfigSubstitutionWithEnvVariables(bootstrap);
    }

    @Override
    public void run(final TaskManagerConfiguration config, final Environment environment) {
        LOGGER.info("Initializing JDBI");
        final Jdbi jdbi = new JdbiFactory().build(environment, config.getDataSourceFactory(), "database");
        TaskRepository taskRepository = new SQLTaskRepository(jdbi);
        LOGGER.info("Registering REST resources to Jersey");
        environment.jersey().register(new CreateTaskResource(new CreateTask(taskRepository)));
        environment.jersey().register(new GetTaskResource(new GetTask(taskRepository)));
        environment.jersey().register(new DeleteTaskResource(new DeleteTask(taskRepository)));
        environment.jersey().register(new UpdateTaskResource(new UpdateOrCreateTask(taskRepository)));
        environment.jersey().register(new GetTasksResource(new GetTasks(taskRepository)));
        configureJsonMapper(environment.getObjectMapper());
        LOGGER.info("Registering Application Health Check");
        environment.healthChecks().register("db", new JdbiHealthCheck(jdbi, config.getDataSourceFactory().getValidationQuery()));
    }

    private void configureJsonMapper(final ObjectMapper mapper) {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }

    private void enableConfigSubstitutionWithEnvVariables(final Bootstrap<TaskManagerConfiguration> bootstrap) {
        SubstitutingSourceProvider substitutingSourceProvider = new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false));
        bootstrap.setConfigurationSourceProvider(substitutingSourceProvider);
    }

}
