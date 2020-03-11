/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productos_inventario")
public class ProductoInventario extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Producto producto;

    private BigDecimal quantity;

    private boolean infinite;

    @Column(name="precio_costo")
    @NotNull
    private BigDecimal precioCosto;

    @Column(name="precio_venta")
    @NotNull
    private BigDecimal precioVenta;

    private boolean inicial;

    private boolean activo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date trackingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Usuario usuario;

    private String descripcion;
}
