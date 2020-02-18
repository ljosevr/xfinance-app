package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_FORGOT, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_RECOVER_PASS +" | "+ Constantes.VIEW_MAIN)
public class ForgotPasswordView extends VerticalLayout {
}
