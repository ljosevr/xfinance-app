/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import com.gigti.xfinance.backend.data.enums.TipoUsuarioEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario extends AbstractEntity {

    @Column(name="nombre_usuario")
    @Size(min = 4, max = 25)
    @NotNull
    private String nombreUsuario;

    @Column(name="password_usuario")
    @NotNull
    @Size(min = 4, max = 512)
    private String passwordUsuario;

    private boolean activo;

    @NotNull
    private boolean eliminado = false;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Persona persona;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Rol rol;

    private boolean adminDefecto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipoUsuario;

    @NotNull(message = "No puede estar Vacio")
    @Column()
    @Email
    private String email;

    @PrePersist
    private void validateUser() {
        if(tipoUsuario == null) {
            tipoUsuario = TipoUsuarioEnum.NORMAL;
        }
    }

    public Usuario(){
        super();
    }

    public Usuario(String nombreUsuario, String passwordUsuario, Boolean activo, Persona persona, Rol rol, TipoUsuarioEnum tipoUsuario, String email) {
        this.nombreUsuario = nombreUsuario;
        this.passwordUsuario = passwordUsuario;
        this.activo = activo;
        this.persona = persona;
        this.rol = rol;
        this.tipoUsuario = tipoUsuario;
        this.eliminado = false;
        this.email = email;
    }

    public String getActivoS() {
        return isActivo() ? "SI" : "NO";
    }

    @Override
    public String toString() {
        return nombreUsuario;
    }
}
