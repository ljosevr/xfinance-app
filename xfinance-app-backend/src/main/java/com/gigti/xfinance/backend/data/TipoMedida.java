package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "tipos_medidas",
        uniqueConstraints={@UniqueConstraint(columnNames = {"nombre" , "simbolo", "eliminado", "empresa_id"})})
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TipoMedida extends AbstractEntity {
    @Size(min = 2, message = "Tipo Medida debe tener minimo 2 Caracteres")
    @NotNull
    private String nombre;

    @NotNull
    private String simbolo;

    private String descripcion;

    @NotNull
    private boolean activo;

    @NotNull
    private boolean eliminado;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @Transient
    private String activoS;

}
