package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.user.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.manager.user.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.manager.finance.helper.converter.RoleIdConverter;
import com.manager.finance.helper.prepare.RolePrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.RoleSpringDataRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({RoleIdConverter.class, RolePrepareHelper.class})
@ActiveProfiles("test")
class RoleTest {
    private static final String ROLE_API = "/v1/admin/role";
    @MockitoBean
    private RoleSpringDataRepository roleRepository;
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
    void getRoles_shouldReturnRoleEntitiesAndOk_when_roleIsExists() {
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_API))
                .andExpect(status().is(200))
                .andExpect(jsonPath("payload.[0].name").value(role.getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getRole_shouldReturnRoleEntityAndOk_when_roleIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_API + "/" + role.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()));
    }


    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void createRole_shouldReturnRoleEntityAndOk_when_roleIsOk() {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_API)
                        .param("name", role.getName())
                        .param("permissions", PermissionEntity.ALL_READ.toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.permissions").value(PermissionEntity.ALL_READ.toString()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void createRole_shouldReturnExceptionAndBadResponse_when_roleIsInvalid() {
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
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_API + "/" + role.getId())
                        .param("name", role.getName()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(role.getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void updateRole_shouldReturnExceptionAndBadResponse_when_roleIsInvalid() {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_API + "/" + role.getId())
                        .param("name", role.getName())
                        .param("permissions", "10"))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void deleteRole_shouldReturnNullAndOk_when_roleIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_API + "/" + role.getId()))
                .andExpect(status().is(200));
    }
}