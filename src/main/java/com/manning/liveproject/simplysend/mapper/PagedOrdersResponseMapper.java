package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.entity.Order;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(PagedOrdersResponseMapperDecorator.class)
public interface PagedOrdersResponseMapper extends PagedResponseMapper<OrderDto, Order, OrderMapper> {
}
