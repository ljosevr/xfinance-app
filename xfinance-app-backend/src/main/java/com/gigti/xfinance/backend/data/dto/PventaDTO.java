package com.gigti.xfinance.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
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
    private BigDecimal cantidadVenta;
    private BigDecimal inStock;
    private String unidadMedida;
    private String impuestoId;
    private BigDecimal impuestoValor;
    private String impuestoNombre;
    private boolean infinite;
    private BigDecimal descuento;
    private BigDecimal descuentoTotal;

    public BigDecimal getSubTotal(){
        return  precioVentaActual.multiply(cantidadVenta);
    }

}
