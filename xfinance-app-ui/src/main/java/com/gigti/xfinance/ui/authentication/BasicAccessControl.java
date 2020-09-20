/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;


/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

    private UsuarioService usuarioService;

    @Override
    public Response signIn(String codigoEmpresa, String username, String password, @Autowired UsuarioService iusu) {

        Response response = new Response();
        this.usuarioService = iusu;

        if (username == null || username.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Usuario Incorrecto");
        }

        if (password == null || password.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Password incorrecto");
        }
        String pass = "";
        try {
            pass = UtilsBackend.encrytPass(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Password incorrecto");
        }
        Usuario usuario = usuarioService.login(codigoEmpresa, username, pass);

        if(usuario != null){
            if(usuario.getPersona().getEmpresa().isActivo()) {
                if (usuario.isActivo()) {
                    response.setSuccess(true);
                    CurrentUser.set(usuario);
                } else {
                    response.setSuccess(false);
                    response.setMessage("Usuario " + usuario.getNombreUsuario() + " NO ACTIVO");
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Empresa: " + usuario.getPersona().getEmpresa().getNombreEmpresa() + " NO ACTIVA");
            }
        } else {
            response.setSuccess(false);
            response.setMessage("Validar Datos Ingresados");
        }

        return response;
    }

    @Override
    public boolean isUserSignedIn() {
        return CurrentUser.get() != null;
    }

    @Override
    public void signOut() {
        //UI.getCurrent().getSession().invalidate();
        CurrentUser.set(null);
        UI.getCurrent().getSession().close();
    }
}
