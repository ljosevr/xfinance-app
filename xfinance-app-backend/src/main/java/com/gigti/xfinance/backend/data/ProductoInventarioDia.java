/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@ToString
@Table(name = "producto_inventario_dia")
public class ProductoInventarioDia extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Producto producto;

    private double quantity;

    @Temporal(TemporalType.DATE)
    private Date trackingDate;

    public ProductoInventarioDia() {
    }

    public ProductoInventarioDia(Producto producto, double quantity, Date trackingDate) {
        this.producto = producto;
        this.quantity = quantity;
        this.trackingDate = trackingDate;
    }
}
