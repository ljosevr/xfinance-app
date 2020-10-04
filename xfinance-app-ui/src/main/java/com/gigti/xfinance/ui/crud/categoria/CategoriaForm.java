package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

public class CategoriaForm extends Dialog {
    private final TextField tfCatDescripcion;
    private final Checkbox chkCatActivo;
    private final Button btnDelete;
    private final H2 titleForm;
    private final TextField tfCatNombre;
    private Button btnSave;
    private final Binder<CategoriaProducto> binder;

    public CategoriaForm() {

        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        titleForm = new H2("");
        titleForm.addClassName("titleView");

        tfCatNombre = new TextField("Nombre Categoria");
        tfCatNombre.setRequired(true);
        tfCatNombre.setValueChangeMode(ValueChangeMode.EAGER);
        tfCatNombre.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfCatNombre.setClearButtonVisible(true);
        tfCatNombre.focus();

        tfCatDescripcion = new TextField("Descripción Categoria");
        tfCatDescripcion.setRequired(false);
        tfCatDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        tfCatDescripcion.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfCatDescripcion.setClearButtonVisible(true);

        chkCatActivo = new Checkbox("Activo");
        chkCatActivo.setValue(true);

        binder = new BeanValidationBinder<>(CategoriaProducto.class);
        binder.forField(tfCatNombre).asRequired("Digite Nombre de Categoria").bind(CategoriaProducto::getNombre,
                CategoriaProducto::setNombre);
        binder.forField(tfCatDescripcion).bind(CategoriaProducto::getDescripcion,
                CategoriaProducto::setDescripcion);
        binder.forField(chkCatActivo).bind(CategoriaProducto::isActivo,
                CategoriaProducto::setActivo);
        binder.bindInstanceFields(this);

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);

        content.add(titleForm,tfCatNombre,tfCatDescripcion,chkCatActivo, actionsLayout);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setCategoria(CategoriaProducto categoria, String title, String type) {
        binder.setBean(categoria);
        setReadOnlyByDelete(type.equals(ICrudView.OPTION_DELETE));
        btnDelete.setEnabled(!ICrudView.OPTION_ADD.equals(type));
        titleForm.setText(title);
        tfCatNombre.focus();
    }

    private void setReadOnlyByDelete(boolean readOnly) {
        btnSave.setVisible(!readOnly);
        btnDelete.setText(readOnly ? "Sí, Eliminar" : "Eliminar");
        tfCatNombre.setReadOnly(readOnly);
        tfCatDescripcion.setReadOnly(readOnly);
        chkCatActivo.setReadOnly(readOnly);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class CategoriaFormEvent extends ComponentEvent<CategoriaForm> {
        private CategoriaProducto categoria;

        CategoriaFormEvent(CategoriaForm source, CategoriaProducto categoria) {
            super(source, false);
            this.categoria = categoria;
        }

        public CategoriaProducto getCategoria() {
            return categoria;
        }
    }

    public static class SaveEvent extends CategoriaForm.CategoriaFormEvent {
        SaveEvent(CategoriaForm source, CategoriaProducto producto) {
            super(source, producto);
        }
    }

    public static class DeleteEvent extends CategoriaForm.CategoriaFormEvent {
        DeleteEvent(CategoriaForm source, CategoriaProducto producto) {
            super(source, producto);
        }
    }

    public static class CloseEvent extends CategoriaForm.CategoriaFormEvent {
        CloseEvent(CategoriaForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
