package com.Hacktualidad.service;

import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    Optional<ProductResponseDTO> getProductById(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, MultipartFile photo);
    Optional<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO productRequestDTO, MultipartFile photo);
    void deleteProduct(Long id);
}