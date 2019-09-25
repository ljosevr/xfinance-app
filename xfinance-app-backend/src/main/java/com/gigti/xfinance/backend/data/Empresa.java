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
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "empresa")
public class Empresa extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private TipoIde tipoIde;

    @NotNull
    @NotEmpty
    @Size(min= 4)
    @Column(unique = true)
    private String identificacion;

    @NotNull
    @NotEmpty
    @Size(min= 4)
    @Column(name = "nombre_empresa",unique = true)
    private String nombreEmpresa;

    private String direccion;

    @OneToMany(mappedBy = "empresa",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;

    @OneToMany(mappedBy = "empresa", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoriaProducto> categoriaProductos;

    private Boolean activo;

    private Boolean eliminado;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fechaActivacion;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fechaDesactivacion;

    public Empresa(){}

    public Empresa(TipoIde tipoIde, String identificacion, String nombreEmpresa, String direccion, Boolean activo, Date fechaActivacion, Date fechaDesactivacion) {
        this.tipoIde = tipoIde;
        this.identificacion = identificacion;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.activo = activo;
        this.fechaActivacion = fechaActivacion;
        this.fechaDesactivacion = fechaDesactivacion;
        this.eliminado = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(tipoIde, empresa.tipoIde) &&
                Objects.equals(identificacion, empresa.identificacion) &&
                Objects.equals(nombreEmpresa, empresa.nombreEmpresa) &&
                Objects.equals(direccion, empresa.direccion) &&
                Objects.equals(usuarios, empresa.usuarios) &&
                Objects.equals(productos, empresa.productos) &&
                Objects.equals(categoriaProductos, empresa.categoriaProductos) &&
                Objects.equals(activo, empresa.activo) &&
                Objects.equals(eliminado, empresa.eliminado) &&
                Objects.equals(fechaActivacion, empresa.fechaActivacion) &&
                Objects.equals(fechaDesactivacion, empresa.fechaDesactivacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tipoIde, identificacion, nombreEmpresa, direccion, usuarios, productos, categoriaProductos, activo, eliminado, fechaActivacion, fechaDesactivacion);
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "ID=" + id + + '\'' +
                "tipoIde=" + tipoIde + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", nombreEmpresa='" + nombreEmpresa + '\'' +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                ", eliminado=" + eliminado +
                ", fechaActivacion=" + fechaActivacion +
                ", fechaDesactivacion=" + fechaDesactivacion +
                '}';
    }

    public static Empresa dummy(){
        return new Empresa(TipoIde.NIT,
                "9000800500",
                "GigTi S.A.S",
                "Bogota Laplace",
                true,
                new Date(),
                null
                );
    }
}
