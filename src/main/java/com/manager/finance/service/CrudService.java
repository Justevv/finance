package com.manager.finance.service;

import com.manager.finance.dto.CrudDTO;
import com.manager.finance.dto.response.CrudResponseDTO;
import com.manager.finance.entity.CrudEntity;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CrudService<E extends CrudEntity, D extends CrudDTO, R extends CrudResponseDTO> {

    R get(E entity);

    R get(UUID guid, Principal principal);

    List<R> getAll(Principal principal);

    R create(Principal principal, D dto);

    R update(E entity, D dto);

    Void delete(E entity);

}
