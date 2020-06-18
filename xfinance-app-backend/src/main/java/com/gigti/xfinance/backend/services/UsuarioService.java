package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.others.Response;

import java.util.List;

public interface UsuarioService {

    public Usuario login(String codigoEmpresa, String nombreUsuario, String Password);

    public List<Rol> findAllRol(Empresa empresa, boolean eliminado);

    Response saveUsuario(UsuarioDTO usuario);

    Response deleteUsuario(String id);

    List<UsuarioDTO> findAll(Empresa empresa, int page, int pageSize);

    List<UsuarioDTO> findAll(String filter, Empresa empresa, int page, int pageSize);

    Response changePassword(String id, String value, String value1, String value2);
}
