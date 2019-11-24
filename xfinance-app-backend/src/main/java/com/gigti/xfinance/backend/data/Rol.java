/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "roles")
public class Rol implements Serializable {

    public static final Rol ROOT = new Rol(1,"Root", "rol por defecto - Acceso todo");
    public static final Rol ADMIN = new Rol(2,"Administrador", "rol por defecto - Acceso Usuario Admin");
    public static final Rol AUXILIAR = new Rol(3,"Auxiliar", "Auxiliar");
    public static final Rol VENDEDOR = new Rol(4,"Vendedor", "Vendedor");
    public static final Rol CONTADOR = new Rol(5,"Contador", "Contador");

    @Id
    private int id;

    @Column(unique = true)
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RolPermisos> rolPermisos;

    public Rol() {
    }

    public Rol(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
