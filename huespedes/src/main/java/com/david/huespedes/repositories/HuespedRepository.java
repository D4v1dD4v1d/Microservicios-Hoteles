package com.david.huespedes.repositories;

import com.david.commons.enums.EstadoRegistro;
import com.david.commons.enums.TipoDocumento;
import com.david.huespedes.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    List<Huesped> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Huesped> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
    boolean existsByTipoDocumentoAndNumeroDocumentoIgnoreCaseAndEstadoRegistro(TipoDocumento tipoDocumento, String numeroDocumento, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long id);
    boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long id);
    boolean existsByTipoDocumentoAndNumeroDocumentoIgnoreCaseAndEstadoRegistroAndIdNot(TipoDocumento tipoDocumento, String numeroDocumento, EstadoRegistro estadoRegistro, Long id);

}
