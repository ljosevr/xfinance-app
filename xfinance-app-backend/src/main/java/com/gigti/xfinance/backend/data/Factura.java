/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Factura extends AbstractEntity {

    @NotEmpty
    @Column(name="numero_factura",unique = true)
    private String numeroFactura;

    @NotEmpty
    @Column(name="numero_factura_interno",unique = true)
    private String numeroFacturaInterno;

    private String descripcion;

    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idUser")
    private Usuario usuario;

    @OneToMany(mappedBy = "factura",fetch = FetchType.LAZY)
    private List<ItemsFactura> items;

    public Factura() {
    }

    public Factura(String numeroFactura, String numeroFacturaInterno, String descripcion, Date fechaCreacion, Usuario usuario) {
        this.numeroFactura = numeroFactura;
        this.numeroFacturaInterno = numeroFacturaInterno;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    //TODO
    public String createFacturaNueva(){
        return "0001";
    }
}
