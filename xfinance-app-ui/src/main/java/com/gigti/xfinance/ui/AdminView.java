/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaDataProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

/**
 * Admin view that is registered dynamically on admin user login.
 * <p>
 * Allows CRUD operations for the book categories.
 */
@Route(value = "admin",layout = MainLayout.class)
public class AdminView extends VerticalLayout {

    //public final String VIEW_NAME = "admin";

    private final IronList<CategoriaProducto> categoriesListing;
    private final CategoriaDataProvider dataProvider;
    private final Button newCategoryButton;

    //TODO
    //Cambiar View Admin por otra cosa. Esta View debe ser Exclusiva para el
    // menu Admin - Categorias - Usuarios - Roles - Permisos
    public AdminView() {
        categoriesListing = new IronList<>();

        dataProvider = CategoriaDataProvider.getInstance(null);

        //categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(new ComponentRenderer<>(this::createCategoryEditor));

        newCategoryButton = new Button("Agregar Nueva Categoria", event -> {
            CategoriaProducto category = new CategoriaProducto();
            dataProvider.findAll().add(category);
            //dataProvider.refreshAll();
        });
        newCategoryButton.setDisableOnClick(true);

        add(new H2("Hola Admin"), new H4("Editar Categorias"), newCategoryButton, categoriesListing);
    }

    private Component createCategoryEditor(CategoriaProducto category) {
        TextField nameField = new TextField();
        if (category.getId().isBlank()) {
            nameField.focus();
        }

        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), event -> {
            boolean  result = dataProvider.delete(category);
            if(result) {
                dataProvider.findAll().remove(category);
                //dataProvider.refreshAll();
                Notification.show("Categoria Eliminada");
            }else{
                Notification.show("Categoria NO pudo ser Eliminada");
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        BeanValidationBinder<CategoriaProducto> binder = new BeanValidationBinder<>(CategoriaProducto.class);
        binder.forField(nameField).bind("nombre");
        binder.setBean(category);
        binder.addValueChangeListener(event -> {
            if (binder.isValid()) {
                //TODO
                dataProvider.save(category);
                deleteButton.setEnabled(true);
                newCategoryButton.setEnabled(true);
                Notification.show("Categoria Guardada.");
            }
        });
        deleteButton.setEnabled(!category.getId().isBlank());

        HorizontalLayout layout = new HorizontalLayout(nameField, deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}
