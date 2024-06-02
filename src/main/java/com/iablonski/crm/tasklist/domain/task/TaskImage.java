package com.iablonski.crm.tasklist.domain.task;

import org.springframework.web.multipart.MultipartFile;

public record TaskImage(MultipartFile file) {
}
