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
@Table(name="compras",
        uniqueConstraints={@UniqueConstraint(columnNames = {"numero_factura" , "empresa_id"})})
public class Compra extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @NotEmpty
    @Column(name="numero_factura")
    private String numeroFactura;

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

    @OneToMany(mappedBy = "compra", fetch = FetchType.EAGER)
    private List<CompraItem> items;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    private BigDecimal totalFactura;

    private BigDecimal descuentoFactura;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Proveedor proveedor;

    private String imageId;

    private String imageUrl;

    //TODO
    //private boolean eliminado;

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
