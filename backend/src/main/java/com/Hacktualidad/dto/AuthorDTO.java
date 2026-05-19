package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información simplificada del autor del contenido")
public class AuthorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador único del usuario", example = "42")
    private Long userId;

    @Schema(description = "Nombre visible o Alias", example = "MrRobot")
    private String name;

    @Schema(description = "URL o nombre de archivo del avatar", example = "user_42_avatar.png")
    private String photo;
}