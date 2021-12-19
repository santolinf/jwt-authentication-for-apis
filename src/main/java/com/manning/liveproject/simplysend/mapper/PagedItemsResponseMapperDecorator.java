package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.entity.Item;
import org.springframework.data.domain.Page;

public abstract class PagedItemsResponseMapperDecorator extends PagedResponseMapperDecorator<ItemDto, Item, ItemMapper> implements PagedItemsResponseMapper {

    @Override
    public PagedResponse<ItemDto> pageToPagedResponse(Page<Item> page, ItemMapper contentMapper) {
        return super.pageToPagedResponse(page, contentMapper);
    }
}
