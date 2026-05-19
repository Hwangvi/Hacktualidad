package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cuerpo para enviar un nuevo comentario")
public class CommentRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Contenido del comentario", example = "No estoy de acuerdo con el punto 3...")
    private String content;
}