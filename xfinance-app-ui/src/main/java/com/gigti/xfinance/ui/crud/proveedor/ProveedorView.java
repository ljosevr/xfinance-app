package com.gigti.xfinance.ui.crud.proveedor;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.MainLayout2;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_ADMIN_PROVEEDOR, layout = MainLayout2.class)
@PageTitle(value = Constantes.VIEW_ADMIN_PROVEEDOR +" | "+Constantes.VIEW_MAIN)
public class ProveedorView extends VerticalLayout {

    public ProveedorView() {
        addClassName("ProveedorView");
        setDefaultHorizontalComponentAlignment(Alignment.START);
        H2 titleConstruction = new H2("Pagina En Contrucción");
        titleConstruction.setClassName("underConstruction");
        add(titleConstruction);
    }
}
