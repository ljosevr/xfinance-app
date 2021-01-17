/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import com.gigti.xfinance.backend.others.AllUtils;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    private boolean manageStock;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Usuario usuario;

    private BigDecimal precioCosto;

    private BigDecimal precioVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Impuesto impuesto;

    private boolean definitivo;

    @Transient
    private String definitivoS = "NO";

    @Transient
    private String manageStockS = "NO";

    @Transient
    private String fechaS;

    @Transient
    private String precioCostoS;

    @Transient
    private String precioVentaS;

}
