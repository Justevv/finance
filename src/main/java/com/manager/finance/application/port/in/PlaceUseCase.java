package com.manager.finance.application.port.in;

import com.manager.finance.domain.model.PlaceModel;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface PlaceUseCase {

    List<PlaceModel> getAll(Principal principal);

    PlaceModel get(UUID uuid, Principal principal);

    PlaceModel create(Principal principal, PlaceModel model);
}
