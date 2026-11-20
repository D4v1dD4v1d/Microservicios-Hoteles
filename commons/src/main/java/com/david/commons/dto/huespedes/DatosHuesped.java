package com.david.commons.dto.huespedes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del huesped asociado a una reservacion")
public record DatosHuesped(

        @Schema(description = "Nombre completo del Huesped", example = "Juan Carlos Perez")
        String nombre,

        @Schema(description = "Telefono del huesped", example = "1234567890")
        String telefono,

        @Schema(description = "Tipo del documento dejado por el huesped", example = "INE")
        String tipoDocumento,

        @Schema(description = "Numero de indentifiacion del documento", example = "222229777")
        String numeroDocumento,

        @Schema(description = "Nacionalidad del huesped", example = "Mexicano")
        String nacionalidad
) {
}
