package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.entity.Order;
import org.springframework.data.domain.Page;

public abstract class PagedOrdersResponseMapperDecorator extends PagedResponseMapperDecorator<OrderDto, Order, OrderMapper> implements PagedOrdersResponseMapper {

    @Override
    public PagedResponse<OrderDto> pageToPagedResponse(Page<Order> page, OrderMapper contentMapper) {
        return super.pageToPagedResponse(page, contentMapper);
    }
}
