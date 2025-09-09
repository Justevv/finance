package com.manager.finance.controller;

import com.manager.Manager;
import com.manager.finance.helper.prepare.PasswordResetTokenPrepareHelper;
import com.manager.finance.helper.prepare.UserPrepareHelper;
import com.manager.user.infrastructure.adapter.out.persistence.repository.PasswordResetTokenRepository;
import com.manager.user.infrastructure.adapter.out.persistence.repository.springdata.UserSpringDataRepository;
import com.manager.user.domain.service.SecurityUserService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Manager.class)
@AutoConfigureMockMvc
@Import({UserPrepareHelper.class, PasswordResetTokenPrepareHelper.class})
class PasswordControllerTest {
    private final static String RESET_PASSWORD_API = "/v1/user/password/reset";
    private final static String FORGET_PASSWORD_API = "/v1/user/password/forget";
    @MockBean
    private UserSpringDataRepository userRepository;
    @MockBean
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @MockBean
    private SecurityUserService securityUserService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserPrepareHelper userPrepareHelper;
    @Autowired
    private PasswordResetTokenPrepareHelper passwordResetTokenPrepareHelper;

    @BeforeEach
    void prepare() {
        var userEntity = userPrepareHelper.createUser();
        var passwordResetToken = passwordResetTokenPrepareHelper.createPasswordResetToken();
        Mockito.when(userRepository.findByEmailAndIsEmailConfirmed(userEntity.getEmail(), true)).thenReturn(Optional.of(userEntity));
        Mockito.when(passwordResetTokenRepository.findByToken(passwordResetToken.getToken())).thenReturn(Optional.of(passwordResetToken));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void forgetPassword_shouldReturnOkAndTrue_whenEmailIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(FORGET_PASSWORD_API)
                        .param("email", "email@email.ru")
                )
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.['Token created']").value(true));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void resetPassword_shouldReturnOkAndTrue_whenTokenIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(RESET_PASSWORD_API)
                        .param("password", "1")
                        .param("token", "token")
                )
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.['Password updated']").value(true));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void forgetPassword_shouldReturn400AndFalse_whenEmailIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(FORGET_PASSWORD_API)
                        .param("email", "email")
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void resetPassword_shouldReturn400AndFalse_whenTokenIsExists() {
        mockMvc.perform(MockMvcRequestBuilders.post(RESET_PASSWORD_API)
                        .param("token", "token")
                )
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"));
    }

}