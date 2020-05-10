package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventario_actual_costo")
public class InventarioActualCosto extends AbstractEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Producto producto;

    private BigDecimal cantidad;

    @Column(name="precio_costo")
    @NotNull
    private BigDecimal precioCosto;

    private boolean infinite;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    private boolean activo;

}
