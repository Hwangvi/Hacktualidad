package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información sobre un tema de discusión")
public class TopicDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador del tema", example = "3")
    private int topicId;

    @Schema(description = "Nombre del tema", example = "Inteligencia Artificial")
    private String topicName;

    @Schema(description = "Descripción detallada", example = "Noticias y debates sobre IA y Machine Learning")
    private String topicDescription;

    @Schema(description = "URL de la imagen de fondo", example = "ai_background.jpg")
    private String backgroundImage;
}