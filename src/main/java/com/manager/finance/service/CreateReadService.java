package com.manager.finance.service;

import com.manager.finance.dto.CrudDTO;
import com.manager.finance.dto.response.CrudResponseDTO;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CreateReadService<D extends CrudDTO, R extends CrudResponseDTO> {

    R get(UUID guid, Principal principal);

    List<R> getAll(Principal principal);

    R create(Principal principal, D dto);

}
