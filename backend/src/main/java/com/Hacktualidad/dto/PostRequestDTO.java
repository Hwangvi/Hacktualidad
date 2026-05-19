package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear o actualizar una publicación")
public class PostRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "Título de la publicación", example = "Análisis de las vulnerabilidades Zero-Day")
    private String title;

    @Schema(description = "Contenido completo del artículo", example = "En este artículo exploraremos cómo funcionan...")
    private String content;
}