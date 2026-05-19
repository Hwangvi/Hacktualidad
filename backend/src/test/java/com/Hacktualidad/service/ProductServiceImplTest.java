package com.Hacktualidad.service;

import com.Hacktualidad.dto.CategoryRequestDTO;
import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import com.Hacktualidad.entity.Category;
import com.Hacktualidad.entity.Product;
import com.Hacktualidad.mapper.ProductMapper;
import com.Hacktualidad.repository.CategoryRepository;
import com.Hacktualidad.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductMapper productMapper;
    @Mock private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldCreateProductWithoutPhoto() {
        ProductRequestDTO request = new ProductRequestDTO();

        CategoryRequestDTO catReqDTO = new CategoryRequestDTO();
        catReqDTO.setCategoryId(1L);

        request.setCategory(catReqDTO);

        Category categoryEntity = new Category();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setName("Electrónica");

        Product product = new Product();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(productMapper.toProductEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductResponseDTO(product)).thenReturn(new ProductResponseDTO());

        ProductResponseDTO result = productService.createProduct(request, null);

        assertNotNull(result);
        verify(productRepository).save(product);
    }
}