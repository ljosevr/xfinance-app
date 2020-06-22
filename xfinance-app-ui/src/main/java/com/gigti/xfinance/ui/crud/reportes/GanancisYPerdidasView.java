package com.gigti.xfinance.ui.crud.reportes;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_GANANCIAS_Y_PERDIDAS, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_GANANCIAS_Y_PERDIDAS +" | "+Constantes.VIEW_MAIN)
public class GanancisYPerdidasView extends VerticalLayout {

    public GanancisYPerdidasView() {
        addClassName("BalanceView");
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        H2 titleConstruction = new H2("Pagina En Contrucción");
        titleConstruction.setClassName("underConstruction");
        add(titleConstruction);
    }
}

