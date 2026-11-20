package com.leonardo.habitacion.service;

import com.david.commons.clients.ReservaClient;
import com.david.commons.dto.habitaciones.HabitacionRequest;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.enums.EstadoHabitacion;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.enums.TipoHabitacion;
import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.DatoInvalidoException;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import com.david.commons.exceptions.ServicioNoDisponibleException;
import com.leonardo.habitacion.entity.Habitacion;
import com.leonardo.habitacion.mapper.HabitacionMapper;
import com.leonardo.habitacion.repository.HabitacionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;
    private final ReservaClient reservaClient;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> lista() {
        log.info("Listando todas las habitaciones activas...");
        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(habitacionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacionSinValidarEstado(Long id) {
        log.info("Buscando habitación sin validar estado con id {}", id);
        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Habitación no encontrada con id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        return habitacionMapper.entidadAResponse(obtenerHabitacionActivaPorId(id));
    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        log.info("Registrando nueva habitación con número: {}", request.numero());
        validarDatosUnicos(request);
        Habitacion habitacion = habitacionMapper.requestAEntidad(request);
        habitacionRepository.saveAndFlush(habitacion);
        log.info("Nueva habitación registrada correctamente con ID: {}", habitacion.getId());
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerHabitacionActivaPorId(id);
        validarCambiosUnicos(request, id);

        // Notion 4.3: la capacidad no puede ser menor que numeroPersonas de la reserva vigente
        // Solo una habitación OCUPADA puede tener reserva vigente
        if (habitacion.getEstadoHabitacion() == EstadoHabitacion.OCUPADA) {
            Integer personas = obtenerPersonasReservaVigente(id);
            if (request.capacidad() < personas) {
                throw new ConflictoException("La capacidad no puede ser menor que el número de personas de la reserva vigente ("
                        + personas + ")");
            }
        }

        log.info("Actualizando habitación con id: {}", id);
        // El PUT ignora el estado: solo cambia número, precio, capacidad y tipo (Notion 6.3)
        habitacion.actualizar(request.numero(), request.precio(), request.capacidad(), TipoHabitacion.obtenerTipoHabitacionPorDescripcion(request.tipo()));
        habitacionRepository.saveAndFlush(habitacion);
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        Habitacion habitacion = obtenerHabitacionActivaPorId(id);
        log.info("Eliminando lógicamente la habitación con id: {}", id);
        habitacion.eliminar(); // lanza 409 si está OCUPADA
        habitacionRepository.saveAndFlush(habitacion);
    }


    @Override
    public HabitacionResponse cambiarEstado(Long id, Long idEstado) {
        EstadoHabitacion nuevo = obtenerEstado(idEstado);           // fuera de 1-4 -> 400
        Habitacion habitacion = obtenerHabitacionActivaPorId(id);   // no existe -> 404
        EstadoHabitacion actual = habitacion.getEstadoHabitacion();
        log.info("Cambio manual de habitación {}: {} -> {}", id, actual, nuevo);

        if (actual == EstadoHabitacion.OCUPADA) {
            throw new ConflictoException("Una habitación OCUPADA no puede cambiar de estado manualmente");
        }
        if (nuevo == EstadoHabitacion.OCUPADA) {
            throw new ConflictoException("No se puede cambiar manualmente una habitación a OCUPADA");
        }
        if (nuevo == actual) {
            throw new ConflictoException("La habitación ya se encuentra en " + actual);
        }
        // Lo que queda son movimientos entre DISPONIBLE, LIMPIEZA y MANTENIMIENTO: todos permitidos

        habitacion.cambiarEstado(nuevo);
        habitacionRepository.saveAndFlush(habitacion);
        return habitacionMapper.entidadAResponse(habitacion);
    }

    /* ================= CAMBIO AUTOMÁTICO (desde Reservas) ================= */

    @Override
    public HabitacionResponse cambiarEstadoInterno(Long id, Long idEstado) {
        EstadoHabitacion nuevo = obtenerEstado(idEstado);
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación no encontrada con id: " + id));
        if (habitacion.getEstadoRegistro() == EstadoRegistro.ELIMINADO)
            throw new ConflictoException("La habitación no está activa");
        EstadoHabitacion actual = habitacion.getEstadoHabitacion();
        log.info("Cambio automático de habitación {}: {} -> {}", id, actual, nuevo);

        boolean valida =
                (actual == EstadoHabitacion.DISPONIBLE && nuevo == EstadoHabitacion.OCUPADA)    // crear reserva
                        || (actual == EstadoHabitacion.OCUPADA && nuevo == EstadoHabitacion.LIMPIEZA)      // check-out
                        || (actual == EstadoHabitacion.OCUPADA && nuevo == EstadoHabitacion.DISPONIBLE)    // cancelación / compensación
                        || (actual == EstadoHabitacion.LIMPIEZA && nuevo == EstadoHabitacion.OCUPADA)
                        || (actual.equals(nuevo));     // compensación de check-out

        if (!valida) {
            throw new ConflictoException("Transición no permitida: " + actual + " -> " + nuevo);
        }

        habitacion.cambiarEstado(nuevo);
        habitacionRepository.saveAndFlush(habitacion);
        return habitacionMapper.entidadAResponse(habitacion);
    }


    private Habitacion obtenerHabitacionActivaPorId(Long id) {
        log.info("Buscando habitación activa con id {}", id);
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación activa no encontrada con id: " + id));
    }

    // idEstado fuera de catálogo -> 400 (Notion 5.4)
    private EstadoHabitacion obtenerEstado(Long idEstado) {
        EstadoHabitacion estado = idEstado == null ? null : EstadoHabitacion.fromId(idEstado);
        if (estado == null) {
            throw new DatoInvalidoException("Estado inválido: " + idEstado + ". Debe ser de 1 a 4");
        }
        return estado;
    }

    private Integer obtenerPersonasReservaVigente(Long idHabitacion) {
        try {
            return reservaClient.personasReservaVigente(idHabitacion);
        } catch (FeignException e) {
            throw new ServicioNoDisponibleException("El servicio de reservas no está disponible"); // 503
        }
    }

    private void validarDatosUnicos(HabitacionRequest request) {
        if (habitacionRepository.existsByNumeroAndEstadoRegistro(request.numero(), EstadoRegistro.ACTIVO)) {
            throw new ConflictoException("Ya existe una habitación activa registrada con el número: " + request.numero());
        }
    }

    private void validarCambiosUnicos(HabitacionRequest request, Long id) {
        if (habitacionRepository.existsByNumeroAndEstadoRegistroAndIdNot(request.numero(), EstadoRegistro.ACTIVO, id)) {
            throw new ConflictoException("Ya existe otra habitación activa registrada con el número: " + request.numero());
        }
    }
}