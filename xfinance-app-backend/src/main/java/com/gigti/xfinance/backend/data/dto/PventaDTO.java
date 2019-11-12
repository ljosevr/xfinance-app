package com.gigti.xfinance.backend.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = {"item", "idProducto"}, callSuper = false)
public class PventaDTO implements Serializable {

    private int item;
    private String idProducto;
    private String nombreProducto;
    private String codigoBarra;
    private BigDecimal precioCostoActual;
    private BigDecimal precioVentaActual;
    private double cantidadVenta;

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

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PventaDTO pventaDTO = (PventaDTO) o;
        return item == pventaDTO.item &&
                Double.compare(pventaDTO.cantidadVenta, cantidadVenta) == 0 &&
                Objects.equals(idProducto, pventaDTO.idProducto) &&
                Objects.equals(nombreProducto, pventaDTO.nombreProducto) &&
                Objects.equals(codigoBarra, pventaDTO.codigoBarra) &&
                Objects.equals(precioCostoActual, pventaDTO.precioCostoActual) &&
                Objects.equals(precioVentaActual, pventaDTO.precioVentaActual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, idProducto, nombreProducto, codigoBarra, precioCostoActual, precioVentaActual, cantidadVenta);
    }*/
}
