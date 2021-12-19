package com.manning.liveproject.simplysend.mapper;

public interface PageableContentMapper<T, E> {

    T entityToDto(E entity);
}
