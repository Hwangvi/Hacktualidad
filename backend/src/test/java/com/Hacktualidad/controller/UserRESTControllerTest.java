package com.Hacktualidad.controller;

import com.Hacktualidad.dto.LoginRequestDTO;
import com.Hacktualidad.dto.UserResponseDTO;
import com.Hacktualidad.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRESTController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequestDTO loginReq = new LoginRequestDTO();
        loginReq.setEmail("test@test.com");
        loginReq.setPassword("1234");

        UserResponseDTO userRes = new UserResponseDTO();

        when(userService.loginUser(any(LoginRequestDTO.class))).thenReturn(Optional.of(userRes));

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailLogin() throws Exception {
        LoginRequestDTO loginReq = new LoginRequestDTO();
        loginReq.setEmail("fail@test.com");
        loginReq.setPassword("wrong");

        when(userService.loginUser(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email o contraseña incorrectos"));
    }
}