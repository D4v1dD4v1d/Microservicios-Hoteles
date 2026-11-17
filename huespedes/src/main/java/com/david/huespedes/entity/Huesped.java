package com.david.huespedes.entity;

import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.DatoInvalidoException;
import com.david.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.enums.TipoDocumento;

@Entity
@Table(name = "HUESPED")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Huesped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HUESPED")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO", length = 50, nullable = false)
    private String apellidos;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DOCUMENTO", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "NUMERO_DOCUMENTO", nullable = false)
    private String numeroDocumento;

    @Column(name = "NACIONALIDAD", nullable = false)
    private String nacionalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public String obtenerNombreCompleto(){
        return String.join(
                " ",
                this.getNombre(),
                this.getApellidos());
    }

    public static void validarDatos(String nombre, String apellidos, String email,
                                    String telefono, String nacionalidad,
                                    String numeroDocumento) {

        StringCustomUtils.validarTamanio(nombre, 2, 50,
                "El nombre es requerido y debe tener entre 2 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidos, 2, 50,
                "Los apellidos son requeridos y deben tener entre 2 y 50 caracteres");

        StringCustomUtils.validarTamanio(email, 1, 50,
                "El email es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(nacionalidad, 2, 50,
                "La nacionalidad es obligatoria y debe contener entre 2 y 50 caracteres");

        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El telefono es requerido y debe tener 10 caracteres");

        StringCustomUtils.validarTamanio(numeroDocumento, 8, 20,
                "El número de documento es obligatorio y debe contener entre 8 y 20 caracteres");
    }

    public void validarNoEliminado(){
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new ConflictoException("El huesped ya esta eliminado");
    }

    public void actualizarTipoDocumento(TipoDocumento tipoDocumento){
        validarNoEliminado();

        if(tipoDocumento == null)
            throw new DatoInvalidoException("El estado de registro es requerida");

        this.tipoDocumento = tipoDocumento;
    }

    public void actualizar(String nombre,String apellidos, String email,String telefono,
                           String nacionalidad, TipoDocumento tipoDocumento, String numeroDocumento){


        validarDatos(nombre, apellidos, email ,telefono,
                nacionalidad, numeroDocumento);

        this.nombre = nombre.trim();
        this.apellidos = apellidos.trim();
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.nacionalidad = nacionalidad.trim();
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento.trim();
    }

    public static Huesped crear(String nombre,String apellidos, String email,String telefono,
                           TipoDocumento tipoDocumento, String nacionalidad, String numeroDocumento) {


        validarDatos(nombre, apellidos, email, telefono,
                nacionalidad, numeroDocumento);

        return Huesped.builder()
                .nombre(nombre.trim())
                .apellidos(apellidos.trim())
                .email(email.trim().toLowerCase())
                .nacionalidad(nacionalidad.trim())
                .tipoDocumento(tipoDocumento)
                .telefono(telefono.trim())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .numeroDocumento(numeroDocumento.trim())
                .build();
    }

    public void eliminar() {
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

}
