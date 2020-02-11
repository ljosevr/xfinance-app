/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

    private UsuarioService usuarioService;

    @Override
    public boolean signIn(String username, String password, @Autowired UsuarioService iusu) {

        this.usuarioService = iusu;

        if (username == null || username.isEmpty())
            return false;

        if (password == null || password.isEmpty())
            return false;

        Usuario usuario = usuarioService.login(username,password);

        if(usuario != null){
            CurrentUser.set(usuario);
            return true;
        }

        return false;
    }

    @Override
    public boolean isUserSignedIn() {
        return CurrentUser.get() != null;
    }

    @Override
    public boolean isUserInRole(Usuario currentUser) {
        if ("Default".equals(currentUser.getRol().getNombre())) {
            return getTipoUsuario().equalsIgnoreCase("ADMIN") || getTipoUsuario().equalsIgnoreCase("ROOT");
        }

        // All users are in all non-admin roles
        return true;
    }

    @Override
    public String getTipoUsuario() {
        return CurrentUser.get().getTipoUsuario().getName();
    }

    @Override
    public void signOut() {
        CurrentUser.set(null);
        VaadinSession.getCurrent().close();
        //UI.getCurrent().navigate(MainLayout.class);
    }
}
