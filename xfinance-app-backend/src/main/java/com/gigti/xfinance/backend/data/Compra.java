package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="compras")
public class Compra extends AbstractEntity {
    @NotEmpty
    @Column(name="numero_factura",unique = true)
    private String numeroFactura;

    @Column(name="numero_factura_interno", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long numeroFacturaInterno;

    private String descripcion;

    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @NotNull
    @Column(name = "fecha_compra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCompra;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Usuario usuario;

    @OneToMany(mappedBy = "compra",fetch = FetchType.EAGER)
    private List<CompraItem> items;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @Transient
    private BigDecimal totalFactura;

    private BigDecimal descuentoFactura;

    private String proveedor;

    private String telefonoProveedor;

    private String direccionProveedor;


    public LocalDate getFechaCompraLD(){
        return fechaCompra != null ? fechaCompra.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate() : null;
    }

    public void setFechaCompraLD(LocalDate fechaCom) {
        this.fechaCompra = java.util.Date.from(fechaCom.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public LocalDate getFechaCreacionLD(){
        return fechaCreacion != null ? fechaCreacion.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate() : null;
    }

    public void setFechaCreacionLD(LocalDate fechaCre) {
        this.fechaCreacion = java.util.Date.from(fechaCre.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
