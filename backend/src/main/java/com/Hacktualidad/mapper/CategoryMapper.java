package com.Hacktualidad.mapper;

import com.Hacktualidad.dto.CategoryRequestDTO;
import com.Hacktualidad.dto.CategoryResponseDTO;
import com.Hacktualidad.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDTO toCategoryResponseDTO(Category category);
    List<CategoryResponseDTO> toCategoryResponseDTOs(List<Category> categories);
    Category toCategory(CategoryRequestDTO categoryRequestDTO);
    void updateCategoryFromDto(CategoryRequestDTO dto, @MappingTarget Category category);
}