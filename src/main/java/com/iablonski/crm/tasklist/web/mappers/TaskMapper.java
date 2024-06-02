package com.iablonski.crm.tasklist.web.mappers;

import com.iablonski.crm.tasklist.domain.task.Task;
import com.iablonski.crm.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
