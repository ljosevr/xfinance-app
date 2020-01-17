/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.gigti.xfinance.ui.authentication.AccessControl;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.LoginScreen;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * This class is used to listen to BeforeEnter event of all UIs in order to
 * check whether a user is signed in or not before allowing entering any page.
 * It is registered in a file named
 * com.vaadin.flow.server.VaadinServiceInitListener in META-INF/services.
 */

@SpringComponent
public class XfinanceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {

        final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

        event.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                if (!accessControl.isUserSignedIn() && !LoginScreen.class
                        .equals(enterEvent.getNavigationTarget())) {
                    enterEvent.rerouteTo(LoginScreen.class);
                }
            });
        });
    }
}
