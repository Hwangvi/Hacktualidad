package com.Hacktualidad.mapper;

import com.Hacktualidad.dto.ProductRequestDTO;
import com.Hacktualidad.dto.ProductResponseDTO;
import com.Hacktualidad.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;


@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    ProductResponseDTO toProductResponseDTO(Product product);
    List<ProductResponseDTO> toProductResponseDTOs(List<Product> products);
    Product toProductEntity(ProductRequestDTO productRequestDTO);
    @Mapping(target = "category", ignore = true)
    void updateProductFromDto(ProductRequestDTO dto, @MappingTarget Product product);
}