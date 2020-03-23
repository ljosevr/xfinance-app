package com.gigti.xfinance.backend.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "impuestos")
public class Impuesto extends AbstractEntity {

    private String nombre;
    private BigDecimal valor;
    private String descripcion;
}
