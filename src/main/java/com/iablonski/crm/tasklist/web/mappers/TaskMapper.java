package com.iablonski.crm.tasklist.web.mappers;

import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> tasks);
    Task toEntity(TaskDto taskDto);
}
