package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.entity.Item;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(PagedItemsResponseMapperDecorator.class)
public interface PagedItemsResponseMapper extends PagedResponseMapper<ItemDto, Item, ItemMapper> {
}
