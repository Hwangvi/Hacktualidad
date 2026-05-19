package com.Hacktualidad.controller;

import com.Hacktualidad.dto.CartDTO;
import com.Hacktualidad.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartRESTController.class)
class CartRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser(username = "user@test.com")
    void shouldGetMyCart() throws Exception {
        when(cartService.getMyCart("user@test.com")).thenReturn(new CartDTO());

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());

        verify(cartService).getMyCart("user@test.com");
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void shouldAddToCart() throws Exception {
        when(cartService.addProductToCart("user@test.com", 10L)).thenReturn(new CartDTO());

        mockMvc.perform(post("/api/cart/add/10").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void shouldCheckout() throws Exception {
        mockMvc.perform(post("/api/cart/checkout").with(csrf()))
                .andExpect(status().isOk());

        verify(cartService).checkout("user@test.com");
    }
}