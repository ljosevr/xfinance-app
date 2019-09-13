/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.about;

import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;

@Route(value = "about",layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout {

    public static final String VIEW_NAME = "Ayuda";

    public AboutView() {
        add(VaadinIcon.INFO_CIRCLE.create());
        add(new Span(" Esta App esta Bajo Construcción:" +
                "  Proximamente:" +
                "  * Contabilidad " +
                "  * Inventiario " +
                "  * Ventas Online " +
                "  * Compras y Ventas " +
                "  * Exogena Dian " +
                "  * Manejo de Nomina "+
                "  * Usuarios y Roles "+
                "  * Reportes "+
                "  * Alertas Web y Moviles"+
                "  -> Y mucho Más"
                + Version.getFullVersion() + "."));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}
