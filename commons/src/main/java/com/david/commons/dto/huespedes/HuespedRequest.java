package com.david.commons.dto.huespedes;

import com.david.commons.enums.TipoDocumento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos necesarios para registrar o actualizar un huesped")
public record HuespedRequest(

        @Schema(description = "Nombre del huesped", example = "Christian")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @Schema(description = "Apellidos del Huesped", example = "Blancas Elvira")
        @NotBlank(message = "Los apellidos son requeridos")
        @Size(min = 2, max = 50, message = "Los apellidos debe tener entre 2 y 50 caracteres")
        String apellidos,

        @Schema(description = "Correo electrónico del huesped", example = "christian.garcia@example.com")
        @NotBlank(message = "El email es requerido")
        @Email(message = "Debe ingresar un formato de email válido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener el formato correcto (email@dominio)")
        String email,

        @Schema(description = "Número telefónico de contacto. Debe contener exactamente 10 dígitos.",
                example = "2221234567", pattern = "^[0-9]{10}$")
        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener solo 10 dígitos numéricos")
        String telefono,

        @Schema(description = "Numero del documento proporcionado del huesped", example = "123456789102466")
        @NotBlank(message = "El numero es requerido")
        @Size(min = 1, max = 50, message = "El numero de documento debe tener entre 1 y 50 caracteres")
        String numeroDocumento,

        @Schema(description = "Tipo de documento proporcionado del huesped", example = "INE")
        @NotNull(message = "El tipo de documento es requerido")
        @Positive(message = "El id del tipo de documento debe ser positivo")
        Long idTipoDocumento,

        @Schema(description = "Nacionalidad del huesped", example = "Mexicano")
        @NotBlank(message = "La nacionalidad es requerida")
        @Size(min = 1, max = 50, message = "El apellido materno debe tener entre 1 y 50 caracteres")
        String nacionalidad
) {
}
