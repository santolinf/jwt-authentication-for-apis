package com.manning.liveproject.simplysend.api.dto;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@ApiModel("pagedResponse")
@Data
public final class PagedResponse<T> implements Iterable<T> {

    @JsonUnwrapped
    @JsonValue
    private List<T> content;
    @JsonIgnore
    private Integer pageNumber;
    @JsonIgnore
    private Integer pageSize;
    @JsonIgnore
    private Long totalElements;
    @JsonIgnore
    private Integer totalPages;

    @JsonIgnore
    private Integer firstPageNumber;
    @JsonIgnore
    private Integer previousPageNumber;
    @JsonIgnore
    private Integer nextPageNumber;
    @JsonIgnore
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
