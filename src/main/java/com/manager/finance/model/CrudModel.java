package com.manager.finance.model;

import com.manager.finance.entity.CrudEntity;
import com.manager.finance.repo.UserRepo;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

@Getter
public abstract class CrudModel<T, V> {
    private final ModelMapper mapper = new ModelMapper();
    @Autowired
    private UserRepo userRepo;

    public abstract CrudEntity create(V v, Principal principal);

    public abstract CrudEntity update(T t, V v);

    public abstract Void delete(T t);

}
