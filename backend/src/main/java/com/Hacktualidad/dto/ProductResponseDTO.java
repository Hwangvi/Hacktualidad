package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalles completos de un producto visible en la tienda")
public class ProductResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador único del producto", example = "105")
    private Long productId;

    @Schema(description = "Nombre comercial del producto", example = "Teclado Mecánico RGB")
    private String name;

    @Schema(description = "Descripción detallada", example = "Teclado mecánico con switches azules y retroiluminación configurable.")
    private String description;

    @Schema(description = "Precio unitario en euros", example = "89.99")
    private BigDecimal price;

    @Schema(description = "Unidades disponibles en almacén", example = "150")
    private Integer stock;

    @Schema(description = "Indica si el producto está visible para la venta", example = "true")
    private Boolean active;

    @Schema(description = "Nombre del archivo de imagen o URL", example = "teclado_rgb_v2.jpg")
    private String photo;

    @Schema(description = "Categoría a la que pertenece el producto")
    private CategoryResponseDTO category;
}