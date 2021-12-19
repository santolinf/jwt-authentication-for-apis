package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

public interface PagedResponseMapper<T, E, V extends PageableContentMapper<T, E>> {

    @Mapping(target = "pageNumber", ignore = true)
    @Mapping(target = "pageSize", ignore = true)
    @Mapping(target = "firstPageNumber", ignore = true)
    @Mapping(target = "previousPageNumber", ignore = true)
    @Mapping(target = "nextPageNumber", ignore = true)
    @Mapping(target = "lastPageNumber", ignore = true)
    PagedResponse<T> pageToPagedResponse(Page<E> page, V contentMapper);
}
