package com.leonardo.habitacion.controller;

import com.david.commons.dto.habitaciones.*;
import com.david.commons.enums.EstadoHabitacion;
import com.leonardo.habitacion.service.HabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Habitaciones", description = "Gestión de habitaciones del hotel")
public class HabitacionController {
    private final HabitacionService service;

    @PostMapping
    @Operation(summary = "Registrar habitación (ADMIN)")
    public ResponseEntity<HabitacionResponse> registrar(@Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @GetMapping
    @Operation(summary = "Listar habitaciones activas (USER/ADMIN)")
    public ResponseEntity<List<HabitacionResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{idHabitacion}")
    @Operation(summary = "Consultar habitación activa (USER/ADMIN)")
    public ResponseEntity<HabitacionResponse> buscar(@PathVariable @Positive Long idHabitacion) {
        return ResponseEntity.ok(service.buscarPorId(idHabitacion));
    }

    @GetMapping("/id-habitacion/{idHabitacion}")
    @Operation(summary = "Consulta interna para Feign (sin estadoRegistro)")
    public ResponseEntity<DatosHabitacion> consultaInterna(@PathVariable @Positive Long idHabitacion) {
        return ResponseEntity.ok(service.consultarInterno(idHabitacion));
    }

    @PutMapping("/{idHabitacion}")
    @Operation(summary = "Actualizar datos de habitación, sin modificar su estado (ADMIN)")
    public ResponseEntity<HabitacionResponse> actualizar(
            @PathVariable @Positive Long idHabitacion, @Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.ok(service.actualizar(idHabitacion, request));
    }

    @PatchMapping("/{idHabitacion}/estado/{idEstado}")
    @Operation(summary = "Cambio manual de estado (ADMIN)")
    public ResponseEntity<Void> cambiarEstado(@PathVariable @Positive Long idHabitacion,
                                               @PathVariable @Positive Long idEstado) {
        service.cambiarEstadoManual(idHabitacion, idEstado);
        return ResponseEntity.noContent().build();
    }

    // Necesita autorización de servicio a servicio: NO exponer esta ruta a usuarios finales.
    @PatchMapping("/interno/{idHabitacion}/estado/{estado}")
    @Operation(summary = "Cambio automático exclusivo de Reservas")
    public ResponseEntity<Void> cambioAutomatico(@PathVariable @Positive Long idHabitacion,
                                                  @PathVariable EstadoHabitacion estado) {
        service.cambiarEstadoAutomatico(idHabitacion, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idHabitacion}")
    @Operation(summary = "Eliminar lógicamente una habitación (ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long idHabitacion) {
        service.eliminar(idHabitacion);
        return ResponseEntity.noContent().build();
    }
}
