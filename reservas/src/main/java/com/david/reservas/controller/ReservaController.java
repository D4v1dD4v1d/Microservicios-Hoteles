package com.david.reservas.controller;

import com.david.commons.controller.CrudController;
import com.david.commons.docs.ProblemaDoc;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import com.david.reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "API reserva", description = "Métodos para la géstion de las reservas")
public class ReservaController extends CrudController<ReservaRequest, ReservaResponse, ReservaService> {

    public ReservaController(ReservaService service) {
        super(service);
    }

    @Operation(summary = "Actualizar estado de la reserva",
            description = "Actualiza el estado de una reserva utilizando el identificador de la reserva y el" +
                    "identificador del estado")
    @ApiResponse(responseCode = "204", description = "Estado de la reserva actualizado")
    @ApiResponse(responseCode = "409", description = "El estado de la reserva no puede actualizarse por regla de negocio",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @PatchMapping("/{idReserva}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoCita(
            @PathVariable @Positive(message = "El idReserva debe ser positivo") Long idReserva,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado
    ) {
        service.actualizarEstadoReserva(idReserva,idEstado);
        return ResponseEntity.noContent().build();
    }

}
