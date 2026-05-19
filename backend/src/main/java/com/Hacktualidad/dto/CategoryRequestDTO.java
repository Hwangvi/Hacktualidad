package com.Hacktualidad.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO implements Serializable {
    @NotNull(message = "El ID de la categoría no puede ser nulo")
    private static final long serialVersionUID = 1L;
    private Long categoryId;
}