package com.leonardo.habitacion.controller;

import com.leonardo.habitacion.service.HabitacionService;
import com.david.commons.controller.CrudController;
import com.david.commons.docs.ProblemaDoc;
import com.david.commons.dto.habitaciones.HabitacionRequest;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/habitacion")
@Tag(name = "API Habitaciones", description = "Métodos para la gestión de habitaciones")
public class HabitacionController extends CrudController<HabitacionRequest, HabitacionResponse, HabitacionService> {

    public HabitacionController(HabitacionService service) {
        super(service);
    }

    @GetMapping("/id-habitacion/{id}")
    @Operation(summary = "Obtener habitación por ID sin importar el estado del registro")
    @ApiResponse(responseCode = "200", description = "Habitación encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "La habitación no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<HabitacionResponse> obtenerHabitacionSinValidarEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerHabitacionSinValidarEstado(id));
    }

    @PatchMapping("/{id}/estado/{idEstado}")
    @Operation(summary = "Cambiar el estado de una habitación (solo ADMIN)")
    public ResponseEntity<HabitacionResponse> cambiarEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @PathVariable Long idEstado
    ) {
        return ResponseEntity.ok(service.cambiarEstado(id, idEstado));
    }

    @PutMapping("/id-habitacion/{id}/estado/{idEstado}")
    @Operation(summary = "Cambiar estado de la habitación (uso interno de Reservas)")
    public ResponseEntity<HabitacionResponse> cambiarEstadoInterno(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @PathVariable Long idEstado
    ) {
        return ResponseEntity.ok(service.cambiarEstadoInterno(id, idEstado));
    }
}