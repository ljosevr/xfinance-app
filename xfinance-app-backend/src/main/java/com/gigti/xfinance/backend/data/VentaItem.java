/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

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
@Table(name = "ventas_items")
public class VentaItem extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Producto producto;

    private double cantidad;

    private BigDecimal precioVenta;

    private BigDecimal precioCosto;

    private BigDecimal descuentoArticulo;

    private BigDecimal impuestoArticulo;

    @Min(value = 1)
    private int item;

    @Transient
    private BigDecimal precioTotalCosto;

    @Transient
    private BigDecimal precioTotalVenta;

    public VentaItem(Venta venta, Producto producto, double cantidad, BigDecimal precioVenta, BigDecimal precioCosto, int item) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
        this.item = item;
    }
}
