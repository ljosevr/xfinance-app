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
@Table(name="tipos_usuario")
public class TipoUsuario implements Serializable {

    public static final TipoUsuario ROOT = new TipoUsuario(1,"ROOT","Usuario Admin del Sistema");
    public static final TipoUsuario ADMIN = new TipoUsuario(2,"ADMIN","Administrador de la Compañia");
    public static final TipoUsuario SELLER = new TipoUsuario(3,"SELLER","Vendedor");
    public static final TipoUsuario NORMAL = new TipoUsuario(4,"NORMAL","Usuario Normal de una compañia");


    @Id
    private Integer id;
    private String name;
    private String description;

    public TipoUsuario() {
    }

    public TipoUsuario(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
