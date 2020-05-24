/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

/**
 * View shown when trying to navigate to a view that does not exist using
 */
@ParentLayout(value = MainLayout2.class)
public class ErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    private Span explanation;

    public ErrorView() {
        H1 header = new H1("Esta pagina no ha podido ser encontrada.");
        add(header);

        explanation = new Span();
        add(explanation);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        explanation.setText("No se puede navegar a '"+ event.getLocation().getPath() + "'.");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
