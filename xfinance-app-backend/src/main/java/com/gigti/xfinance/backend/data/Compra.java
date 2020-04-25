package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name="compras")
public class Compra extends AbstractEntity {
    @NotEmpty
    @Column(name="numero_factura",unique = true)
    private String numeroFactura;

    @Column(name="numero_factura_interno", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long numeroFacturaInterno;

    private String descripcion;

    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Usuario usuario;

    @OneToMany(mappedBy = "compra",fetch = FetchType.EAGER)
    private List<CompraItem> items;

    @Transient
    private BigDecimal totalFactura;

    private BigDecimal descuentoFactura;

    private String proveedor;

    private String telefonoProveedor;

    private boolean inicial = false;
}
