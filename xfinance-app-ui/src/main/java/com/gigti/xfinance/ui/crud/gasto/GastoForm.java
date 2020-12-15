package com.gigti.xfinance.ui.crud.gasto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Gasto;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.math.BigDecimal;

public class GastoForm extends Dialog {
    private final TextField tfDescripcion;
    private BigDecimalField tfValor;
    private DatePicker dpFecha;
    private final Button btnDelete;
    private final H2 titleForm;
    private Button btnSave;
    private final Binder<Gasto> binder;

    public GastoForm() {

        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        titleForm = new H2("");
        titleForm.addClassName("titleView");

        tfDescripcion = new TextField("Descripción");
        tfDescripcion.setRequired(true);
        tfDescripcion.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfDescripcion.setClearButtonVisible(true);
        tfDescripcion.focus();

        dpFecha = new DatePicker();
        dpFecha.setLabel("Fecha");
        dpFecha.setRequired(true);
        dpFecha.setClearButtonVisible(true);
        dpFecha.getElement().setAttribute("theme", "align-center");
        dpFecha.getElement().setAttribute("theme", "small");


        tfValor = new BigDecimalField("Valor Gasto");
        tfValor.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfValor.setReadOnly(false);
        tfValor.setValue(BigDecimal.valueOf(0));

        binder = new BeanValidationBinder<>(Gasto.class);
        binder.forField(tfDescripcion).asRequired("Digite descripción del Gasto").bind(Gasto::getDescripcion,
                Gasto::setDescripcion);
        binder.forField(dpFecha).bind(Gasto::getFechaLD,
                Gasto::setFechaLD);
        binder.forField(tfValor).bind(Gasto::getValor,
                Gasto::setValor);
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

        content.add(titleForm,tfDescripcion,dpFecha,tfValor, actionsLayout);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setGasto(Gasto gasto, String title, String type) {
        binder.setBean(gasto);
        setReadOnlyByDelete(type.equals(ICrudView.OPTION_DELETE));
        btnDelete.setEnabled(!ICrudView.OPTION_ADD.equals(type));
        titleForm.setText(title);
        tfDescripcion.focus();
    }

    private void setReadOnlyByDelete(boolean readOnly) {
        btnSave.setVisible(!readOnly);
        btnDelete.setText(readOnly ? "Sí, Eliminar" : "Eliminar");
        tfDescripcion.setReadOnly(readOnly);
        dpFecha.setReadOnly(readOnly);
        tfValor.setReadOnly(readOnly);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class GastoFormEvent extends ComponentEvent<GastoForm> {
        private Gasto gasto;

        GastoFormEvent(GastoForm source, Gasto gasto) {
            super(source, false);
            this.gasto = gasto;
        }

        public Gasto getGasto() {
            return gasto;
        }
    }

    public static class SaveEvent extends GastoForm.GastoFormEvent {
        SaveEvent(GastoForm source, Gasto gasto) {
            super(source, gasto);
        }
    }

    public static class DeleteEvent extends GastoForm.GastoFormEvent {
        DeleteEvent(GastoForm source, Gasto gasto) {
            super(source, gasto);
        }
    }

    public static class CloseEvent extends GastoForm.GastoFormEvent {
        CloseEvent(GastoForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
