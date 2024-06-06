package com.iablonski.crm.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iablonski.crm.tasklist.web.dto.validation.OnCreate;
import com.iablonski.crm.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "User DTO")
public record UserDto(
        @Schema(description = "User Id", example = "1")
        @NotNull(message = "Id must be not null", groups = OnUpdate.class)
        Long id,
        @Schema(description = "User name", example = "John Doe")
        @NotNull(message = "Name must be not null",
                groups = {OnUpdate.class, OnCreate.class})
        @Length(max = 255, message = "Name must be smaller than 255 symbols",
                groups = {OnUpdate.class, OnCreate.class})
        String name,
        @Schema(description = "User username", example = "johndoe@gmail.com")
        @NotNull(message = "Username must be not null",
                groups = {OnUpdate.class, OnCreate.class})
        @Length(max = 255,
                message = "Username must be smaller than 255 symbols",
                groups = {OnUpdate.class, OnCreate.class})
        String username,
        @Schema(description = "User password", example = "12345")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Password must be not null",
                groups = {OnUpdate.class, OnCreate.class})
        String password,
        @Schema(description = "User password confirm", example = "12345")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Password confirmation must be not null",
                groups = OnCreate.class)
        String passwordConfirmation) {
}
