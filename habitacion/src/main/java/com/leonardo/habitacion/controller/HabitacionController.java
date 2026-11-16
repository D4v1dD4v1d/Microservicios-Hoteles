package com.leonardo.habitacion.controller;

import com.leonardo.habitacion.dto.HabitacionRequest;
import com.leonardo.habitacion.dto.HabitacionResponse;
import com.leonardo.habitacion.service.HabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Habitaciones", description = "Administración de habitaciones del hotel")
public class HabitacionController {

    private final HabitacionService habitacionService;

    @PostMapping
    @Operation(summary = "Registrar una habitación")
    public ResponseEntity<HabitacionResponse> registrar(
            @Valid @RequestBody HabitacionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(habitacionService.registrar(request));
    }

    @GetMapping
    @Operation(summary = "Listar habitaciones activas")
    public ResponseEntity<List<HabitacionResponse>> listar() {

        return ResponseEntity.ok(habitacionService.listar());
    }

    @GetMapping("/{idHabitacion}")
    @Operation(summary = "Buscar habitación por ID")
    public ResponseEntity<HabitacionResponse> buscarPorId(
            @PathVariable Long idHabitacion) {

        return ResponseEntity.ok(habitacionService.buscarPorId(idHabitacion));
    }

    @PutMapping("/{idHabitacion}")
    @Operation(summary = "Actualizar una habitación")
    public ResponseEntity<HabitacionResponse> actualizar(
            @PathVariable Long idHabitacion,
            @Valid @RequestBody HabitacionRequest request) {

        return ResponseEntity.ok(
                habitacionService.actualizar(idHabitacion, request)
        );
    }

    @DeleteMapping("/{idHabitacion}")
    @Operation(summary = "Eliminar lógicamente una habitación")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long idHabitacion) {

        habitacionService.eliminar(idHabitacion);

        return ResponseEntity.noContent().build();
    }
}
