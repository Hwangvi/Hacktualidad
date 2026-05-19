package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estado actual del carrito de compras")
public class CartDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador del carrito", example = "890")
    private Long cartId;

    @Schema(description = "Lista de elementos añadidos al carrito")
    private List<CartElementDTO> items;

    @Schema(description = "Monto total a pagar", example = "299.99")
    private Double totalAmount;
}