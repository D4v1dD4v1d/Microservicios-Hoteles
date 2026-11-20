package com.david.auth.services;

import com.david.auth.dto.UsuarioRequest;
import com.david.auth.dto.UsuarioResponse;
import com.david.auth.dto.UsuarioUpdateRequest;
import com.david.auth.entities.Rol;
import com.david.auth.entities.Usuario;
import com.david.auth.exceptions.ConflictoException;
import com.david.auth.exceptions.DatoInvalidoException;
import com.david.auth.exceptions.RecursoNoEncontradoException;
import com.david.auth.mapper.UsuarioMapper;
import com.david.auth.repositories.RolRepository;
import com.david.auth.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder; // bean BCrypt de SecurityConfig

    @Override
    @Transactional(readOnly = true) // solo lectura: más ligero, no abre escritura
    public Set<UsuarioResponse> listar() {
        log.info("Listado de todos los usuarios solicitado");
        return usuarioRepository.findByEstadoRegistro("ACTIVO").stream()
                .map(usuarioMapper::entityToResponse).collect(Collectors.toSet());
    }

    @Override
    public UsuarioResponse registrar(UsuarioRequest request) {
        // Normalizamos primero: validamos y guardamos exactamente el mismo valor
        String username = request.username().trim();

        log.info("Buscando usuario {}", username);
        if (usuarioRepository.existsByUsernameIgnoreCaseAndEstadoRegistro(username, "ACTIVO")) {
            // 409: viola una regla de negocio (username único)
            throw new ConflictoException("El usuario " + username + " ya está registrado");
        }

        Set<Rol> roles = resolverRoles(request.roles());

        Usuario usuario = usuarioMapper.requestToEntity(request,
                passwordEncoder.encode(request.password()), roles);

        usuarioRepository.save(usuario);
        return usuarioMapper.entityToResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return usuarioMapper.entityToResponse(buscarPorId(id));
    }

    @Override
    public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request) {
        log.info("Actualizando usuario {}", id);
        Usuario usuario = buscarPorId(id);
        Set<Rol> roles = resolverRoles(request.roles());
        if (roles.stream().noneMatch(rol -> "ROLE_ADMIN".equals(rol.getNombre())))
            validarUltimoAdmin(usuario);
        usuario.setRoles(roles);

        // El password es opcional: solo se cambia si viene en la petición
        if (request.password() != null) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        usuarioRepository.save(usuario);
        return usuarioMapper.entityToResponse(usuario);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando usuario {}", id);
        Usuario usuario = buscarPorId(id);
        if (usuario.getUsername().equalsIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName()))
            throw new ConflictoException("No puedes eliminar tu propio usuario");
        validarUltimoAdmin(usuario);
        usuario.setEstadoRegistro("ELIMINADO");
        usuarioRepository.save(usuario);
    }

    private void validarUltimoAdmin(Usuario usuario) {
        if (usuario.getRoles().stream().anyMatch(rol -> "ROLE_ADMIN".equals(rol.getNombre()))
                && usuarioRepository.countDistinctByEstadoRegistroAndRoles_Nombre("ACTIVO", "ROLE_ADMIN") <= 1)
            throw new ConflictoException("No se puede eliminar o cambiar el rol del último administrador activo");
    }

    private Usuario buscarPorId(Long id) {
        return usuarioRepository.findByIdAndEstadoRegistro(id, "ACTIVO")
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario: " + id));
    }

    /**
     * Convierte los nombres de rol recibidos en entidades.
     * Cada nombre debe existir en BD; si uno falla, no se guarda nada.
     */
    private Set<Rol> resolverRoles(Set<String> nombres) {
        if (nombres == null || nombres.size() != 1 ||
                (!nombres.contains("ROLE_ADMIN") && !nombres.contains("ROLE_USER")))
            throw new DatoInvalidoException("Debe seleccionar ADMIN o USER");
        return nombres.stream().map(nombre ->
                rolRepository.findByNombre(nombre).orElseThrow(() ->
                        // 400: el cliente envió un rol que no existe
                        new DatoInvalidoException("El rol " + nombre + " no existe"))
        ).collect(Collectors.toSet());
    }
}