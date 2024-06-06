package com.iablonski.crm.tasklist.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iablonski.crm.tasklist.domain.task.Status;
import com.iablonski.crm.tasklist.web.dto.validation.OnCreate;
import com.iablonski.crm.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDto(
        @NotNull(message = "Id must be not null", groups = OnUpdate.class)
        Long id,
        @NotNull(message = "Title must be not null",
                groups = {OnUpdate.class, OnCreate.class})
        @Length(max = 255, message = "Title must be smaller than 255 symbols",
                groups = {OnUpdate.class, OnCreate.class})
        String title,
        @Length(max = 255,
                message = "Description must be smaller than 255 symbols",
                groups = {OnUpdate.class, OnCreate.class})
        String description,
        Status status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime expirationTime,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<String> images
) {
}
