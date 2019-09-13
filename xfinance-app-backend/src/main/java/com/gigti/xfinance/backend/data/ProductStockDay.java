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
@Table(name = "product_stock_day")
public class ProductStockDay extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Producto producto;

    private Integer quantity;
    @Temporal(TemporalType.DATE)
    private Date trackingDate;

    public ProductStockDay() {
    }

    public ProductStockDay(Producto producto, Integer quantity, Date trackingDate) {
        this.producto = producto;
        this.quantity = quantity;
        this.trackingDate = trackingDate;
    }
}
