/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "producto_valores")
public class ProductoValores extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Producto producto;

    @Column(name="precio_costo")
    @NotNull
    private BigDecimal precioCosto;

    @Column(name="precio_venta")
    @NotNull
    private BigDecimal precioVenta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActivacion;

    private Boolean activo;

    private Float impuestos;

    public ProductoValores() {
    }

    public ProductoValores(Producto producto, @NotNull @Size(message = "Precio de Costo No puede ser menor a Cero (0)") BigDecimal precioCosto, @NotNull @Size(message = "Precio de Venta No puede ser menor a Cero (0)") BigDecimal precioVenta, Date fechaActivacion, Boolean activo, Float impuestos) {
        this.producto = producto;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.fechaActivacion = fechaActivacion;
        this.activo = activo;
        this.impuestos = impuestos;
    }
}
