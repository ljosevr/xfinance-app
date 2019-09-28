/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    @Transient
    private String activoS;

    @Transient
    private BigDecimal precioCostoActual;

    @Transient
    private BigDecimal precioVentaActual;

    @Transient
    private int stockActual;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private CategoriaProducto categoria;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoValores> productoValores;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductStockStart> productStockStart;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductStockDay> productsStockDay;

    @OneToMany(mappedBy = "producto", cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ProductoVenta> productoVenta;

    public Producto(){}

    public Producto(String nombreProducto, String codigoBarra, String descripcion, boolean activo, Empresa empresa) {
        this.nombreProducto = nombreProducto;
        this.codigoBarra = codigoBarra;
        this.descripcion = descripcion;
        this.activo = activo;
        this.eliminado = false;
        this.empresa = empresa;
    }

    public String getActivoS() {
        return isActivo() ? "SI" : "NO";
    }

    public BigDecimal getPrecioVentaActual(){

        LocalDate maxDate = null;
        ProductoValores pro = null;
//        for(ProductoValores p : getProductsFinance()){
//            if(p.getActive()) {
//                if (maxDate == null) {
//                    maxDate = p.getActiveDate();
//                    pro = p;
//                }else{
//                    if(p.getActiveDate().isAfter(maxDate)){
//                        maxDate = p.getActiveDate();
//                        pro = p;
//                    }
//                }
//            }
//        }
        return BigDecimal.valueOf(0.0);
    }

    public BigDecimal getPrecioCostoActual(){

        LocalDate maxDate = null;
        ProductoValores pro = null;
//        for(ProductoValores p : getProductsFinance()){
//            if(p.getActive()) {
//                if (maxDate == null) {
//                    maxDate = p.getActiveDate();
//                    pro = p;
//                }else{
//                    if(p.getActiveDate().isAfter(maxDate)){
//                        maxDate = p.getActiveDate();
//                        pro = p;
//                    }
//                }
//            }
//        }
        return BigDecimal.valueOf(0.0);
    }

    public int getStockActual(){
        return 0;
    }

}
