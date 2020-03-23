package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movimientos")
public class Movimiento extends AbstractEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Producto producto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private BigDecimal cantidad;

    private BigDecimal precioCosto;

    private BigDecimal precioVenta;

    private BigDecimal impuestoValor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private TipoMovimiento tipoMovimiento;
}
