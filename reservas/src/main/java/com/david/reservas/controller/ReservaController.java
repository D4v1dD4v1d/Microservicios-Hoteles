package com.david.reservas.controller;

import com.david.commons.controller.CrudController;
import com.david.commons.docs.ProblemaDoc;
import com.david.reservas.dto.ActualizarReservaRequest;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import com.david.reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PutMapping("/actualizarReserva/{id}")
    @Operation(summary = "Actualizar un recurso existente")
    @ApiResponse(responseCode = "200",description = "Recurso actualizado.")
    @ApiResponse(responseCode = "404", description = "El recurso no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<ReservaResponse> actualizarReserva(
            @Valid @RequestBody ActualizarReservaRequest request,
            @Parameter(description = "Id del recurso.", example = "1")
            @PathVariable @Positive(message = "El id debe ser positivo.") Long id

    ){
        validarIdNotNullPositive(id);
        return ResponseEntity.ok(service.actualizarReserva(request,id));
    }

    @ApiResponse(responseCode = "204", description = "El húesped no tiene reservas asignadas")
    @ApiResponse(responseCode = "409", description = "Húesped tiene reservas asignadas y no se puede modificar ni eliminar",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @GetMapping("/id-huesped/{idHuesped}/reservas-asignadas")
    public ResponseEntity<Void> huespedTieneReservasAsignadas(
            @Parameter(description = "Id del húesped", example = "1")
            @PathVariable @Positive(message = "El idHuesped debe ser positivo") Long idHuesped){
        service.huespedTieneReservasAsignadas(idHuesped);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id-huesped/{idHuesped}/validar-eliminacion")
    @Operation(summary = "Validar si un huésped puede ser eliminado (no debe tener reservas activas)")
    @ApiResponse(responseCode = "204", description = "El huésped no tiene reservas que impidan su eliminación")
    @ApiResponse(responseCode = "409", description = "El huésped tiene reservas activas o en curso y no puede eliminarse",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> validarEliminacionHuesped(
            @PathVariable @Positive(message = "El ID del huésped debe ser positivo") Long idHuesped
    ) {
        service.validarEliminacionHuesped(idHuesped);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "204", description = "La habitación no tiene reservas asignadas")
    @ApiResponse(responseCode = "409", description = "La habitación tiene reservas asignadas y no se puede modificar ni eliminar",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @GetMapping("/id-habitacion/{idhabitacion}/reservas-asignadas")
    public ResponseEntity<Void> habitacionTieneReservasAsignadas(
            @Parameter(description = "Id de la habitación", example = "1")
            @PathVariable @Positive(message = "El idHabitacion debe ser positivo") Long idHabitacion){
        service.habitacionTieneReservasAsignadas(idHabitacion);
        return ResponseEntity.noContent().build();
    }

}
