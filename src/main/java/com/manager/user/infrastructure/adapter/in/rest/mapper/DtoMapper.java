package com.manager.user.infrastructure.adapter.in.rest.mapper;

public interface DtoMapper<R, V, M> {

    M toModel(R dto);

    V toResponseDto(M dto);
}
