package com.Hacktualidad.controller;

import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import com.Hacktualidad.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Catálogo de Productos", description = "Operaciones CRUD para los productos")
public class ProductRESTController {

    @Autowired
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "Listar productos", description = "Devuelve el catálogo completo.")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Ver producto", description = "Obtiene el detalle de un producto por ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear producto", description = "Sube un producto nuevo con foto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en el formato del JSON")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Parameter(description = "JSON String de ProductRequestDTO")
            @RequestPart("product") String productJson,
            @Parameter(description = "Foto del producto")
            @RequestPart(value= "photo", required = false) MultipartFile photo) {
        try {
            ProductRequestDTO productRequestDTO = objectMapper.readValue(productJson, ProductRequestDTO.class);
            ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO, photo);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            System.err.println(">>> ERROR JSON: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Editar producto", description = "Modifica datos o foto de un producto existente.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart("product") String productJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        try {
            ProductRequestDTO productRequestDTO = objectMapper.readValue(productJson, ProductRequestDTO.class);
            return productService.updateProduct(id, productRequestDTO, photo)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}