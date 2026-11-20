package com.david.commons.dto.huespedes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de un huesped")
public record HuespedResponse(

        @Schema(description = "Identificador unico del huesped", example = "1")
        Long id,

        @Schema(description = "Nombre del Huesped", example = "Juan Carlos Perez")
        String nombre,

        @Schema(description = "Correo electronico del huesped", example = "juan@medico.com")
        String email,

        @Schema(description = "Telefono del huesped", example = "1234567890")
        String telefono,

        @Schema(description = "Tipo del documento dejado por el huesped", example = "3")
        String tipoDocumento,

        @Schema(description = "Numero de indentifiacion del documento", example = "222229777")
        String numeroDocumento,

        @Schema(description = "Nacionalidad del huesped", example = "Mexicano")
        String nacionalidad,

        @Schema(description = "Estado de registro del huesped", example = "Activo")
        String estadoRegistro,
        String apellidos
) {
}
