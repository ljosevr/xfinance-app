package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

public class CategoriaForm extends Dialog {
    private TextField tfCatNombre;
    private FormLayout content;

    private Button btnSave;
    private Button btnDelete;

    private CategoriaCrud viewLogic;
    private Binder<CategoriaProducto> binder;
    private CategoriaProducto currentCategoria;

    public CategoriaForm(CategoriaCrud categoriaCrudLogic) {
        content = new FormLayout();
        content.setClassName("formLayoutDialog");
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));

        H4 title = new H4("Crear o Editar Categoria");
        title.addClassName("subTitleView");
        content.add(title, 3);

        viewLogic = categoriaCrudLogic;

        tfCatNombre = new TextField("Nombre Categoria");
        tfCatNombre.setRequired(true);
        tfCatNombre.setValueChangeMode(ValueChangeMode.EAGER);
        tfCatNombre.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfCatNombre.setClearButtonVisible(true);
        //tfCatNombre.setWidth("70%");
        tfCatNombre.focus();

        TextField tfCatDescripcion = new TextField("Descripción Categoria");
        tfCatDescripcion.setRequired(false);
        tfCatDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        tfCatDescripcion.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfCatDescripcion.setClearButtonVisible(true);

        content.add(tfCatNombre, tfCatDescripcion);

        Checkbox cbCatActivo = new Checkbox("Activo");
        cbCatActivo.setValue(true);
        content.add(cbCatActivo, 3);

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
        });

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnSave.addClickListener(event -> {
            if (currentCategoria != null
                    && binder.writeBeanIfValid(currentCategoria)) {
                viewLogic.save(currentCategoria);
            }
        });
        btnSave.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        Button btnCancel = new Button("Cancelar");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnCancel.addClickListener(event -> viewLogic.cancelar());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelar())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        //btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SMALL);
        btnDelete.addClickListener(event -> {
            if (currentCategoria != null) {
                viewLogic.delete(currentCategoria);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnCancel);
        content.add(actionsLayout,3);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
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

    public TextField getTfCatNombre() {
        return tfCatNombre;
    }
}
