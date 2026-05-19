package com.Hacktualidad.controller;

import com.Hacktualidad.dto.CategoryCreateUpdateDTO;
import com.Hacktualidad.dto.CategoryResponseDTO;
import com.Hacktualidad.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryRESTController.class)
class CategoryRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldGetAllCategories() throws Exception {
        when(categoryService.findAllCategory()).thenReturn(Arrays.asList(new CategoryResponseDTO(), new CategoryResponseDTO()));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void shouldCreateCategory() throws Exception {
        CategoryCreateUpdateDTO request = new CategoryCreateUpdateDTO();
        request.setName("Nueva Categoria");
        request.setDescription("Descripcion de prueba");

        CategoryResponseDTO response = new CategoryResponseDTO();

        when(categoryService.createCategory(any(CategoryCreateUpdateDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        when(categoryService.findCategoryById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound());
    }
}