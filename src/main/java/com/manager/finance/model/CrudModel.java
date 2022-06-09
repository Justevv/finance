package com.manager.finance.model;

import com.manager.finance.entity.CrudEntity;
import lombok.Getter;
import org.modelmapper.ModelMapper;


public abstract class CrudModel<T, V> {
    @Getter
    private final ModelMapper mapper = new ModelMapper();

    public abstract CrudEntity create(V v);

    public abstract CrudEntity update(T t, V v);

    public abstract Void delete(T t);

}
