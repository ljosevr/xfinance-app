/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Data // Aplica para Lombok para no tener que crear los Get y Set - Falla con Java 12
@Entity
@Table(name = "personas")
public class Persona extends AbstractEntity{

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
    private TipoIde tipoIde;

    @NotEmpty
    @Column(unique = true)
    private String identificacion;
    @NotEmpty
    @Column(name = "primer_nombre")
    private String primerNombre;
    @Column(name = "segundo_nombre")
    private String segundoNombre;
    @NotEmpty
    @Column(name = "primer_apellido")
    private String primerApellido;
    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    private String telefono;
    private String direccion;

    public Persona(){}

    public Persona(TipoIde tipoIde, String identificacion, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, Date fechaNacimiento, String email, String telefono, String direccion) {
        this.tipoIde = tipoIde;
        this.identificacion = identificacion;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Persona persona = (Persona) o;
        return Objects.equals(tipoIde, persona.tipoIde) &&
                Objects.equals(identificacion, persona.identificacion) &&
                Objects.equals(primerNombre, persona.primerNombre) &&
                Objects.equals(segundoNombre, persona.segundoNombre) &&
                Objects.equals(primerApellido, persona.primerApellido) &&
                Objects.equals(segundoApellido, persona.segundoApellido) &&
                Objects.equals(fechaNacimiento, persona.fechaNacimiento) &&
                Objects.equals(email, persona.email) &&
                Objects.equals(telefono, persona.telefono) &&
                Objects.equals(direccion, persona.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tipoIde, identificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaNacimiento, email, telefono, direccion);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "tipoIde=" + tipoIde +
                ", identificacion='" + identificacion + '\'' +
                ", primerNombre='" + primerNombre + '\'' +
                ", segundoNombre='" + segundoNombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }

    public static Persona dummy(double number){
        Persona persona = new Persona(TipoIde.CEDULA,
                        "100"+number,
                        "Luis",
                "José",
                "Villarreal",
                "Rincón",
                 new Date(),
                "ljosevr"+number+"@gmail.com",
                "3006520012",
                "Laplace");

        return persona;
    }

    public String getCompleteName(){
        return String.format("%s %s %s %s", primerNombre, segundoNombre, primerApellido, segundoApellido);
    }
}
