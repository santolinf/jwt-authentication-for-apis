package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.User;
import com.manning.liveproject.simplysend.entity.UserAccount;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper extends PageableContentMapper<UserDto, User> {

    @Mapping(target = "username", source = "emailId")
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserAccount userDtoToUserAccount(UserDto user);

    @Mapping(target = "email", source = "emailId")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "manager", ignore = true)
    User userDtoToUser(UserDto user);

    @Mapping(target = "emailId", source = "email")
    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "password", ignore = true)
    @Override
    UserDto entityToDto(User entity);
}
