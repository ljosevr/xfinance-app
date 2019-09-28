package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name="categoria_producto")
public class CategoriaProducto extends AbstractEntity{

    @Size(min = 2, message = "Categoria debe tener minimo 2 Caracteres")
    @NotNull
    private String nombre;
    private String descripcion;
    @NotNull
    private boolean activo;
    @NotNull
    private boolean eliminado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    @Transient
    private String activoS;

    public CategoriaProducto() {
    }

    public CategoriaProducto(@Size(min = 2, message = "Categoria debe tener minimo 2 Caracteres") @NotNull String nombre, String descripcion, Empresa empresa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.empresa = empresa;
        this.activo = true;
        this.eliminado = false;
    }

    public CategoriaProducto(@Size(min = 2, message = "Categoria debe tener minimo 2 Caracteres") @NotNull String nombre, String descripcion, @NotNull boolean activo, Empresa empresa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.empresa = empresa;
        this.eliminado = false;
    }

    public String getActivoS() {
        return isActivo() ? "SI" : "NO";
    }

}
