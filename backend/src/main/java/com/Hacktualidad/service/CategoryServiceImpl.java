package com.Hacktualidad.service;

import com.Hacktualidad.dto.CategoryRequestDTO;
import com.Hacktualidad.dto.CategoryResponseDTO;
import com.Hacktualidad.entity.Category;
import com.Hacktualidad.mapper.CategoryMapper;
import com.Hacktualidad.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Hacktualidad.dto.CategoryCreateUpdateDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO createCategory(CategoryCreateUpdateDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDTO(savedCategory);
    }

    @Override
    public Optional<CategoryResponseDTO> updateCategory(Long id, CategoryCreateUpdateDTO categoryDTO) {
        return categoryRepository.findById(id).map(existingCategory -> {
            existingCategory.setName(categoryDTO.getName());
            existingCategory.setDescription(categoryDTO.getDescription());

            Category updatedCategory = categoryRepository.save(existingCategory);
            return categoryMapper.toCategoryResponseDTO(updatedCategory);
        });
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede borrar la categoría con ID " + id + " porque no existe.");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<CategoryResponseDTO> findCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toCategoryResponseDTO);
    }

    @Override
    public List<CategoryResponseDTO> findAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseDTOs(categories);
    }
}