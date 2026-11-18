package com.leonardo.habitacion.service;

import com.david.commons.clients.ReservaClient;
import com.david.commons.dto.habitaciones.*;
import com.david.commons.enums.EstadoHabitacion;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.DatoInvalidoException;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import com.leonardo.habitacion.entity.Habitacion;
import com.leonardo.habitacion.mapper.HabitacionMapper;
import com.leonardo.habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HabitacionServiceImpl implements HabitacionService {
    private final HabitacionRepository repository;
    private final HabitacionMapper mapper;
    private final ReservaClient reservaClient;

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        validarNumero(request.numero(), null);
        return mapper.entidadAResponse(repository.save(mapper.requestAEntidad(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        return repository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream().map(mapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse buscarPorId(Long idHabitacion) {
        return mapper.entidadAResponse(obtenerActiva(idHabitacion));
    }

    @Override
    @Transactional(readOnly = true)
    public DatosHabitacion consultarInterno(Long idHabitacion) {
        Habitacion h = obtenerActiva(idHabitacion);
        return new DatosHabitacion(h.getIdHabitacion(), h.getNumero(), h.getTipo(),
                h.getPrecio(), h.getCapacidad(), h.getEstado());
    }

    @Override
    public HabitacionResponse actualizar(Long idHabitacion, HabitacionRequest request) {
        Habitacion habitacion = obtenerActiva(idHabitacion);
        validarNumero(request.numero(), idHabitacion);
        // No reducir la capacidad por debajo de una reserva vigente.
        if (request.capacidad() < habitacion.getCapacidad()) {
            RestriccionesReservaHabitacion restricciones = reservaClient.obtenerRestricciones(idHabitacion);
            if (restricciones != null && restricciones.maximoPersonasVigentes() != null
                    && request.capacidad() < restricciones.maximoPersonasVigentes()) {
                throw new ConflictoException("La capacidad no puede ser inferior a las personas de una reserva vigente");
            }
        }
        mapper.actualizarEntidad(request, habitacion); // PUT no cambia estado ni estadoRegistro.
        return mapper.entidadAResponse(repository.save(habitacion));
    }

    @Override
    public void cambiarEstadoManual(Long idHabitacion, Long idEstado) {
        Habitacion habitacion = obtenerActiva(idHabitacion);
        EstadoHabitacion nuevo = estadoPorCodigo(idEstado);
        EstadoHabitacion actual = habitacion.getEstado();
        if (actual == EstadoHabitacion.OCUPADA || nuevo == EstadoHabitacion.OCUPADA || actual == nuevo) {
            log.info("Estamos aqui en el if");
            throw new ConflictoException("Transición manual de estado no permitida: " + actual + " -> " + nuevo);
        }
        // DISPONIBLE/LIMPIEZA/MANTENIMIENTO pueden alternar entre sí, sin repetirse.
        habitacion.setEstado(nuevo);
        repository.save(habitacion);
    }

    @Override
    public void cambiarEstadoAutomatico(Long idHabitacion, EstadoHabitacion nuevoEstado) {
        // Este método se invoca exclusivamente durante la orquestación interna de Reservas.
        Habitacion h = obtenerActiva(idHabitacion);
        if (nuevoEstado == null) throw new DatoInvalidoException("El estado es obligatorio");
        boolean valido = switch (h.getEstado()) {
            case DISPONIBLE -> nuevoEstado == EstadoHabitacion.OCUPADA;
            case OCUPADA -> nuevoEstado == EstadoHabitacion.LIMPIEZA || nuevoEstado == EstadoHabitacion.DISPONIBLE;
            default -> false;
        };
        if (!valido) throw new ConflictoException("Transición automática no permitida");
        h.setEstado(nuevoEstado);
        repository.save(h);
    }

    @Override
    public void eliminar(Long idHabitacion) {
        Habitacion habitacion = obtenerActiva(idHabitacion);
        if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
            throw new ConflictoException("No se puede eliminar una habitación OCUPADA");
        }
        RestriccionesReservaHabitacion restricciones = reservaClient.obtenerRestricciones(idHabitacion);
        if (restricciones != null && restricciones.tieneReservasVigentes()) {
            throw new ConflictoException("No se puede eliminar una habitación con reservas vigentes");
        }
        habitacion.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        repository.save(habitacion);
    }

    private Habitacion obtenerActiva(Long idHabitacion) {
        if (idHabitacion == null || idHabitacion <= 0) {
            throw new DatoInvalidoException("El identificador de habitación debe ser positivo");
        }
        return repository.findByIdHabitacionAndEstadoRegistro(idHabitacion, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación no encontrada: " + idHabitacion));
    }

    private void validarNumero(Integer numero, Long idActual) {
        boolean duplicado = idActual == null
                ? repository.existsByNumeroAndEstadoRegistro(numero, EstadoRegistro.ACTIVO)
                : repository.existsByNumeroAndEstadoRegistroAndIdHabitacionNot(numero, EstadoRegistro.ACTIVO, idActual);
        if (duplicado) throw new ConflictoException("Ya existe una habitación ACTIVA con número: " + numero);
    }

    private EstadoHabitacion estadoPorCodigo(Long idEstado) {
        if (idEstado == null) throw new DatoInvalidoException("El estado es requerido");
        return switch (idEstado.toString()) {
            case "1" -> EstadoHabitacion.DISPONIBLE;
            case "2" -> EstadoHabitacion.OCUPADA;
            case "3" -> EstadoHabitacion.LIMPIEZA;
            case "4" -> EstadoHabitacion.MANTENIMIENTO;
            default -> throw new DatoInvalidoException("Código de estado inválido: " + idEstado);
        };
    }
}
