package com.leonardo.habitacion.service;

import com.leonardo.habitacion.dto.HabitacionRequest;
import com.leonardo.habitacion.dto.HabitacionResponse;
import com.leonardo.habitacion.entity.Habitacion;
import com.leonardo.habitacion.enums.EstadoRegistro;
import com.leonardo.habitacion.mapper.HabitacionMapper;
import com.leonardo.habitacion.repository.HabitacionRepository;
import com.leonardo.habitacion.service.HabitacionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional
    public HabitacionResponse registrar(HabitacionRequest request) {

        log.info("Registrando habitación número: {}", request.numero());

        validarNumeroDuplicado(request.numero());

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);

        return habitacionMapper.entidadAResponse(
                habitacionRepository.save(habitacion)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {

        log.info("Listando habitaciones activas");

        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(habitacionMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse buscarPorId(Long idHabitacion) {

        log.info("Buscando habitación con ID: {}", idHabitacion);

        return habitacionMapper.entidadAResponse(
                obtenerHabitacionActiva(idHabitacion)
        );
    }

    @Override
    @Transactional
    public HabitacionResponse actualizar(Long idHabitacion, HabitacionRequest request) {

        log.info("Actualizando habitación con ID: {}", idHabitacion);

        Habitacion habitacion = obtenerHabitacionActiva(idHabitacion);

        if (!habitacion.getNumero().equals(request.numero())) {
            validarNumeroDuplicado(request.numero());
        }

        habitacionMapper.actualizarEntidad(request, habitacion);

        return habitacionMapper.entidadAResponse(
                habitacionRepository.save(habitacion)
        );
    }

    @Override
    @Transactional
    public void eliminar(Long idHabitacion) {

        log.info("Eliminando habitación con ID: {}", idHabitacion);

        Habitacion habitacion = obtenerHabitacionActiva(idHabitacion);

        habitacion.setEstadoRegistro(EstadoRegistro.ELIMINADO);

        habitacionRepository.save(habitacion);
    }

    // Obtener una habitación activa
    private Habitacion obtenerHabitacionActiva(Long idHabitacion) {

        return habitacionRepository
                .findByIdHabitacionAndEstadoRegistro(
                        idHabitacion,
                        EstadoRegistro.ACTIVO
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Habitación no encontrada con ID: " + idHabitacion
                ));
    }

    // Validar número duplicado entre habitaciones activas
    private void validarNumeroDuplicado(Integer numero) {

        if (habitacionRepository.existsByNumeroAndEstadoRegistro(
                numero,
                EstadoRegistro.ACTIVO)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una habitación activa con el número: " + numero
            );
        }
    }
}

