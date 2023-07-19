package com.oracle.challenge.db;

import com.oracle.challenge.core.Task;
import com.oracle.challenge.core.TaskId;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        return new Task(
                new TaskId(rs.getString("id")),
                rs.getString("name"),
                OffsetDateTime
                        .ofInstant(rs.getTimestamp("date").toInstant(), ZoneOffset.UTC)
        );
    }

    @Override
    public RowMapper<Task> specialize(final ResultSet rs, final StatementContext ctx) throws SQLException {
        return RowMapper.super.specialize(rs, ctx);
    }

    @Override
    public void init(final ConfigRegistry registry) {
        RowMapper.super.init(registry);
    }
}
