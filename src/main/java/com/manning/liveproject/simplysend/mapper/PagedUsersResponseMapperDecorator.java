package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.User;
import org.springframework.data.domain.Page;

public class PagedUsersResponseMapperDecorator extends PagedResponseMapperDecorator<UserDto, User, UserMapper> implements PagedUsersResponseMapper {

    @Override
    public PagedResponse<UserDto> pageToPagedResponse(Page<User> page, UserMapper contentMapper) {
        return super.pageToPagedResponse(page, contentMapper);
    }
}
