package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.api.enums.ItemType;
import com.manning.liveproject.simplysend.entity.Item;
import com.manning.liveproject.simplysend.mapper.ItemMapper;
import com.manning.liveproject.simplysend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemRepository itemRepository;
    private final ItemMapper mapper;

    public List<ItemDto> findItems(ItemType type, Integer limit) {
        log.debug("Find items: type=[{}], limit=[{}]", type, limit);

        String typeAsText = ofNullable(type).map(ItemType::getValue).orElse("%");
        Pageable pageRequest = PageRequest.of(0, ofNullable(limit).orElse(100));

        Page<Item> page = itemRepository.findItemsByTypeIsLike(typeAsText, pageRequest);
        int totalPages = page.getTotalPages();
        int pageNumber = totalPages == 0 ? page.getNumber() : page.getNumber() + 1;
        log.debug("Loaded {} of {} items (page {} of {})", page.getNumberOfElements(), page.getTotalElements(), pageNumber, totalPages);

        return page.getContent().stream()
                .map(mapper::itemToItemDto)
                .collect(Collectors.toList());
    }
}
