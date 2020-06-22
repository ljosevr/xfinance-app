/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * View shown when trying to navigate to a view that does not exist using
 */
@Route(value = "NotAccess", layout = MainLayout.class)
public class NotAccessView extends VerticalLayout {

    public NotAccessView() {
        H1 header = new H1("No tiene permiso para ingresar a esta pagina.\nValide con el administrador");
        add(header);
    }

}
