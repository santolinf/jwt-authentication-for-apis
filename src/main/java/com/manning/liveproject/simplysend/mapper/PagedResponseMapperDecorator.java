package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;

public abstract class PagedResponseMapperDecorator<T, E, V extends PageableContentMapper<T, E>> implements PagedResponseMapper<T, E, V> {

    @Override
    public PagedResponse<T> pageToPagedResponse(Page<E> page, V contentMapper) {
        PagedResponse<T> pagedResponse = new PagedResponse<>();

        pagedResponse.setContent(
                page.getContent().stream()
                        .map(contentMapper::entityToDto)
                        .collect(Collectors.toList())
        );

        pagedResponse.setPageNumber(page.getNumber());
        pagedResponse.setPageSize(page.getSize());
        pagedResponse.setTotalElements(page.getTotalElements());
        pagedResponse.setTotalPages(page.getTotalPages());

        if (!page.isFirst()) {
            Pageable firstPage = page.getPageable().first();
            pagedResponse.setFirstPageNumber(firstPage.getPageNumber());
        }

        if (page.hasPrevious()) {
            Pageable previousPage = page.previousPageable();
            pagedResponse.setPreviousPageNumber(previousPage.getPageNumber());
        }

        if (page.hasNext()) {
            Pageable nextPage = page.nextPageable();
            pagedResponse.setNextPageNumber(nextPage.getPageNumber());
        }

        if (!page.isLast()) {
            pagedResponse.setLastPageNumber(page.getTotalPages() - 1);
        }

        return pagedResponse;
    }
}
