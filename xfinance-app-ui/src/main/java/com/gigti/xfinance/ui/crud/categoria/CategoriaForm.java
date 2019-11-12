package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

public class CategoriaForm extends FormLayout {
    //private VerticalLayout content;

    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private CategoriaCrudLogic viewLogic;
    private Binder<CategoriaProducto> binder;
    private CategoriaProducto currentCategoria;

    public CategoriaForm(CategoriaCrudLogic categoriaCrudLogic) {
        //this.setClassName("form-layout");
        //this.setSizeFull();
        this.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Categoria");
        this.add(title, 3);

        viewLogic = categoriaCrudLogic;

        TextField tfCatNombre = new TextField("Nombre Categoria");
        tfCatNombre.setRequired(true);
        tfCatNombre.setValueChangeMode(ValueChangeMode.EAGER);
        tfCatNombre.focus();

        TextField tfCatDescripcion = new TextField("Descripción Categoria");
        tfCatDescripcion.setRequired(false);
        tfCatDescripcion.setValueChangeMode(ValueChangeMode.EAGER);

        this.add(tfCatNombre, tfCatDescripcion);

        Checkbox cbCatActivo = new Checkbox("Activo");
        cbCatActivo.setValue(true);
        this.add(cbCatActivo, 3);

        binder = new BeanValidationBinder<>(CategoriaProducto.class);
        binder.forField(tfCatNombre).bind(CategoriaProducto::getNombre,
                CategoriaProducto::setNombre);
        binder.forField(tfCatDescripcion).bind(CategoriaProducto::getDescripcion,
                CategoriaProducto::setDescripcion);
        binder.forField(cbCatActivo).bind(CategoriaProducto::isActivo,
                CategoriaProducto::setActivo);
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
        });

        btnSave = new Button("Guardar");
        //btnSave.setWidth("100%");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
            if (currentCategoria != null
                    && binder.writeBeanIfValid(currentCategoria)) {
                viewLogic.guardar(currentCategoria);
            }
        });
        btnSave.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        btnDiscard = new Button("Descartar Cambios");
        //btnDiscard.setWidth("100%");
        btnDiscard.addClickListener(
                event -> viewLogic.editar(currentCategoria));

        Button btnCancel = new Button("Cancelar");
        //btnCancel.setWidth("100%");
        btnCancel.addClickListener(event -> viewLogic.cancelar());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelar())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        //btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentCategoria != null) {
                viewLogic.eliminar(currentCategoria);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDiscard);
        HorizontalLayout actionsLayout2 = new HorizontalLayout();
        actionsLayout.add(btnDelete, btnCancel);
        this.add(actionsLayout,3);
        this.add(actionsLayout2,3);
    }

    public void editCategoria(CategoriaProducto categoria) {
        if (categoria == null) {
            categoria = new CategoriaProducto();
            categoria.setActivo(true);
            btnDelete.setEnabled(false);
        } else if(StringUtils.isBlank(categoria.getId())){
            categoria.setActivo(true);
            btnDelete.setEnabled(false);
        } else {
            btnDelete.setEnabled(true);
        }
        currentCategoria = categoria;
        binder.readBean(categoria);
    }
}
