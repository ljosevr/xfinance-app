package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.ResponseBool;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

public interface UsuarioService {

    public Usuario login(String nombreUsuario, String Password);

    public Usuario findByNombreUsuario(String nombreUsuario);

    public List<Rol> findAllRol(Empresa empresa, boolean activo);

    public Rol findRolById(String id, Empresa empresa, boolean activo);

    public Rol saveRol(Rol rol, List<Vista> vistasRol);

    public ResponseBool deleteRol(Rol rol);

    public List<Vista> findAllVistas();

}
