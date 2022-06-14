package com.manager.finance.model;

import com.manager.finance.dto.CrudDTO;
import com.manager.finance.entity.CrudEntity;
import com.manager.finance.repository.UserRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.List;

@Getter
public abstract class CrudModel<T extends CrudEntity, V extends CrudDTO> {
    private final ModelMapper mapper = new ModelMapper();
    @Autowired
    private UserRepository userRepository;

    public abstract List<T> getAll(Principal principal);

    public abstract CrudEntity create(V v, Principal principal);

    public abstract CrudEntity update(T t, V v);

    public abstract Void delete(T t);

}
