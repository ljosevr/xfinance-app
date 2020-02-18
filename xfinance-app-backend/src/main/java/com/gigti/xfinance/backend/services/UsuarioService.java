package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.ResponseBool;

import java.util.List;

public interface UsuarioService {

    public Usuario login(String codigoEmpresa, String nombreUsuario, String Password);

    public List<Usuario> findByNombreUsuario(String nombreUsuario, Empresa empresa, int page, int pageSize);

    public List<Rol> findAllRol(Empresa empresa, boolean eliminado);

    public Rol findRolById(String id, Empresa empresa, boolean activo);

    public Rol saveRol(Rol rol, List<Vista> vistasRol);

    public ResponseBool deleteRol(Rol rol);

    public List<Vista> findAllVistas();

    public Usuario findUsuarioById(String id);

    Usuario saveUsuario(Usuario usuario);

    boolean deleteUsuario(String id);

    List<Usuario> findAll(Empresa empresa, int page, int pageSize);

    List<Usuario> findAll(String filter, Empresa empresa, int page, int pageSize);
}
