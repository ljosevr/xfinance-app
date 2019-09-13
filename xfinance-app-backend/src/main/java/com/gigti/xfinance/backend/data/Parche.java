package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Table(name = "parches")
public class Parche extends AbstractEntity {

    @NotNull
    @Size(min = 4, max = 255)
    private String nombre;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEjecucion;

    private boolean estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Empresa empresa;

    public Parche() {
    }

    public Parche(@NotNull @Size(min = 4, max = 255) String nombre, @NotNull Date fechaEjecucion, boolean estado, Empresa empresa) {
        this.nombre = nombre;
        this.fechaEjecucion = fechaEjecucion;
        this.estado = estado;
        this.empresa = empresa;
    }
}
