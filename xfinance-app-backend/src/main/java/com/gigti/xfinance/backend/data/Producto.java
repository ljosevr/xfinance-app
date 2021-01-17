/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "productos")
@ToString
public class Producto extends AbstractEntity {

    @NotNull
    @Column(name = "nombre_producto")
    private String nombreProducto;
    @ToString.Exclude
    @Column(name = "codigo_barra")
    private String codigoBarra;
    @ToString.Exclude
    private String descripcion;
    @NotNull
    private boolean activo;
    @ToString.Exclude
    @NotNull
    private boolean eliminado;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private TipoMedida tipoMedida;

    @ToString.Exclude
    @Transient
    private String activoS;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private CategoriaProducto categoria;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private InventarioActualCosto inventario;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Impuesto impuesto;

    @ToString.Exclude
    @Transient
    private BigDecimal cantidadInicial;

    @ToString.Exclude
    @Transient
    private BigDecimal precioCosto;

    @ToString.Exclude
    @Transient
    private BigDecimal precioVenta;

    @ToString.Exclude
    @Transient
    private boolean manageStock ;

    @ToString.Exclude
    @Transient
    private boolean manageInitialStock;

    @ToString.Exclude
    @Transient
    private boolean inventarioDefinitivo;

    public Producto(){}

    public Producto(String nombreProducto, String codigoBarra, String descripcion, boolean activo, Empresa empresa, TipoMedida tipoMedida) {
        this.nombreProducto = nombreProducto;
        this.codigoBarra = codigoBarra;
        this.descripcion = descripcion;
        this.activo = activo;
        this.eliminado = false;
        this.empresa = empresa;
        this.tipoMedida = tipoMedida;
    }

    public String getActivoS() {
        return isActivo() ? "SI" : "NO";
    }

}
