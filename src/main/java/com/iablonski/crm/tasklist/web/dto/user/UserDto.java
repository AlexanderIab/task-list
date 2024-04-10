package com.iablonski.crm.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iablonski.crm.tasklist.web.dto.validation.OnCreate;
import com.iablonski.crm.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserDto(
        @NotNull(message = "Id must be not null", groups = OnUpdate.class)
        Long id,
        @NotNull(message = "Name must be not null", groups = {OnUpdate.class, OnCreate.class})
        @Length(max = 255, message = "Name must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
        String name,
        @NotNull(message = "Username must be not null", groups = {OnUpdate.class, OnCreate.class})
        @Length(max = 255, message = "Username must be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
        String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Password must be not null", groups = {OnUpdate.class, OnCreate.class})
        String password,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Password confirmation must be not null", groups = OnCreate.class)
        String passwordConfirmation) {
}
