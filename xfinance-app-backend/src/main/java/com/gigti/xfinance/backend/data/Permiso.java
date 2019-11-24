/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "permisos")
public class Permiso extends AbstractEntity {

    private String nombrePermiso;
    private String nombreVista;
    private Boolean activo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActivacion;

    public Permiso() {
    }

    public Permiso(String nombrePermiso, String nombreVista, Boolean activo, Date fechaActivacion) {
        this.nombrePermiso = nombrePermiso;
        this.nombreVista = nombreVista;
        this.activo = activo;
        this.fechaActivacion = fechaActivacion;
    }
}
