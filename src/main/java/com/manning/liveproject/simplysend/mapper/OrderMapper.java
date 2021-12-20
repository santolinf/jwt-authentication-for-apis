package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "owner", ignore = true)
    Order orderDtoToOrder(OrderDto order);
}
