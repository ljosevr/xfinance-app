package com.gigti.xfinance.backend.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "impuestos")
public class Impuesto extends AbstractEntity {

    private String nombre = "";
    private BigDecimal valor;
    private String descripcion;

    @NotNull
    private boolean activo;

    @NotNull
    private boolean eliminado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @Transient
    private String activoS;
}
