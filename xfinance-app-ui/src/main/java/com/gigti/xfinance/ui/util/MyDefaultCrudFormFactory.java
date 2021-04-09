package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.formlayout.FormLayout;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;

public class MyDefaultCrudFormFactory extends DefaultCrudFormFactory {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MyDefaultCrudFormFactory(Class domainType, FormLayout.ResponsiveStep... responsiveSteps) {
        super(domainType, responsiveSteps);
    }

    public MyDefaultCrudFormFactory(Class domainType) {
        super(domainType);

    }

    @Override
    public String buildCaption(CrudOperation operation, Object domainObject) {
        String op = "";
        switch (operation) {
            case ADD:
                op = "Crear";
                break;
            case UPDATE:
                op = "Actualizar";
                break;
            case DELETE:
                op = "Eliminar";
                break;
            case READ:
                op = "Leer";
                break;
            default:
                op = "";
                break;
        }

        return op;

    }

    public void inSpanish() {
        this.setCancelButtonCaption("Cancelar");
        this.setButtonCaption(CrudOperation.ADD, "Guardar");
        this.setButtonCaption(CrudOperation.UPDATE, "Guardar");
        this.setButtonCaption(CrudOperation.DELETE, "Sí, Eliminar");
    }
}

