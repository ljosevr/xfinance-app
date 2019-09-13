package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "rol_permisos")
public class RolPermisos extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    private Permisos permisos;

}
