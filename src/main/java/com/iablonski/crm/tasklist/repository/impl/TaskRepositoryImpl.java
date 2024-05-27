package com.iablonski.crm.tasklist.repository.impl;

import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.repository.DataSourceConfig;
import com.iablonski.crm.tasklist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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
            WHERE id = ?;
            """;

    private final String FIND_ALL_USER_ID = """
            SELECT t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM tasks.task_list t
                     JOIN tasks.user_task ut ON t.id = ut.task_id
            WHERE ut.user_id = ?;
                        """;

    private final String ASSIGN = """
            INSERT INTO tasks.user_task(task_id, user_id)
            VALUES (?, ?);
                        """;

    private final String UPDATE = """
            UPDATE tasks.task_list
            SET title           = ?,
                description     = ?,
                expiration_date = ?,
                status          = ?
            WHERE id = ?;
                        """;

    private final String CREATE = """
            INSERT INTO tasks.task_list(title, description, status, expiration_date)
            VALUES (?, ?, ?, ?);
                        """;

    private final String DELETE = """
            DELETE
            FROM tasks.task_list
            WHERE id = ?;
                        """;

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return null;
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void create(Task task) {

    }

    @Override
    public void delete(Long id) {

    }
}
