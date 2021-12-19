package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.api.enums.ItemType;
import com.manning.liveproject.simplysend.entity.Item;
import com.manning.liveproject.simplysend.mapper.ItemMapper;
import com.manning.liveproject.simplysend.mapper.PagedItemsResponseMapper;
import com.manning.liveproject.simplysend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemRepository itemRepository;
    private final PagedItemsResponseMapper pagedResponseMapper;
    private final ItemMapper itemMapper;

    public PagedResponse<ItemDto> findItems(ItemType type, Integer limit, Integer page) {
        log.debug("Find items: type=[{}], limit=[{}], page=[{}]", type, limit, page);

        String typeAsText = ofNullable(type).map(ItemType::getValue).orElse("%");
        Pageable pageRequest = PageRequest.of(ofNullable(page).orElse(0), ofNullable(limit).orElse(100));

        Page<Item> items = itemRepository.findItemsByTypeIsLike(typeAsText, pageRequest);
        int totalPages = items.getTotalPages();
        int pageNumber = totalPages == 0 ? items.getNumber() : items.getNumber() + 1;
        log.debug("Loaded {} of {} items (page {} of {})", items.getNumberOfElements(), items.getTotalElements(), pageNumber, totalPages);

        return pagedResponseMapper.pageToPagedResponse(items, itemMapper);
    }
}
