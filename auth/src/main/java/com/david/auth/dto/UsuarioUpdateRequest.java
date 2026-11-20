package com.david.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UsuarioUpdateRequest(
        // Opcional: si no viene (null), se conserva el password actual.
        // Si viene, debe cumplir el mismo tamaño que al registrar.
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9]).+", message = "La contraseña debe contener al menos una letra y un número")
        String password,

        @NotEmpty(message = "El rol es requerido")
        @Size(max = 1, message = "Debe haber un rol")
        Set<String> roles
) {}