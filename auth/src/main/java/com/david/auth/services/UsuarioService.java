package com.david.auth.services;

import com.david.auth.dto.UsuarioRequest;
import com.david.auth.dto.UsuarioResponse;
import com.david.auth.dto.UsuarioUpdateRequest;

import java.util.Set;

public interface UsuarioService {
    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse obtenerPorId(Long id);

    UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request);

    void eliminar(Long id);
}
