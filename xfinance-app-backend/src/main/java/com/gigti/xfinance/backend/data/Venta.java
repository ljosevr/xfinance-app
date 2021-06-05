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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ventas",
        uniqueConstraints={@UniqueConstraint(columnNames = {"numero_factura" , "empresa_id"})})
public class Venta extends AbstractEntity {

    @NotEmpty
    @Column(name="numero_factura")
    private String numeroFactura;

    @Column(name="numero_factura_interno",  updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long numeroFacturaInterno;

    private String descripcion;

    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @NotNull
    @Column(name = "fecha_venta_efectiva")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVentaEfectiva;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Usuario usuario;

    @OneToMany(mappedBy = "venta",fetch = FetchType.LAZY)
    private List<VentaItem> items;

    @Transient
    private BigDecimal totalVenta;

    @Transient
    private BigDecimal totalCosto;

    private BigDecimal descuentoFactura;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Cliente cliente;

    public Venta(String numeroFactura, long numeroFacturaInterno, String descripcion, Date fechaCreacion, Usuario usuario) {
        this.numeroFactura = numeroFactura;
        this.numeroFacturaInterno = numeroFacturaInterno;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }
}
