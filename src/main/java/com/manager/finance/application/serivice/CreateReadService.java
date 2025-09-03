package com.manager.finance.application.serivice;

import com.manager.finance.infrastructure.controller.dto.request.CrudDTO;
import com.manager.finance.infrastructure.controller.dto.response.CrudResponseDTO;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CreateReadService<D extends CrudDTO, R extends CrudResponseDTO> {

    R get(UUID id, Principal principal);

    List<R> getAll(Principal principal);

    R create(Principal principal, D dto);

}
