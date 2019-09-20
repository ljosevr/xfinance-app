/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "roles")
public class Rol extends AbstractEntity {

    public static final Rol ROOT = new Rol("Default", "rol por defecto - Acceso todo");

    @Column(unique = true)
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RolPermisos> rolPermisos;

    public Rol() {
    }

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
