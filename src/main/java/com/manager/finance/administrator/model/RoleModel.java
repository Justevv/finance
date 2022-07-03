package com.manager.finance.administrator.model;


import com.manager.finance.administrator.dto.RoleDTO;
import com.manager.finance.entity.RoleEntity;
import com.manager.finance.exception.UserAlreadyExistException;
import com.manager.finance.helper.UserHelper;
import com.manager.finance.log.CrudLogConstants;
import com.manager.finance.repository.PermissionRepository;
import com.manager.finance.repository.RoleRepository;
import com.manager.finance.service.ConfirmationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class RoleModel {
    private static final String ROLE_LOG_NAME = "role";
    private final CrudLogConstants crudLogConstants = new CrudLogConstants(ROLE_LOG_NAME);
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    public List<RoleEntity> getAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public RoleEntity create(RoleDTO roleDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputNewDTO(), roleDTO);
        var role = mapper.map(roleDTO, RoleEntity.class);
        roleRepository.save(role);
        log.info(crudLogConstants.getSaveEntityToDatabase(), role);
        return role;
    }

    @Transactional
    public RoleEntity update(RoleEntity role, RoleDTO roleDTO) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputDTOToChangeEntity(), roleDTO, role);
        mapper.map(roleDTO, role);
        roleRepository.save(role);
        log.info(crudLogConstants.getUpdateEntityToDatabase(), role);
        return role;
    }

    @Transactional
    public Void delete(RoleEntity role) throws UserAlreadyExistException {
        log.debug(crudLogConstants.getInputEntityForDelete(), role);
        roleRepository.delete(role);
        log.info(crudLogConstants.getDeleteEntityFromDatabase());
        return null;
    }

    public RoleEntity addPermission(RoleEntity role, List<Long> permissionIds) {
        log.debug("For role {} will be add the permissions with id: {}", role, permissionIds);
        var permissionEntities = permissionRepository.findAllById(permissionIds);
        log.debug("For role {} will be add the permissions: {}", role, permissionEntities);
        role.getPermissions().addAll(permissionEntities);
        roleRepository.save(role);
        return role;
    }

    public RoleEntity deletePermission(RoleEntity role, List<Long> permissionIds) {
        log.debug("For role {} will be remove the permissions with id: {}", role, permissionIds);
        var permissionEntities = permissionRepository.findAllById(permissionIds);
        log.debug("For role {} will be remove the permissions: {}", role, permissionEntities);
        permissionEntities.forEach(role.getPermissions()::remove);
        roleRepository.save(role);
        return role;
    }

}

