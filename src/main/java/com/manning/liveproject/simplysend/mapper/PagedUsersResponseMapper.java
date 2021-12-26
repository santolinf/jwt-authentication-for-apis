package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(PagedUsersResponseMapperDecorator.class)
public interface PagedUsersResponseMapper extends PagedResponseMapper<UserDto, User, UserMapper> {
}
