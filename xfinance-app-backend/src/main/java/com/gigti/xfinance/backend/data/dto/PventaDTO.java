package com.gigti.xfinance.backend.data.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PventaDTO implements Serializable {

    private String idProducto;
    private String nombreProducto;
    private String codigoBarra;
    private BigDecimal precioCostoActual;
    private BigDecimal precioVentaActual;
    private int cantidadVenta;

    public PventaDTO() {
    }

    public PventaDTO(String idProducto, String nombreProducto, String codigoBarra, BigDecimal precioCostoActual, BigDecimal precioVentaActual, int cantidadVenta) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.codigoBarra = codigoBarra;
        this.precioCostoActual = precioCostoActual;
        this.precioVentaActual = precioVentaActual;
        this.cantidadVenta = cantidadVenta;
    }
}
