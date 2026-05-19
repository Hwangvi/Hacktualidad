package com.Hacktualidad.controller;

import com.Hacktualidad.dto.CategoryCreateUpdateDTO;
import com.Hacktualidad.dto.CategoryResponseDTO;
import com.Hacktualidad.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Categorías", description = "Clasificación de elementos/productos")
public class CategoryRESTController {

    private final CategoryService categoryService;

    public CategoryRESTController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Listar categorías")
    @GetMapping
    public List<CategoryResponseDTO> getCategories() {
        return categoryService.findAllCategory();
    }

    @Operation(summary = "Obtener categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear categoría")
    @PostMapping
    public CategoryResponseDTO createCategory(@Valid @RequestBody CategoryCreateUpdateDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @Operation(summary = "Actualizar categoría")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateUpdateDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}