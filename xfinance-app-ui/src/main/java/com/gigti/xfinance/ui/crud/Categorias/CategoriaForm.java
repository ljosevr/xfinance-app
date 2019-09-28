package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

public class CategoriaForm extends Div {
    private VerticalLayout content;

    private TextField tfCatNombre;
    private TextField tfCatDescripcion;
    private Checkbox  cbCatActivo;

    private Button btnSave;
    private Button btnDiscard;
    private Button btnCancel;
    private Button btnDelete;

    private CategoriaCrudLogic viewLogic;
    private Binder<CategoriaProducto> binder;
    private CategoriaProducto currentCategoria;

    public CategoriaForm(CategoriaCrudLogic categoriaCrudLogic) {
        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = categoriaCrudLogic;

        tfCatNombre = new TextField("Nombre Categoria");
        tfCatNombre.setWidth("100%");
        tfCatNombre.setRequired(true);
        tfCatNombre.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(tfCatNombre);

        tfCatDescripcion = new TextField("Descripción Categoria");
        tfCatDescripcion.setWidth("100%");
        tfCatDescripcion.setRequired(false);
        tfCatDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(tfCatDescripcion);

        cbCatActivo = new Checkbox("Activo");
        cbCatActivo.setValue(true);
        content.add(cbCatActivo);

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
        btnSave.setWidth("100%");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
            if (currentCategoria != null
                    && binder.writeBeanIfValid(currentCategoria)) {
                viewLogic.saveCategoria(currentCategoria);
            }
        });
        btnSave.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        btnDiscard = new Button("Descartar Cambios");
        btnDiscard.setWidth("100%");
        btnDiscard.addClickListener(
                event -> viewLogic.editCategoria(currentCategoria));

        btnCancel = new Button("Cancelar");
        btnCancel.setWidth("100%");
        btnCancel.addClickListener(event -> viewLogic.cancelCategoria());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelCategoria())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentCategoria != null) {
                viewLogic.deleteCategoria(currentCategoria);
            }
        });

        content.add(btnSave, btnDiscard, btnDelete, btnCancel);
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
