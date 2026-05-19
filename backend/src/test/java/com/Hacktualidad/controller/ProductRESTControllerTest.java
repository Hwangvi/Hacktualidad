package com.Hacktualidad.controller;

import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import com.Hacktualidad.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRESTController.class)
class ProductRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProductWithImage() throws Exception {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        String jsonProduct = objectMapper.writeValueAsString(requestDTO);

        MockMultipartFile jsonPart = new MockMultipartFile(
                "product", "", "application/json", jsonProduct.getBytes());

        MockMultipartFile filePart = new MockMultipartFile(
                "photo", "img.png", "image/png", "bytes".getBytes());

        when(productService.createProduct(any(ProductRequestDTO.class), any())).thenReturn(new ProductResponseDTO());

        mockMvc.perform(multipart("/api/products")
                        .file(jsonPart)
                        .file(filePart)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduct() throws Exception {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        String jsonProduct = objectMapper.writeValueAsString(requestDTO);

        MockMultipartFile jsonPart = new MockMultipartFile("product", "", "application/json", jsonProduct.getBytes());

        when(productService.updateProduct(eq(1L), any(), any())).thenReturn(java.util.Optional.of(new ProductResponseDTO()));

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/products/1")
                        .file(jsonPart)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}