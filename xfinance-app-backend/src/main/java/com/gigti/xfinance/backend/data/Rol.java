/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "roles", uniqueConstraints={@UniqueConstraint(columnNames={"nombre","empresa_id"})})
public class Rol extends AbstractEntity {

    public static final Rol ROOT = new Rol("Root", "ROOT", true, null , false, null);
    public static final Rol ADMIN = new Rol("Administrador", "ADMIN", true, null , false, null);
    public static final Rol AUXILIAR = new Rol("Auxiliar", "Auxiliar", true, null , false, null);
    public static final Rol VENDEDOR = new Rol("Vendedor", "Vendedor", true, null , false, null);
    public static final Rol CONTADOR = new Rol("Contador", "Contador", true, null , false, null);

    private String nombre;
    private String descripcion;
    private boolean porDefecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    private boolean eliminado;

    @NotNull
    private boolean activo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActivacion;

    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name="rolesvistas", joinColumns={@JoinColumn(name="roles_id")}, inverseJoinColumns={@JoinColumn(name="vistas_id")})
    private Set<Vista> vistas;

    @Transient
    private String activoS;

    public Rol() {
    }

    public Rol(String id, String nombre, String descripcion, boolean porDefecto, Empresa empresa, boolean eliminado, Date fechaActivacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porDefecto = porDefecto;
        this.empresa = empresa;
        this.eliminado = eliminado;
        this.fechaActivacion = fechaActivacion;
    }

    public Rol(String nombre, String descripcion, boolean porDefecto, Empresa empresa, boolean eliminado, Date fechaActivacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porDefecto = porDefecto;
        this.empresa = empresa;
        this.eliminado = eliminado;
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
                Objects.equals(eliminado, rol.eliminado) &&
                Objects.equals(fechaActivacion, rol.fechaActivacion) &&
                Objects.equals(usuarios, rol.usuarios) &&
                Objects.equals(vistas, rol.vistas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getActivoS() {
        return isActivo() ? "SI" : "NO";
    }
}
