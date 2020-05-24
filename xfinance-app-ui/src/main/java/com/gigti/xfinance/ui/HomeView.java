package com.gigti.xfinance.ui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;

@Route(value = "Home", layout = MainLayout2.class)
public class HomeView extends FlexLayout {
    public static final String VIEW_NAME = "home";

    public HomeView() {
        Label lbl = new Label("HOME VIEW");
        add(lbl);
    }
}
