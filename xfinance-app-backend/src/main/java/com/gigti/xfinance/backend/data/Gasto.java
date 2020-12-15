package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "gastos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gasto extends AbstractEntity {

    @NotNull
    private String descripcion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private BigDecimal valor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    public LocalDate getFechaLD(){
        return fecha != null ? fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate() : null;
    }
    public void setFechaLD(LocalDate fecha) {
        this.fecha = java.util.Date.from(fecha.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
