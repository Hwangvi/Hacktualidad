package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Elemento individual (línea) dentro del carrito")
public class CartElementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único de la línea del carrito", example = "55")
    private Long id;

    @Schema(description = "Información del producto añadido")
    private ProductResponseDTO product;

    @Schema(description = "Cantidad de unidades", example = "2")
    private int quantity;

    @Schema(description = "Subtotal (Precio x Cantidad)", example = "50.00")
    private Double subtotal;
}