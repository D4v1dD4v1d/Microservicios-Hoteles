
package com.leonardo.habitacion.entity;

import com.david.commons.enums.EstadoHabitacion;
import com.david.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "HABITACION", schema = "HOTELES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long idHabitacion;

    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    @Column(name = "TIPO", length = 50, nullable = false)
    private String tipo;

    @JdbcTypeCode(SqlTypes.NUMERIC)
    @Column(name = "PRECIO", precision = 10, scale = 2, nullable = false)
    private Double precio;

    @Column(name = "CAPACIDAD", nullable = false)
    private Short capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 30, nullable = false)
    private EstadoHabitacion estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", length = 30, nullable = false)
    private EstadoRegistro estadoRegistro;
}
