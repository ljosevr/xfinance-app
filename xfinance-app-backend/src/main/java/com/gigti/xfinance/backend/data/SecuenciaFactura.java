package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class SecuenciaFactura implements Serializable {

    @Id
    private Empresa empresa;
    private int numero;

    public SecuenciaFactura() {
    }

    public SecuenciaFactura(int numero, Empresa empresa) {
        this.numero = numero;
        this.empresa = empresa;
    }
}
