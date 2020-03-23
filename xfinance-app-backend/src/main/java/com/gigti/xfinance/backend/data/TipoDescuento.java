package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tipos_descuentos")
public class TipoDescuento implements Serializable {

    public static final TipoDescuento VALOR_TOTAL = new TipoDescuento(1,"Valor Total","valor Total de la factura");
    public static final TipoDescuento VALOR_UNIDAD = new TipoDescuento(2,"Valor x Unidad","valor de la Unidad");
    public static final TipoDescuento PORCENTAJE_TOTAL = new TipoDescuento(3,"Porcentaje Total","porcentaje del total de la factura");
    public static final TipoDescuento PORCENTAJE_UNIDAD = new TipoDescuento(4,"Porcentaje x Unidad","Porcentaje de la Unidad");

    @Id
    private Integer id;
    private String nombre;
    private String descripcion;

}
