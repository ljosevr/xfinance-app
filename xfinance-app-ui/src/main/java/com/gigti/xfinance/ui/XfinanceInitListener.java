/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.*;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * This class is used to listen to BeforeEnter event of all UIs in order to
 * check whether a user is signed in or not before allowing entering any page.
 * It is registered in a file named
 * com.vaadin.flow.server.VaadinServiceInitListener in META-INF/services.
 */

@Component
public class XfinanceInitListener implements VaadinServiceInitListener, SessionInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {

        event.getSource().addUIInitListener(uiEvent -> { //
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });

        event.getSource().addSessionInitListener(this);
    }

    private void authenticateNavigation(BeforeEnterEvent event) {
    }

    @Override
    public void sessionInit(SessionInitEvent event) {
        event.getSession().setLocale(new Locale("es", "CO"));
    }
}
