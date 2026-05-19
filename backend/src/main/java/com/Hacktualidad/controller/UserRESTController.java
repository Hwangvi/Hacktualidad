package com.Hacktualidad.controller;

import com.Hacktualidad.dto.LoginRequestDTO;
import com.Hacktualidad.dto.UserRequestDTO;
import com.Hacktualidad.dto.UserResponseDTO;
import com.Hacktualidad.dto.UserUpdateRequestDTO;
import com.Hacktualidad.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Gestión de Usuarios", description = "Registro, login y administración de usuarios")
public class UserRESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Obtener hash de contraseña", description = "Utilidad para ver cómo se encripta una contraseña.")
    @GetMapping("/hash/{password}")
    public String getPasswordHash(@PathVariable String password) {
        return "El hash para '" + password + "' es: " + passwordEncoder.encode(password);
    }

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con avatar opcional.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponseDTO> register(
            @Parameter(description = "JSON String con los datos del usuario (UserRequestDTO)", schema = @Schema(type = "string"))
            @RequestParam("user") String userRequestDtoString,
            @Parameter(description = "Archivo de imagen para el avatar")
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        UserRequestDTO userRequestDTO = new ObjectMapper().readValue(userRequestDtoString, UserRequestDTO.class);
        UserResponseDTO registeredUser = userService.registerUser(userRequestDTO, file);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve sus datos/token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<UserResponseDTO> userResponse = userService.loginUser(loginRequestDTO);
        if (userResponse.isPresent()) {
            return ResponseEntity.ok(userResponse.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña incorrectos");
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Borra un usuario por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar perfil", description = "Actualiza datos y/o avatar del usuario logueado o específico.")
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Parameter(description = "JSON String con los datos a actualizar")
            @RequestPart("user") String userUpdateDtoString,
            @Parameter(description = "Nueva imagen de perfil (opcional)")
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        UserUpdateRequestDTO userUpdateRequestDTO = new ObjectMapper().readValue(userUpdateDtoString, UserUpdateRequestDTO.class);
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateRequestDTO, file);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Listar todos (Admin)", description = "Obtiene todos los usuarios registrados.")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Operation(summary = "Obtener por ID (Admin)", description = "Busca un usuario específico.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Operation(summary = "Crear usuario (Admin)", description = "Creación administrativa de usuarios.")
    @PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestPart("user") String userRequestDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        UserRequestDTO userRequestDTO = new ObjectMapper().readValue(userRequestDtoString, UserRequestDTO.class);
        UserResponseDTO newUser = userService.createUser(userRequestDTO, file);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}