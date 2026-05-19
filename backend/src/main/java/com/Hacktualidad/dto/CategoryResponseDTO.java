package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de una categoría de productos")
public class CategoryResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador único de la categoría", example = "1")
    private Long categoryId;

    @Schema(description = "Nombre de la categoría", example = "Periféricos")
    private String name;

    @Schema(description = "Descripción breve del contenido", example = "Teclados, ratones, auriculares y otros accesorios")
    private String description;
}