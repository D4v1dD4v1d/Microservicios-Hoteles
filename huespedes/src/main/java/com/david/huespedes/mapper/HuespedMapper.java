package com.david.huespedes.mapper;

import com.david.commons.dto.huespedes.HuespedRequest;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.mapper.CommonMapper;
import com.david.commons.enums.TipoDocumento;
import com.david.huespedes.entity.Huesped;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper implements CommonMapper<HuespedRequest, Huesped, HuespedResponse> {

    @Override
    public Huesped requestAEntidad(HuespedRequest request) {
        return request == null
                ? null
                : Huesped.crear(
                request.nombre(),
                request.apellidos(),
                request.email(),
                request.telefono(),
                request.nacionalidad(),
                request.numeroDocumento(),
                TipoDocumento.obtenerTipoDocumentoPorCodigo(request.idTipoDocumento())
                );
    }

    @Override
    public HuespedResponse entidadAResponse(Huesped entidad) {
        return entidad == null
                ? null
                : new HuespedResponse(
                        entidad.getId(),
                        entidad.getNombre(),
                        entidad.getEmail(),
                        entidad.getTelefono(),
                        entidad.getTipoDocumento().getDescripcion(),
                        entidad.getNumeroDocumento(),
                        entidad.getNacionalidad(),
                        entidad.getEstadoRegistro().name(),
                        entidad.getApellidos());
    }
}
