package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta completa con los detalles de una publicación")
public class PostResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único del post", example = "505")
    private Long postId;

    @Schema(description = "Título del post", example = "Guía de seguridad en Java")
    private String title;

    @Schema(description = "Contenido del post", example = "Java ofrece múltiples herramientas para...")
    private String content;

    @Schema(description = "Fecha de publicación", example = "2023-11-01T09:00:00")
    private LocalDateTime publicationDate;

    @Schema(description = "Autor del post")
    private AuthorDTO author;

    @Schema(description = "Lista de comentarios asociados")
    private List<CommentResponseDTO> comments;

    @Schema(description = "Nombre del tema o categoría", example = "Ciberseguridad")
    private String topicName;
}