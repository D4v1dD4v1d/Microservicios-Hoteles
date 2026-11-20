package com.david.huespedes.service;

import com.david.commons.clients.ReservaClient;
import com.david.commons.dto.habitaciones.RestriccionesReservaHabitacion;
import com.david.commons.dto.huespedes.HuespedRequest;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.enums.EstadoHabitacion;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.enums.TipoDocumento;
import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import com.david.huespedes.entity.Huesped;
import com.david.huespedes.mapper.HuespedMapper;
import com.david.huespedes.repositories.HuespedRepository;
import com.david.reservas.enums.EstadoReserva;
import com.david.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HuespedServiceImpl implements HuespedService{

    private final HuespedRepository huespedRepository;

    private final HuespedMapper huespedMapper;

    private final ReservaClient reservaClient;

    @Override
    public HuespedResponse obtenerHuespedPorIdSinEstado(Long id) {
        log.info("Buscando huesped sin estado con ID: ", id);
        return huespedMapper.entidadAResponse(huespedRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Huesped sin estado no encontrado con ID: " + id
                )
        ));
    }

    @Override
    public void actualizarTipoDocumento(Long id, Long idTipoMedico) {
        Huesped huesped = obtenerHuespedActivoPorId(id);

        log.info("Actualizando estado registro de huesped con ID: {}", id);

        TipoDocumento nuevoTipoDocumento = TipoDocumento.obtenerTipoDocumentoPorCodigo(idTipoMedico);

        TipoDocumento tipoDocumentoAnt = huesped.getTipoDocumento();

        huesped.actualizarTipoDocumento(nuevoTipoDocumento);

        huespedRepository.saveAndFlush(huesped);

        log.info("Tipo de documento del huesped con ID {} cambio de {} a {}",
                id, tipoDocumentoAnt, nuevoTipoDocumento
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponse> lista() {
        log.info("Listando a todos los huespedes activos");

        return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(huespedMapper::entidadAResponse).toList();
    }

    @Override
    public HuespedResponse obtenerPorId(Long id) {
        return huespedMapper.entidadAResponse(obtenerHuespedActivoPorId(id));
    }

    @Override
    public HuespedResponse registrar(HuespedRequest request) {
        log.info("Registrando nuevo huesped: {}", request.nombre());

        validarDatosUnicos(request);

        Huesped huesped = huespedMapper.requestAEntidad(request);

        huespedRepository.saveAndFlush(huesped);

        log.info("Nuevo huesped registrado: {}", huesped.obtenerNombreCompleto());

        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public HuespedResponse actualizar(HuespedRequest request, Long id) {
        Huesped huesped = obtenerHuespedActivoPorId(id);

        validarHuespedTieneReservacionAgendada(id);

        validarCambiosUnicos(request, id);

        log.info("Actualizando huesped con ID: {}", id);

        huesped.actualizar(
                request.nombre(),
                request.apellidos(),
                request.email(),
                request.telefono(),
                request.nacionalidad(),
                TipoDocumento.obtenerTipoDocumentoPorCodigo(request.idTipoDocumento()),
                request.numeroDocumento()
        );

        huespedRepository.saveAndFlush(huesped);

        log.info("Huesped con ID: {} actualizado correctamente", id);

        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public void eliminar(Long id) {
        Huesped huesped = obtenerHuespedActivoPorId(id);

        log.info("Elimar huesped con ID: {}", id);

        reservaClient.validarEliminacionHuespedConReserva(id);

        huesped.eliminar();

        huespedRepository.saveAndFlush(huesped);

        log.info("Se eliminó el huesped exitosamente ");
    }

    private Huesped obtenerHuespedActivoPorId(Long id){

        log.info("Obteniendo huesped activo con ID: {}", id);

        return huespedRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Huesped no encontrado con ID: " + id));
    }

    private void validarDatosUnicos(HuespedRequest request){
        log.info("Obteniendo email unico");

        if(huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistro(
                request.email().trim(), EstadoRegistro.ACTIVO))
            throw new ConflictoException(
                    "Ya existe un huesped activo con el Email: " + request.email()
            );

        log.info("Obteniendo telefono unico");

        if(huespedRepository.existsByTelefonoAndEstadoRegistro(
                request.telefono().trim(), EstadoRegistro.ACTIVO))
            throw new ConflictoException(
                    "Ya existe un huesped activo con el Telefono: " + request.telefono()
            );

        log.info("Obteniendo numero de documento unico");

        if(huespedRepository.existsByNumeroDocumentoIgnoreCaseAndEstadoRegistro(
                request.numeroDocumento().trim(), EstadoRegistro.ACTIVO))
            throw new ConflictoException(
                    "Ya existe un medico activo registrado con el numero de documento: "
                            + request.numeroDocumento()
            );
    }

    private void validarCambiosUnicos(HuespedRequest request, Long id){
        log.info("Validando cambio en email unico");

        if(huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                request.email().trim(), EstadoRegistro.ACTIVO, id))
            throw new ConflictoException(
                    "Ya existe un huesped activo registrado con el Email: " + request.email()
            );

        log.info("Validando cambio en telefono");

        if(huespedRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                request.telefono().trim(), EstadoRegistro.ACTIVO, id))
            throw new ConflictoException(
                    "Ya existe un huesped activo registrado con el telefono: " + request.telefono()
            );

        log.info("Validando cambio en numeor de documento unica");

        if(huespedRepository.existsByNumeroDocumentoIgnoreCaseAndEstadoRegistroAndIdNot(
                request.numeroDocumento().trim(), EstadoRegistro.ACTIVO, id))
            throw new ConflictoException(
                    "Ya existe un medico activo registrado con la cedula profesional: "
                            + request.numeroDocumento()
            );
    }

    private void validarHuespedTieneReservacionAgendada(Long id){
        log.info("Validando si el huesped con ID: {}"+
                "tiene reservacion/es agendadas", id);

        reservaClient.huespedTieneReservasAsignadas(id);
    }

    private void validarEliminacionHuespedConReserva(Long idHuesped){
        log.info("Validando que el huesped no tenga reservas en estado de CONFIRMADA O EN CURSO ");

        reservaClient.validarEliminacionHuespedConReserva(idHuesped);
    }
}
