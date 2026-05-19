package com.Hacktualidad.mapper;

import com.Hacktualidad.dto.CartElementDTO;
import com.Hacktualidad.entity.CartElement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {

    @Mapping(target = "product", source = "product")
    @Mapping(target = "subtotal", source = ".", qualifiedByName = "calculateSubtotal")
    CartElementDTO toCartElementDTO(CartElement cartElement);

    List<CartElementDTO> toCartElementDTOList(List<CartElement> cartElements);

    @Named("calculateSubtotal")
    default Double calculateSubtotal(CartElement element) {
        if (element.getProduct() == null || element.getProduct().getPrice() == null) {
            return 0.0;
        }
        return element.getProduct().getPrice() * element.getQuantity();
    }
}