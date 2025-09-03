package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.user.entity.PermissionEntity;
import com.manager.user.entity.RoleEntity;
import com.manager.finance.helper.converter.RoleIdConverter;
import com.manager.finance.helper.prepare.RolePrepareHelper;
import com.manager.user.repository.RoleRepository;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({RoleIdConverter.class, RolePrepareHelper.class})
@ActiveProfiles("test")
class PermissionsRoleTest {
    private static final String PERMISSION_API = "/v1/admin/role/{id}/permission";
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RolePrepareHelper rolePrepareHelper;
    private RoleEntity role;

    @BeforeEach
    private void prepare() {
        role = rolePrepareHelper.createRole();
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void addPermissions_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(PERMISSION_API, role.getId())
                        .param("permissionIds", PermissionEntity.ALL_READ.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void deletePermissions_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PERMISSION_API, role.getId())
                        .param("permissionIds", PermissionEntity.ALL_READ.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.permissions").isEmpty());
    }
}