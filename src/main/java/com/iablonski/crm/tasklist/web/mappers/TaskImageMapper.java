package com.iablonski.crm.tasklist.web.mappers;

import com.iablonski.crm.tasklist.domain.task.TaskImage;
import com.iablonski.crm.tasklist.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}

