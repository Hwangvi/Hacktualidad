package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalles de un comentario en una publicación")
public class CommentResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "ID del comentario", example = "101")
    private Long commentId;

    @Schema(description = "Texto del comentario", example = "¡Excelente artículo! Muy bien explicado.")
    private String content;

    @Schema(description = "Fecha y hora de publicación", example = "2023-10-25T14:30:00")
    private LocalDateTime commentDate;

    @Schema(description = "Información del usuario que escribió el comentario")
    private AuthorDTO author;
}