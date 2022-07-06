package com.manager.finance.controller;

import com.manager.Manager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PermissionTest {
    private static final String PERMISSION_API = "/v1/admin/permission";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getPermissions_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.get(PERMISSION_API))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(authorities = {"role:crud"})
    @SneakyThrows
    void getPermission_shouldReturnPermissionEntityAndOk_when_permissionIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.get(PERMISSION_API + "/ALL_READ"))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"));
    }
}