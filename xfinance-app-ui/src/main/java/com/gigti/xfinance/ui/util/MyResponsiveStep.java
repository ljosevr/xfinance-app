package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.formlayout.FormLayout;

import java.util.ArrayList;
import java.util.List;

public class MyResponsiveStep extends FormLayout.ResponsiveStep {
    private MyResponsiveStep(String minWidth, int columns) {
        super(minWidth, columns);
    }


    public static List<FormLayout.ResponsiveStep> getMyList() {
        List<FormLayout.ResponsiveStep> lista = new ArrayList<>();
        lista.add(new MyResponsiveStep("300Px", 1));
        lista.add(new MyResponsiveStep("450px", 3));
        lista.add(new MyResponsiveStep("800px", 4));
        lista.add(new MyResponsiveStep("1000px", 5));
        return lista;
    }

    public static List<FormLayout.ResponsiveStep> getMyListForm2Columns() {
        List<FormLayout.ResponsiveStep> lista = new ArrayList<>();
        lista.add(new MyResponsiveStep("300Px", 1));
        lista.add(new MyResponsiveStep("450px", 2));
        return lista;
    }

    public static List<FormLayout.ResponsiveStep> getMyListForm3Columns() {
        List<FormLayout.ResponsiveStep> lista = new ArrayList<>();
        lista.add(new MyResponsiveStep("300Px", 1));
        lista.add(new MyResponsiveStep("450px", 2));
        lista.add(new MyResponsiveStep("800px", 3));
        return lista;
    }
}
