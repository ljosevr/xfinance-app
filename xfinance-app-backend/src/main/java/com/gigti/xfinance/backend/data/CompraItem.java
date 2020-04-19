package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "compra_items")
public class CompraItem extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Compra compra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Producto producto;

    private BigDecimal cantidad;

    private BigDecimal precioCosto;

    private BigDecimal precioVenta;

    private BigDecimal descuentoArticulo;

    private BigDecimal impuestoArticulo;

    @Min(value = 1)
    private int item;

    @Transient
    private BigDecimal precioTotalCosto;

    @Transient
    private BigDecimal precioTotalVenta;

}
