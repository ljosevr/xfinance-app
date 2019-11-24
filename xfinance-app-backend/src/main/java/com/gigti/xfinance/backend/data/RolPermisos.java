package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "rol_permisos")
public class RolPermisos extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Permiso permiso;

    public RolPermisos() {
    }

    public RolPermisos(Rol rol, Permiso permiso) {
        this.rol = rol;
        this.permiso = permiso;
    }
}
