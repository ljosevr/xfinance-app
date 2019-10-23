/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "items_factura")
public class ItemFactura extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Producto producto;

    private double cantidad;

    private BigDecimal precioVenta;

    private BigDecimal precioCosto;

    @Transient
    private BigDecimal precioTotalCosto;

    @Transient
    private BigDecimal precioTotalVenta;

    public ItemFactura() {
    }

    public ItemFactura(Factura factura, Producto producto, double cantidad, BigDecimal precioVenta, BigDecimal precioCosto) {
        this.factura = factura;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
    }
}
