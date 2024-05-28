package com.iablonski.crm.tasklist.repository.impl;

import com.iablonski.crm.tasklist.domain.exception.ResourceMappingException;
import com.iablonski.crm.tasklist.domain.user.Role;
import com.iablonski.crm.tasklist.domain.user.User;
import com.iablonski.crm.tasklist.repository.DataSourceConfig;
import com.iablonski.crm.tasklist.repository.UserRepository;
import com.iablonski.crm.tasklist.web.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT u.id               as user_id,
                   u.name             as user_name,
                   u.username         as user_username,
                   u.password         as user_password,
                   ur.role            as user_role_role,
                   tl.id              as task_id,
                   tl.title           as task_title,
                   tl.description     as task_description,
                   tl.expiration_date as task_expiration_date,
                   tl.status          as task_status
            FROM tasks.user_list u
                     LEFT JOIN tasks.user_roles ur on u.id = ur.user_id
                     LEFT JOIN tasks.user_task ut on u.id = ut.user_id
                     LEFT JOIN tasks.task_list tl on ut.task_id = tl.id
            WHERE u.id = ?;
            """;

    private final String FIND_BY_USERNAME = """
            SELECT u.id               as user_id,
                   u.name             as user_name,
                   u.username         as user_username,
                   u.password         as user_password,
                   ur.role            as user_role_role,
                   tl.id              as task_id,
                   tl.title           as task_title,
                   tl.description     as task_description,
                   tl.expiration_date as task_expiration_date,
                   tl.status          as task_status
            FROM tasks.user_list u
                     LEFT JOIN tasks.user_roles ur on u.id = ur.user_id
                     LEFT JOIN tasks.user_task ut on u.id = ut.user_id
                     LEFT JOIN tasks.task_list tl on ut.task_id = tl.id
            WHERE u.username = ?""";

    private final String UPDATE = """
            UPDATE tasks.user_list
            SET name     = ?,
                username = ?,
                password = ?
            WHERE id = ?;
            """;

    private final String CREATE = """
            INSERT INTO tasks.user_list (name, username, password)
            VALUES (?, ?, ?);
            """;

    private final String INSERT_USER_ROLE = """
            INSERT INTO tasks.user_roles(user_id, role)
            VALUES (?, ?);
            """;

    private final String DELETE = """
            DELETE
            FROM tasks.user_list
            WHERE id = ?;
            """;

    private final String IS_TASK_OWNER = """
            SELECT exists(SELECT 1
                          FROM tasks.user_task
                          WHERE user_id = ?
                            AND task_id = ?);
            """;


    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet set = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(set));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by id");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, username);
            try (ResultSet set = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(set));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating user");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet set = statement.getGeneratedKeys()) {
                set.next();
                user.setId(set.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating user");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while inserting role for user");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet set = statement.executeQuery()) {
                set.next();
                return set.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while checking if user is task owner");
        }
    }

    @Override
    public void delete(Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting user");
        }
    }
}