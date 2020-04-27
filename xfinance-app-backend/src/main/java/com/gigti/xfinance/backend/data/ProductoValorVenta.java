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
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "producto_valor_venta")
public class ProductoValorVenta extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Producto producto;

    @Column(name="valor_venta")
    @NotNull
    private BigDecimal valorVenta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    private boolean activo;
}
