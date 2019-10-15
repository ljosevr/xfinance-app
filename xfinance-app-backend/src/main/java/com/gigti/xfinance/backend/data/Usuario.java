/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario extends AbstractEntity {

    @Column(name="nombre_usuario", unique = true)
    @Size(min = 4, max = 25)
    @NotNull
    private String nombreUsuario;

    @Column(name="password_usuario")
    @NotNull
    @Size(min = 4, max = 255)
    private String passwordUsuario;

    private boolean activo;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "persona_id")
    @JoinColumn
    private Persona persona;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "empresa_id")
    @JoinColumn
    private Empresa empresa;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "rol_id")
    @JoinColumn
    private Rol rol;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "tipo_usuario_id")
    @JoinColumn
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;

    public Usuario(){}

    public Usuario(String nombreUsuario, String passwordUsuario, Boolean activo, Persona persona, Empresa empresa, Rol rol, TipoUsuario tipoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.passwordUsuario = passwordUsuario;
        this.activo = activo;
        this.persona = persona;
        this.empresa = empresa;
        this.rol = rol;
        this.tipoUsuario = tipoUsuario;
    }

    public static Usuario dummy(Usuario user, String current, double number){
        if(user.getId().isEmpty()) {
            return new Usuario(current,
                    "1234",
                    true,
                    Persona.dummy(number),
                    Empresa.dummy(),
                    new Rol("Admin", "Administrador"),
                    TipoUsuario.ROOT);
        }else{
            return user.getNombreUsuario().equals(current) ? user : null;
        }
    }
}
