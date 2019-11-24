/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.services.IusuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

    private IusuarioService iusuarioService;

    @Override
    public boolean signIn(String username, String password, @Autowired IusuarioService iusu) {

        this.iusuarioService = iusu;

        if (username == null || username.isEmpty())
            return false;

        if (password == null || password.isEmpty())
            return false;

        Usuario usuario = iusuarioService.login(username,password);

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
        UI.getCurrent().navigate(LoginScreen.class);
    }
}
