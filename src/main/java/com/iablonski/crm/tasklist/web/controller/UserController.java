package com.iablonski.crm.tasklist.web.controller;

import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.domain.user.User;
import com.iablonski.crm.tasklist.service.TaskService;
import com.iablonski.crm.tasklist.service.UserService;
import com.iablonski.crm.tasklist.web.dto.task.TaskDto;
import com.iablonski.crm.tasklist.web.dto.user.UserDto;
import com.iablonski.crm.tasklist.web.dto.validation.OnCreate;
import com.iablonski.crm.tasklist.web.dto.validation.OnUpdate;
import com.iablonski.crm.tasklist.web.mappers.TaskMapper;
import com.iablonski.crm.tasklist.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable("id") Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    public TaskDto createTask(@PathVariable("id") Long id, @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(id, task);
        return taskMapper.toDto(createdTask);
    }
}