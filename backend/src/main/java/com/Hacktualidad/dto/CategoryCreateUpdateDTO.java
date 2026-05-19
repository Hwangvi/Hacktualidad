package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto para creación o edición de categorías")
public class CategoryCreateUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre de la categoría", example = "Gadgets")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción breve de la categoría", example = "Dispositivos electrónicos y accesorios")
    private String description;
}