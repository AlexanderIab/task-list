package com.iablonski.crm.tasklist.service;

import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.domain.task.TaskImage;

import java.util.List;

public interface TaskService {
    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    Task create(Long userId, Task task);

    void delete(Long id);

    void uploadImage(Long id, TaskImage image);
}
