package com.david.auth.repositories;

import com.david.auth.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameIgnoreCaseAndEstadoRegistro(String username, String estadoRegistro);

    Optional<Usuario> findByIdAndEstadoRegistro(Long id, String estadoRegistro);

    List<Usuario> findByEstadoRegistro(String estadoRegistro);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCaseAndEstadoRegistro(String username, String estadoRegistro);

    long countDistinctByEstadoRegistroAndRoles_Nombre(String estadoRegistro, String nombre);
}