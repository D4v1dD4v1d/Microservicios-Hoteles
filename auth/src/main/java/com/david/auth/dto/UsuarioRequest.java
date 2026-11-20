package com.david.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UsuarioRequest(
        @NotBlank(message = "El username es requerido")
        @Size(min = 5, max = 20, message = "El usuario debe tener entre 5 y 20 caracteres")
        @Pattern(regexp = "[A-Za-z0-9._-]+", message = "El usuario contiene caracteres inválidos")
        String username,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9]).+", message = "La contraseña debe contener al menos una letra y un número")
        String password,

        @NotNull(message = "Los roles son requeridos")
        @Size(min = 1, max = 1, message = "Debe haber un rol")
        Set<String> roles
) {}