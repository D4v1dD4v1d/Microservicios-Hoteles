package com.david.huespedes.controllers;

import com.david.commons.controller.CrudController;
import com.david.commons.dto.huespedes.HuespedRequest;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.service.CrudService;
import com.david.commons.docs.ProblemaDoc;
import com.david.huespedes.service.HuespedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "API Huespedes", description = "Métodos para la gestión de huespedes")
public class HuespedController extends CrudController<HuespedRequest, HuespedResponse, HuespedService> {
    public HuespedController(HuespedService service) {
        super(service);
    }

    @GetMapping("/id-huesped/{id}")
    @Operation(summary = "Obtener huesped por id sin importar el estado del registro")
    @ApiResponse(responseCode = "200", description = "Huesped sin estado encontrado")
    @ApiResponse(responseCode = "404", description = "El huesped sin estado no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<HuespedResponse> obtenerHuespedPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerHuespedPorIdSinEstado(id));
    }

    @PutMapping("/{id}/tipoDocumento/{idTipoDocumento}")
    @Operation(summary = "Actualizar el tipo de documento" +
            "(no es endpoint libre, debe gestionarlo el sistema de Reservas)")
    @ApiResponse(responseCode = "204", description = "Tipo de documento actualizado")
    @ApiResponse(responseCode = "409", description = "El tipo de documento no puede " +
            "actualizarse por regla de negocio",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> actualizarTipoDocumento(
            @PathVariable @Positive(message = "El id del huesped debe ser positivo") Long id,
            @PathVariable @Positive(message = "El id del TIpo de documento debe ser positivo") Long idTipoDocumento
    ) {
        service.actualizarTipoDocumento(id, idTipoDocumento);
        return ResponseEntity.noContent().build();
    }

}
