package com.manager.finance.model;

import com.manager.finance.entity.CrudEntity;

public interface CrudModel<T, V> {
    CrudEntity create(V v);

    CrudEntity update(T t, V v);

    Void delete(T t);

}
