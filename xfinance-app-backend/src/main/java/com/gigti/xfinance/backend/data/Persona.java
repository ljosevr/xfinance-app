/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "personas", uniqueConstraints={@UniqueConstraint(columnNames={"identificacion", "empresa_id"})})
public class Persona extends AbstractEntity {

    @NotNull(message = "No puede estar Vacio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
    private TipoIde tipoIde;

    @NotNull(message = "No puede estar Vacio")
    @Column()
    private String identificacion;

    @NotNull(message = "No puede estar Vacio")
    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    private String telefono;

    private String direccion;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    public Persona(){}

    public Persona(TipoIde tipoIde, String identificacion, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, Date fechaNacimiento, String telefono, String direccion, Empresa empresa) {
        this.tipoIde = tipoIde;
        this.identificacion = identificacion;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.empresa = empresa;
    }

    public String getNombreCompleto(){
        return String.format("%s %s %s %s", primerNombre, segundoNombre != null ? segundoNombre : "", primerApellido, segundoApellido != null ? segundoApellido : "");
    }
}
