/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import com.gigti.xfinance.backend.data.enums.TipoMedidaEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
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
    @Enumerated(EnumType.STRING)
    private TipoMedidaEnum tipoMedida = TipoMedidaEnum.UNIDAD;

    @Transient
    private String activoS;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private CategoriaProducto categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private InventarioActualCosto inventario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Impuesto impuesto;

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
                  ", empresa=" + empresa +
                ", categoria=" + categoria +
                 '}';
    }

}
