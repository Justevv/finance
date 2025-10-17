package com.manager.user.domain.service.admin;


import com.manager.user.application.port.out.repository.RoleRepository;
import com.manager.user.domain.model.RoleModel;
import com.manager.user.infrastructure.adapter.in.rest.dto.request.OldRoleDTO;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.manager.user.domain.exception.UserAlreadyExistException;
import com.manager.finance.log.CrudLogConstants;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.RoleSpringDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {
    private static final String ROLE_LOG_NAME = "role";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(ROLE_LOG_NAME);
    private final ModelMapper oldMapper;
    private final RoleSpringDataRepository roleSpringDataRepository;
    private final RoleRepository roleRepository;

    public List<RoleModel> getAll() {
        return roleRepository.findAll();
    }

    public RoleModel get(UUID id) {
        return roleRepository.getById(id);
    }

    @Transactional
    public RoleModel create(RoleModel model) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputNewDTO(), model);
        RoleModel save = RoleModel.builder()
                .id(UUID.randomUUID())
                .name(model.name())
                .permissions(model.permissions())
                .build();
        RoleModel saved = roleRepository.save(save);
        log.info(crudLogConstants.getSaveEntityToDatabase(), model);
        return saved;
    }

    @Transactional
    public RoleEntity update(RoleEntity role, OldRoleDTO roleDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), roleDTO, role);
        oldMapper.map(roleDTO, role);
        roleSpringDataRepository.save(role);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), role);
        return role;
    }

    @Transactional
    public Void delete(RoleEntity role) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputEntityForDelete(), role);
        roleSpringDataRepository.delete(role);
        log.info(crudLogConstants.getDeleteEntityFromDatabase());
        return null;
    }

    public RoleEntity addPermission(RoleEntity role, List<String> permissionIds) {
        log.debug("For role {} will be add the permissions with id: {}", role, permissionIds);
        var permissionEntities = permissionIds.stream().map(PermissionEntity::valueOf).toList();
        log.debug("For role {} will be add the permissions: {}", role, permissionEntities);
        role.getPermissions().addAll(permissionEntities);
        roleSpringDataRepository.save(role);
        return role;
    }

    public RoleEntity deletePermission(RoleEntity role, List<String> permissionIds) {
        log.debug("For role {} will be remove the permissions with id: {}", role, permissionIds);
        var permissionEntities = permissionIds.stream().map(PermissionEntity::valueOf).toList();
        log.debug("For role {} will be remove the permissions: {}", role, permissionEntities);
        permissionEntities.forEach(role.getPermissions()::remove);
        roleSpringDataRepository.save(role);
        return role;
    }

}

