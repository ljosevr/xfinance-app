package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;

public interface UsuarioService {

    Usuario login(String codigoEmpresa, String nombreUsuario, String Password);

    List<Rol> findAllRol(Empresa empresa, boolean eliminado);

    Response saveUsuario(UsuarioDTO usuario);

    Usuario saveUsuario(Usuario usuario);

    Response deleteUsuario(String id);

    List<UsuarioDTO> findAll(Empresa empresa, int page, int pageSize);

    List<UsuarioDTO> findAll(String filter, Empresa empresa, int page, int pageSize);
    List<UsuarioDTO> findAll(String filter, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);

    Response changePassword(String id, String value, String value1, String value2);

    Usuario findByName(String userName);

    int count(String filter, Empresa empresa);

    boolean deleteAllUsuariosByEmpresa(Empresa empresa);
}
