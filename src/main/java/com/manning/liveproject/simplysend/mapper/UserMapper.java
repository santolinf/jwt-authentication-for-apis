package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.User;
import com.manning.liveproject.simplysend.entity.UserAccount;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

    @Mapping(source = "emailId", target = "username")
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserAccount userDtoToUserAccount(UserDto user);

    @Mapping(source = "emailId", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "manager", ignore = true)
    User userDtoToUser(UserDto user);
}
