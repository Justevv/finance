package com.manager.finance.model;

import com.manager.finance.dto.CrudDTO;
import com.manager.finance.dto.response.CrudResponseDTO;
import com.manager.finance.entity.CrudEntity;

import java.security.Principal;
import java.util.List;

public interface CrudModel<E extends CrudEntity, D extends CrudDTO, R extends CrudResponseDTO> {

    R get(E entity);

    List<R> getAll(Principal principal);

    R create(Principal principal, D dto);

    R update(E entity, D dto);

    Void delete(E entity);

    String getEntityTypeName();

}
