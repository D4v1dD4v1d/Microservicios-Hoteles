package com.david.auth.controllers;

import com.david.auth.dto.UsuarioRequest;
import com.david.auth.dto.UsuarioResponse;
import com.david.auth.dto.UsuarioUpdateRequest;
import com.david.auth.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/admin/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Set<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        // 201 Created: el POST crea un recurso nuevo
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        // 204 No Content: se eliminó y no hay nada que devolver
        return ResponseEntity.noContent().build();
    }
}