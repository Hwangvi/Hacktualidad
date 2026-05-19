package com.Hacktualidad.dto;

import com.Hacktualidad.Enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información pública del usuario devuelta por la API")
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador único del usuario", example = "42")
    private Long userId;

    @Schema(description = "Nombre de pila del usuario", example = "Laura")
    private String name;

    @Schema(description = "Apellidos del usuario", example = "García López")
    private String surname;

    @Schema(description = "Dirección postal completa", example = "Calle Gran Vía 12, Madrid")
    private String address;

    @Schema(description = "Número de teléfono de contacto", example = "600123456")
    private Integer phone;

    @Schema(description = "Nombre del archivo o URL de la foto de perfil", example = "avatar_user_42.png")
    private String photo;

    @Schema(description = "Correo electrónico registrado", example = "laura.garcia@hacktualidad.com")
    private String email;

    @Schema(description = "Rol del usuario en la aplicación", example = "USER")
    private Role role;

}