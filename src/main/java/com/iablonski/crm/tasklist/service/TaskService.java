package com.iablonski.crm.tasklist.service;

import com.iablonski.crm.tasklist.domain.task.Task;

import java.util.List;

public interface TaskService {
    Task getById(Long id);
    List<Task> getAllByUserId(Long userId);
    void update(Task task);
    void create(Task task);
    void delete(Long id);
}
