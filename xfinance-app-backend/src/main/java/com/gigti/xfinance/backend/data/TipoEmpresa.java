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

@Data
@Entity
@Table(name="tipo_empresa")
public class TipoEmpresa implements Serializable {

    public static final TipoEmpresa ROOT = new TipoEmpresa(1,"ROOT","Empresa ROOT Admin del Sistema");
    public static final TipoEmpresa NORMAL = new TipoEmpresa(2,"NORMAL","Empresas clientes");


    @Id
    private Integer id;
    private String name;
    private String description;

    public TipoEmpresa() {
    }

    public TipoEmpresa(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
