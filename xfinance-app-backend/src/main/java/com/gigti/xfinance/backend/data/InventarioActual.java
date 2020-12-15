package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventario_actual")
public class InventarioActual extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Producto producto;

    private BigDecimal cantidad;

    private boolean manageStock;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;
}
