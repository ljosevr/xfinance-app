package com.gigti.xfinance.backend.data.dto;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO extends Persona {

    private String usuarioid;
    private String nombreUsuario;
    private Rol rol;
    private boolean eliminado;
    private boolean activo;
    private String activoS;
    private Empresa empresa;
    private String email;

    public String getActivoS() {
        return this.isActivo() ? "SI" : "NO";
    }
}
