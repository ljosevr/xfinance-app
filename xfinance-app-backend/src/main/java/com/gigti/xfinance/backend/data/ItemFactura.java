/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import javax.persistence.*;

@Entity
@Table(name = "items_factura")
public class ItemFactura extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Producto producto;

    private Integer cantidad;

    private Double precioVenta;

    private Double precioCosto;

    @Transient
    private Double precioTotalCosto;

    @Transient
    private Double precioTotalVenta;

    public ItemFactura() {
    }

    public ItemFactura(Factura factura, Producto producto, Integer cantidad, Double precioVenta, Double precioCosto) {
        this.factura = factura;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
    }
}
