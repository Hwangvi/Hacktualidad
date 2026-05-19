package com.Hacktualidad.service;

import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import com.Hacktualidad.entity.Category;
import com.Hacktualidad.entity.Product;
import com.Hacktualidad.mapper.ProductMapper;
import com.Hacktualidad.repository.CategoryRepository;
import com.Hacktualidad.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponseDTO);
    }

    private String saveImage(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return null;
        }
        try {
            String originalFilename = photo.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen: " + e.getMessage());
        }
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, MultipartFile photo) {
        Product product = productMapper.toProductEntity(productRequestDTO);

        Long categoryId = productRequestDTO.getCategory().getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con el id: " + categoryId));
        product.setCategory(category);

        String photoFilename = saveImage(photo);
        product.setPhoto(photoFilename);

        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDTO(savedProduct);
    }

    @Override
    public Optional<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO productRequestDTO, MultipartFile photo) {
        return productRepository.findById(id).map(existingProduct -> {

            productMapper.updateProductFromDto(productRequestDTO, existingProduct);

            Long categoryId = productRequestDTO.getCategory().getCategoryId();
            Category newCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con el id: " + categoryId));
            existingProduct.setCategory(newCategory);

            if (photo != null && !photo.isEmpty()) {
                String newPhotoFilename = saveImage(photo);
                existingProduct.setPhoto(newPhotoFilename);
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
            try {
                Path photoPath = Paths.get("uploads").resolve(product.getPhoto());
                Files.deleteIfExists(photoPath);
            } catch (IOException e) {
            }
        }

        try {
            productRepository.delete(product);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("No se puede eliminar el producto porque está asociado a un Carrito de Compras o Pedido.");
        }
    }
}