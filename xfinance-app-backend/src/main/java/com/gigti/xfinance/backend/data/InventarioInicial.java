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
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventario_inicial")
public class InventarioInicial extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Producto producto;

    private BigDecimal cantidad;

    private boolean infinite;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Usuario usuario;

    @Transient
    private BigDecimal precioCosto;

    @Transient
    private BigDecimal precioVenta;

    @Transient
    private Impuesto impuesto;
}
