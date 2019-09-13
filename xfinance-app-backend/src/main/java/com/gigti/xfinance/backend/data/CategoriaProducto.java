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

    @ManyToOne(fetch = FetchType.EAGER)
    private Empresa empresa;

    public CategoriaProducto() {
    }

    public CategoriaProducto(@Size(min = 2, message = "Categoria debe tener minimo 2 Caracteres") @NotNull String nombre, String descripcion, Empresa empresa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.empresa = empresa;
    }
}
