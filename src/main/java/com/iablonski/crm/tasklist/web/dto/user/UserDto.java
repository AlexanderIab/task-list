package com.iablonski.crm.tasklist.web.dto.user;

public record UserDto(Long id,
                      String name,
                      String username,
                      String password,
                      String passwordConfirmation) {
}
