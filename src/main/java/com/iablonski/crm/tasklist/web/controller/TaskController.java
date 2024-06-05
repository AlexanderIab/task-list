package com.iablonski.crm.tasklist.web.controller;

import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.domain.task.TaskImage;
import com.iablonski.crm.tasklist.service.TaskService;
import com.iablonski.crm.tasklist.web.dto.task.TaskDto;
import com.iablonski.crm.tasklist.web.dto.task.TaskImageDto;
import com.iablonski.crm.tasklist.web.dto.validation.OnUpdate;
import com.iablonski.crm.tasklist.web.mappers.TaskImageMapper;
import com.iablonski.crm.tasklist.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @PutMapping
    @Operation(summary = "Update task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#taskDto.id())")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by Id")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getById(@PathVariable("id") Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by Id")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteById(@PathVariable("id") Long id) {
        taskService.delete(id);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id, @Validated @ModelAttribute TaskImageDto imageDto) {
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);
    }

}