package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import org.springframework.data.domain.Page;

public interface PagedResponseMapper<T, E, V extends PageableContentMapper<T, E>> {

    PagedResponse<T> pageToPagedResponse(Page<E> page, V contentMapper);
}
