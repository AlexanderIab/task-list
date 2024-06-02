package com.iablonski.crm.tasklist.web.mappers;

import com.iablonski.crm.tasklist.domain.user.User;
import com.iablonski.crm.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
