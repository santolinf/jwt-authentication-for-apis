package com.manning.liveproject.simplysend.api.dto;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@Data
public final class PagedResponse<T> implements Iterable<T> {

    private List<T> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;

    private Integer firstPageNumber;
    private Integer previousPageNumber;
    private Integer nextPageNumber;
    private Integer lastPageNumber;

    /**
     * Because we are mapping with specialised decorator class an instance of {@link org.springframework.data.domain.Page},
     * which is an iterable type, we must make this class, the target, an {@link Iterable} too.
     * We return <em>null</em> in all cases, as the iterator will not be used.
     *
     * @return null
     */
    @Override
    @Nullable
    public Iterator<T> iterator() {
        return null;
    }
}
