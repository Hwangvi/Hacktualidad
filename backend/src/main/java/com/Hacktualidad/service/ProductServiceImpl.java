package com.Hacktualidad.service;

import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import com.Hacktualidad.entity.Category;
import com.Hacktualidad.entity.Product;
import com.Hacktualidad.mapper.ProductMapper;
import com.Hacktualidad.repository.CategoryRepository;
import com.Hacktualidad.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryStorageService cloudinaryStorageService;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponseDTO);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, MultipartFile photo) {
        Product product = productMapper.toProductEntity(productRequestDTO);

        Long categoryId = productRequestDTO.getCategory().getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con el id: " + categoryId));
        product.setCategory(category);

        if (photo != null && !photo.isEmpty()) {
            String photoUrl = cloudinaryStorageService.storeFile(photo);
            product.setPhoto(photoUrl);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDTO(savedProduct);
    }

    @Override
    public Optional<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO productRequestDTO, MultipartFile photo) {
        return productRepository.findById(id).map((existingProduct) -> {
            productMapper.updateProductFromDto(productRequestDTO, existingProduct);

            Long categoryId = productRequestDTO.getCategory().getCategoryId();
            Category newCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con el id: " + categoryId));
            existingProduct.setCategory(newCategory);

            if (photo != null && !photo.isEmpty()) {
                if (existingProduct.getPhoto() != null && !existingProduct.getPhoto().isEmpty()) {
                    cloudinaryStorageService.deleteFile(existingProduct.getPhoto());
                }
                String newPhotoUrl = cloudinaryStorageService.storeFile(photo);
                existingProduct.setPhoto(newPhotoUrl);
            }

            Product updatedProduct = productRepository.save(existingProduct);
            return productMapper.toProductResponseDTO(updatedProduct);
        });
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El producto con ID " + id + " no existe."));

        if (product.getPhoto() != null && !product.getPhoto().isEmpty()) {
            cloudinaryStorageService.deleteFile(product.getPhoto());
        }

        try {
            productRepository.delete(product);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("No se puede eliminar el producto porque está asociado a un Carrito de Compras o Pedido.");
        }
    }
}