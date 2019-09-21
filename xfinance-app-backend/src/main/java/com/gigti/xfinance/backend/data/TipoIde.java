/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "tipo_ide")
public class TipoIde implements Serializable {

    public static final TipoIde CEDULA = new TipoIde(1,"CC","Cedula de Ciudadania");
    public static final TipoIde NIT = new TipoIde(2,"NIT","Nit");
    public static final TipoIde EXTRANJERIA = new TipoIde(3,"CE","Cedula de Extranjeria");
    public static final TipoIde TIDENTIDAD = new TipoIde(4,"TI","Tarjeta de Identidad");

    @Id
    private Integer id;

    private String nombre;

    private String descripcion;

    public TipoIde(){}

    public TipoIde(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoIde{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
