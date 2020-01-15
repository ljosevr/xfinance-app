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

@Data
@Entity
@ToString
@Table(name = "producto_inventario_inicio")
public class ProductoInventarioInicio extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Producto producto;

    private double quantity;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Usuario usuario;

    public ProductoInventarioInicio(Producto producto, double quantity, Date startDate, Usuario usuario) {
        this.producto = producto;
        this.quantity = quantity;
        this.startDate = startDate;
        this.usuario = usuario;
    }

    public ProductoInventarioInicio() {
    }
}
