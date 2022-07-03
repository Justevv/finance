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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({RoleIdConverter.class, PermissionIdConverter.class, RolePrepareHelper.class})
@ActiveProfiles("test")
class RoleTest {
    private static final String ROLE_API = "/v1/admin/role";
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
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getRoles_shouldReturnRoleEntitiesAndOk_when_roleIsExists() {
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_API))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].name").value(role.getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getRole_shouldReturnRoleEntityAndOk_when_roleIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_API + "/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()));
    }


    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void createRole_shouldReturnRoleEntityAndOk_when_roleIsOk() {
        Mockito.when(permissionRepository.findById(permission.getId())).thenReturn(Optional.of(permission));
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_API)
                        .param("name", role.getName())
                        .param("permissions", String.valueOf(permission.getId())))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.permissions.[0].name").value(permission.getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void createRole_shouldReturnExceptionAndBadResponse_when_roleIsInvalid() {
        Mockito.when(permissionRepository.findById(permission.getId())).thenReturn(Optional.of(permission));
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_API)
                        .param("name", role.getName())
                        .param("permissions", "10"))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void updateRole_shouldReturnRoleEntityAndOk_when_roleIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_API + "/1")
                        .param("name", role.getName()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void updateRole_shouldReturnExceptionAndBadResponse_when_roleIsInvalid() {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_API + "/1")
                        .param("name", role.getName())
                        .param("permissions", "10"))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void deleteRole_shouldReturnNullAndOk_when_roleIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_API + "/1"))
                .andExpect(status().is(200));
    }
}
