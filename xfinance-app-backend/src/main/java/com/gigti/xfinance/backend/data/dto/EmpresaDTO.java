package com.gigti.xfinance.backend.data.dto;

import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.TipoEmpresa;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.Vista;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class EmpresaDTO implements Serializable {

    private String  empresaId;
    private TipoIde tipoIde;
    private String identificacion;
    private String nombreEmpresa;
    private String direccion;
    private String telefono;
    private boolean activo;
    private boolean eliminado;
    private Date fechaActivacion;
    private Date fechaDesactivacion;
    private TipoEmpresa tipoEmpresa;
    private String codigoEmpresa;

    private String activoS;

    private String usuarioId;
    private String usuarioNombre;
    private Rol rol;
    private List<Vista> vistas;
    private boolean activoUsuario;

    private boolean eliminadoUsuario;
    private Date    fechaActivacionUsuario;
    private Date    fechaDesactivacionUsuario;

    private String personaId;
    private String primerNombrePersona;
    private String segundoNombrePersona;
    private String primerApellidoPersona;
    private String segundoApellidoPersona;

    private TipoIde tipoIdePersona;
    private String identificacionPersona;

    private String direccionPersona;
    private String telefonoPersona;
    private String emailPersona;

    public EmpresaDTO() {
    }

    public String getActivoS() {
        return this.isActivo() ? "SI" : "NO";
    }

    @Override
    public String toString() {
        return nombreEmpresa;
    }

}
