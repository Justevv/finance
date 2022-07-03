package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.entity.PermissionEntity;
import com.manager.finance.helper.converter.PermissionIdConverter;
import com.manager.finance.helper.prepare.PermissionPrepareHelper;
import com.manager.finance.repository.PermissionRepository;
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
@Import({PermissionIdConverter.class, PermissionPrepareHelper.class})
@ActiveProfiles("test")
class PermissionTest {
    private static final String PERMISSION_API = "/v1/admin/permission";
    @MockBean
    private PermissionRepository permissionRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PermissionPrepareHelper permissionPrepareHelper;
    private PermissionEntity permission;

    @BeforeEach
    private void prepare() {
        permission = permissionPrepareHelper.createPermission();
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getPermissions_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        Mockito.when(permissionRepository.findAll()).thenReturn(List.of(permission));
        mockMvc.perform(MockMvcRequestBuilders.get(PERMISSION_API))
                .andExpect(status().is(200))
                .andExpect(jsonPath("[0].name").value(permission.getName()));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getPermission_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        Mockito.when(permissionRepository.findById(permission.getId())).thenReturn(Optional.ofNullable(permission));
        mockMvc.perform(MockMvcRequestBuilders.get(PERMISSION_API + "/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(permission.getName()));
    }
}
