package com.gigti.xfinance.ui.crud.configuracion;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.MainLayout2;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_CONFIG_PERFIL, layout = MainLayout2.class)
@PageTitle(value = Constantes.VIEW_CONFIG_PERFIL +" | "+Constantes.VIEW_MAIN)
public class PerfilView extends VerticalLayout {

    public PerfilView() {
        addClassName("ProfileView");
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        H2 titleConstruction = new H2("Pagina En Contrucción");
        titleConstruction.setClassName("underConstruction");
        add(titleConstruction);
    }
}
