package com.iablonski.crm.tasklist.web.dto.task;

import com.iablonski.crm.tasklist.domain.task.Status;

import java.time.LocalDateTime;

public record TaskDto(Long id,
                      String title,
                      String description,
                      Status status,
                      LocalDateTime expirationTime) {
}
