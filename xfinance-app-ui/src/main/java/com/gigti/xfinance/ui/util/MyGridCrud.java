package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.button.ButtonVariant;
import org.vaadin.crudui.crud.impl.GridCrud;

public class MyGridCrud<T> extends GridCrud<T> {

    public MyGridCrud(Class<T> domainType) {
        super(domainType);
    }

    public void inSpanish() {
        this.getAddButton().getElement().setAttribute("title", "Agregar");
        this.getDeleteButton().getElement().setAttribute("title", "Eliminar");
        this.getUpdateButton().getElement().setAttribute("title", "Editar");
        this.getFindAllButton().getElement().setAttribute("title", "Refrescar");
        this.getAddButton().addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.getDeleteButton().addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.getUpdateButton().addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    }
}
