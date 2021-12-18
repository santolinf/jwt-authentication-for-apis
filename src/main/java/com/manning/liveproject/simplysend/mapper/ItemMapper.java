package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto itemToItemDto(Item item);
}
