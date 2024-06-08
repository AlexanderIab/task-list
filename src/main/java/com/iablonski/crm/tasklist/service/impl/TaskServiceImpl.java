package com.iablonski.crm.tasklist.service.impl;

import com.iablonski.crm.tasklist.domain.exception.ResourceNotFoundException;
import com.iablonski.crm.tasklist.domain.task.Status;
import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.domain.task.TaskImage;
import com.iablonski.crm.tasklist.repository.TaskRepository;
import com.iablonski.crm.tasklist.service.ImageService;
import com.iablonski.crm.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(final Long id) {
        return taskRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(final Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(final Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @Cacheable(
            value = "TaskService::getById",
            condition = "#task.id!=null",
            key = "#task.id")
    public Task create(final Long userId, final Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(final Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void uploadImage(final Long id, final TaskImage image) {
        Task task = getById(id);
        String filename = imageService.upload(image);
        task.getImages().add(filename);
        taskRepository.save(task);
    }
}
