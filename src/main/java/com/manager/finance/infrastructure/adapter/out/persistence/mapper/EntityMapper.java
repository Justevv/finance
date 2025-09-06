package com.manager.finance.infrastructure.adapter.out.persistence.mapper;

public interface EntityMapper<E, M> {

    M toModel(E entity);

    E toEntity(M model);
}
