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
    @Column(name = "codigo_barra")
    private String codigoBarra;
    private String descripcion;
    @NotNull
    private boolean activo;
    @NotNull
    private boolean eliminado;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private TipoMedida tipoMedida;

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

    @Transient
    private BigDecimal cantidadInicial;

    @Transient
    private BigDecimal precioCosto;

    @Transient
    private BigDecimal precioVenta;

    @Transient
    private boolean controlarStock;

    @Transient
    private boolean manageInitialStock;

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

//    @Override
//    public String toString() {
//        return "Producto{" +
//                "nombreProducto='"  + nombreProducto + '\'' +
//                ", codigoBarra='"   + codigoBarra + '\'' +
//                ", descripcion='"   + descripcion + '\'' +
//                ", activo="         + activo + '\'' +
//                ", eliminado="      + eliminado + '\'' +
//                ", tipoMedida="     + tipoMedida.getSimbolo() + '\'' +
//                ", activoS='"       + activoS + '\'' +
//                ", empresa="        + empresa.getNombreEmpresa() + '\'' +
//                ", categoria="      + categoria.getNombre() +
//                 '}';
//    }

}
