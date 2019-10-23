/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "producto_ventas")
public class ProductoVenta extends AbstractEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Producto producto;

    private double quantitySell;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sellDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Usuario usuario;

    public ProductoVenta() {
    }

    public ProductoVenta(Producto producto, Integer quantitySell, Date sellDate, Usuario usuario) {
        this.producto = producto;
        this.quantitySell = quantitySell;
        this.sellDate = sellDate;
        this.usuario = usuario;
    }
}
