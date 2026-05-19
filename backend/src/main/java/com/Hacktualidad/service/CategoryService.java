package com.Hacktualidad.service;
import com.Hacktualidad.dto.CategoryCreateUpdateDTO;
import com.Hacktualidad.dto.CategoryRequestDTO;
import com.Hacktualidad.dto.CategoryResponseDTO;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryCreateUpdateDTO categoryDTO);
    Optional<CategoryResponseDTO> updateCategory(Long id, CategoryCreateUpdateDTO categoryDTO);
    void deleteCategory(Long id);
    Optional<CategoryResponseDTO> findCategoryById(Long id);
    List<CategoryResponseDTO> findAllCategory();

}