package com.gigti.xfinance.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PventaDTO implements Serializable {

    private int item;
    private String idProducto;
    private String nombreProducto;
    private String codigoBarra;
    private BigDecimal precioCostoActual;
    private BigDecimal precioVentaActual;
    private double cantidadVenta;
    private double inStock;

    public BigDecimal getSubTotal(){
        return  precioVentaActual.multiply(BigDecimal.valueOf(cantidadVenta));
    }

}
