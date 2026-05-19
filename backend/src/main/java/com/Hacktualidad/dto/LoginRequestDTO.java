package com.Hacktualidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Credenciales para autenticación de usuario")
public class LoginRequestDTO {

    @Schema(description = "Correo electrónico registrado", example = "admin@hacktualidad.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "P@ssw0rd123!")
    private String password;
}