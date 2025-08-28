package com.manager.finance.service;

import com.manager.finance.dto.CrudDTO;
import com.manager.finance.dto.response.CrudResponseDTO;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CrudService<D extends CrudDTO, R extends CrudResponseDTO> {

    R get(UUID id, Principal principal);

    List<R> getAll(Principal principal);

    R create(Principal principal, D dto);

    R update(UUID id, Principal principal, D dto);

    void delete(UUID id, Principal principal);
}
