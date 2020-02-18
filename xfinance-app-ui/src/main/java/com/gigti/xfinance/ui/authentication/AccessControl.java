/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.util.Response;

import java.io.Serializable;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface AccessControl extends Serializable {

    Response signIn(String codigoEmpresa, String username, String password, UsuarioService iusu);

    boolean isUserSignedIn();

    boolean isUserInRole(Usuario currentUser);

    String getTipoUsuario();

    void signOut();
}
