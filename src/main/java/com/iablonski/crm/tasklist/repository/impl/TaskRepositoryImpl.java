package com.iablonski.crm.tasklist.repository.impl;

import com.iablonski.crm.tasklist.domain.exception.ResourceMappingException;
import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.repository.DataSourceConfig;
import com.iablonski.crm.tasklist.repository.TaskRepository;
import com.iablonski.crm.tasklist.web.mappers.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;
    private final String FIND_BY_ID = """
            SELECT t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM tasks.task_list t
            WHERE id = ?
            """;

    private final String FIND_ALL_BY_USER_ID = """
            SELECT t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM tasks.task_list t
                     JOIN tasks.user_task ut ON t.id = ut.task_id
            WHERE ut.user_id = ?
            """;

    private final String ASSIGN = """
            INSERT INTO tasks.user_task(task_id, user_id)
            VALUES (?, ?)
            """;

    private final String UPDATE = """
            UPDATE tasks.task_list
            SET title           = ?,
                description     = ?,
                expiration_date = ?,
                status          = ?
            WHERE id = ?
            """;

    private final String CREATE = """
            INSERT INTO tasks.task_list(title, description, status, expiration_date)
            VALUES (?, ?, ?, ?)
            """;

    private final String DELETE = """
            DELETE
            FROM tasks.task_list
            WHERE id = ?
            """;

    @Override
    public Optional<Task> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            try (ResultSet set = statement.executeQuery()) {
                return Optional.ofNullable(TaskRowMapper.mapRow(set));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding task by id");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userId);
            try (ResultSet set = statement.executeQuery()) {
                return TaskRowMapper.mapRows(set);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding all tasks by user id");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while assigning task to user");
        }
    }

    @Override
    public void update(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationTime() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationTime()));
            }
            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating task");
        }
    }

    @Override
    public void create(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            statement.setString(3, task.getStatus().name());
            if (task.getExpirationTime() == null) {
                statement.setNull(4, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(task.getExpirationTime()));
            }
            statement.executeUpdate();
            try (ResultSet set = statement.getGeneratedKeys()){
                set.next();
                task.setId(set.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating task");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting task");
        }
    }
}