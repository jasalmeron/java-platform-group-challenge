package com.oracle.challenge.db;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import com.oracle.challenge.core.TaskRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class SQLTaskRepository implements TaskRepository {

    private DAO dao;

    public SQLTaskRepository(final Jdbi jdbi) {
        this.dao = jdbi.onDemand(DAO.class);
    }

    @Override
    public void saveNew(final Task task) {
        this.dao.insert(task.id().value(), task.name(), task.date());
    }

    @Override
    public Optional<Task> findBy(final TaskId id) {
        return Optional.ofNullable(this.dao.findNameById(id.value()));
    }

    @Override
    public boolean deleteBy(final TaskId taskId) {
        return this.dao.deleteTaskById(taskId.value());
    }

    @Override
    public boolean update(final Task task) {
        return this.dao.updateTask(task.name(), task.date(), task.id().value());
    }

    @Override
    public List<Task> getAll() {
        return this.dao.getAll();
    }

    interface DAO {

        @SqlUpdate("insert into task (id, name, date) values (:id, :name, :date)")
        void insert(@Bind("id") String id, @Bind("name") String name, @Bind("date") OffsetDateTime date);

        @SqlQuery("select * from task where id = :id")
        @RegisterRowMapper(TaskRowMapper.class)
        Task findNameById(@Bind("id") String id);

        @SqlUpdate("delete from task where id = :id")
        boolean deleteTaskById(@Bind("id") String id);

        @SqlUpdate("update task set name = :name, date = :date where id = :id")
        boolean updateTask(@Bind("name") String name, @Bind("date") OffsetDateTime date, @Bind("id") String id);

        @SqlQuery("select 1 from task where id = :id")
        Integer existsTask(@Bind("id") String id);

        @SqlQuery("select * from task")
        @RegisterRowMapper(TaskRowMapper.class)
        List<Task> getAll();
    }
}
