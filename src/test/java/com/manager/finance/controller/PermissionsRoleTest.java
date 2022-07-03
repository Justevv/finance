package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.entity.RoleEntity;
import com.manager.finance.helper.converter.PermissionIdConverter;
import com.manager.finance.helper.converter.RoleIdConverter;
import com.manager.finance.helper.prepare.RolePrepareHelper;
import com.manager.finance.repository.PermissionRepository;
import com.manager.finance.repository.RoleRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({PermissionIdConverter.class, RoleIdConverter.class, RolePrepareHelper.class})
@ActiveProfiles("test")
class PermissionsRoleTest {
    private static final String PERMISSION_API = "/v1/admin/role/{id}/permission";
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PermissionRepository permissionRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RolePrepareHelper rolePrepareHelper;
    private RoleEntity role;
    private PermissionEntity permission;

    @BeforeEach
    private void prepare() {
        role = rolePrepareHelper.createRole();
        permission = role.getPermissions().iterator().next();
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
        Mockito.when(permissionRepository.findAllById(List.of(permission.getId()))).thenReturn(List.of(permission));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void addPermissions_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(PERMISSION_API, role.getId())
                        .param("permissionIds", String.valueOf(permission.getId())))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.permissions.[0].name").value(role.getPermissions().iterator().next().getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void deletePermissions_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PERMISSION_API, role.getId())
                        .param("permissionIds", String.valueOf(permission.getId())))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.permissions").isEmpty());
    }
}
