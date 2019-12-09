/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "roles")
public class Rol extends AbstractEntity implements Serializable {

    public static final Rol ROOT = new Rol("root_1","Root", "rol por defecto - Acceso todo", true, null , true, java.sql.Date.valueOf(LocalDate.now()));
    public static final Rol ADMIN = new Rol("Administrador", "rol por defecto - Acceso Usuario Admin", true, null , true, java.sql.Date.valueOf(LocalDate.now()));
    public static final Rol AUXILIAR = new Rol("Auxiliar", "Auxiliar", true, null , true, java.sql.Date.valueOf(LocalDate.now()));
    public static final Rol VENDEDOR = new Rol("Vendedor", "Vendedor", true, null , true, java.sql.Date.valueOf(LocalDate.now()));
    public static final Rol CONTADOR = new Rol("Contador", "Contador", true, null , true, java.sql.Date.valueOf(LocalDate.now()));

    @Column(unique = true)
    private String nombre;
    private String descripcion;
    private boolean porDefecto = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    private Boolean activo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActivacion;

    @OneToMany(mappedBy = "rol",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="rolesvistas", joinColumns={@JoinColumn(name="roles_id")}, inverseJoinColumns={@JoinColumn(name="vistas_id")})
    private Set<Vista> vistas;

    public Rol() {
    }

    public Rol(String id, String nombre, String descripcion, boolean porDefecto, Empresa empresa, Boolean activo, Date fechaActivacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porDefecto = porDefecto;
        this.empresa = empresa;
        this.activo = activo;
        this.fechaActivacion = fechaActivacion;
    }

    public Rol(String nombre, String descripcion, boolean porDefecto, Empresa empresa, Boolean activo, Date fechaActivacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porDefecto = porDefecto;
        this.empresa = empresa;
        this.activo = activo;
        this.fechaActivacion = fechaActivacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rol rol = (Rol) o;
        return porDefecto == rol.porDefecto &&
                Objects.equals(nombre, rol.nombre) &&
                Objects.equals(descripcion, rol.descripcion) &&
                Objects.equals(empresa, rol.empresa) &&
                Objects.equals(activo, rol.activo) &&
                Objects.equals(fechaActivacion, rol.fechaActivacion) &&
                Objects.equals(usuarios, rol.usuarios) &&
                Objects.equals(vistas, rol.vistas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
