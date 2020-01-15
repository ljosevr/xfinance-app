/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import com.gigti.xfinance.backend.TipoMedidaEnum;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "productos")
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

    @NotNull
    private TipoMedidaEnum tipoMedida = TipoMedidaEnum.UNIDAD;
//    @NotNull
//    private Integer cantidadMedida = 1;

    @Transient
    private String activoS;

    @Transient
    private BigDecimal precioCostoActual = BigDecimal.ZERO;

    @Transient
    private BigDecimal precioVentaActual = BigDecimal.ZERO;

    @Transient
    private double stockActual;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private CategoriaProducto categoria;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoValores> productoValores;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoInventarioInicio> productoInventarioInicio;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoInventarioDia> productsStockDay;

    public Producto(){}

    public Producto(String nombreProducto, String codigoBarra, String descripcion, boolean activo, Empresa empresa, TipoMedidaEnum tipoMedida) {
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

    @Override
    public String toString() {
        return "Producto{" +
                "nombreProducto='" + nombreProducto + '\'' +
                ", codigoBarra='" + codigoBarra + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", activo=" + activo +
                ", eliminado=" + eliminado +
                ", tipoMedida=" + tipoMedida +
                ", activoS='" + activoS + '\'' +
                ", precioCostoActual=" + precioCostoActual +
                ", precioVentaActual=" + precioVentaActual +
                ", stockActual=" + stockActual +
                ", empresa=" + empresa +
                ", categoria=" + categoria +
                 '}';
    }
}
