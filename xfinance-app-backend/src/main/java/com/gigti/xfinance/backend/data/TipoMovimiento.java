package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipos_movimientos")
public class TipoMovimiento implements Serializable {

    public static final TipoMovimiento COMPRA = new TipoMovimiento(1,"COMPRA","Cuando se Realiza una Compra de Productos");
    public static final TipoMovimiento VENTA = new TipoMovimiento(2,"VENTA","Cuando se Realiza una Venta de Productos");
    public static final TipoMovimiento INICIAL = new TipoMovimiento(3,"INICIAL","Inventario Inicial");
    public static final TipoMovimiento TRASLADO = new TipoMovimiento(4,"TRASLADO","Traslado entre productos paquete o caja a Unidad");

    @Id
    private Integer id;
    private String nombre;
    private String descripcion;
}
