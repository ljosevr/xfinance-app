package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Usuario;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public interface IusuarioService {

    public Usuario login(String nombreUsuario, String Password);

    public Usuario findByNombreUsuario(String nombreUsuario);

}
