package com.gigti.xfinance.ui.crud.proveedor;

import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

public class ProveedorForm extends FormLayout {

    private final H2 titleForm;
    private final ComboBox<TipoIde> cbTipoIde;
    private Button btnSave;
    private Binder<Proveedor> binder;

    public ProveedorForm() {
        setResponsiveSteps(MyResponsiveStep.getMyList());
        addClassName("form");

        titleForm = new H2("");
        titleForm.addClassName("titleView");
        this.add(titleForm,this.getResponsiveSteps().size());

        cbTipoIde = new ComboBox<>();
        cbTipoIde.setLabel("Tipo Identificación");
        cbTipoIde.setItems(TipoIde.getListTipos());
        cbTipoIde.setRequired(true);
        cbTipoIde.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

        TextField tfIdentificacion = new TextField("N° Identificación");
        tfIdentificacion.setRequired(true);
        tfIdentificacion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfNombreProveedor = new TextField("Nombre Proveedor");
        tfNombreProveedor.setRequired(true);
        tfNombreProveedor.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfDireccion = new TextField("Dirección");
        tfDireccion.setRequired(false);
        tfDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);
        tfTelefono.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        EmailField tfEmail = new EmailField("Email");
        tfEmail.setClearButtonVisible(true);
        tfEmail.setErrorMessage("Agregue un Email Valido");
        tfEmail.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        Checkbox chkActivo = new Checkbox("Activo");
        chkActivo.setValue(true);
        chkActivo.setRequiredIndicatorVisible(true);

        binder = new BeanValidationBinder<>(Proveedor.class);
        binder.forField(chkActivo).bind(Proveedor::isActivo, Proveedor::setActivo);
        binder.forField(cbTipoIde).asRequired("Seleccione el Tipo de Identificación").bind(Proveedor::getTipoIde, Proveedor::setTipoIde);
        binder.forField(tfIdentificacion).asRequired("Digite Identificación").bind(Proveedor::getIdentificacion, Proveedor::setIdentificacion);
        binder.forField(tfNombreProveedor).asRequired("Digite Nombre").bind(Proveedor::getNombre, Proveedor::setNombre);
        binder.bind(tfDireccion, Proveedor::getDireccion, Proveedor::setDireccion);
        binder.bind(tfTelefono, Proveedor::getTelefono, Proveedor::setTelefono);
        binder.forField(tfEmail)
                .withValidator(new EmailValidator("Ingresa un Email Valido"))
                .asRequired("Digite Email")
                .bind(Proveedor::getEmail, Proveedor::setEmail);

        binder.addStatusChangeListener(event -> btnSave.setEnabled(binder.isValid()));

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new ProveedorForm.CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new ProveedorForm.DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete,btnClose);
        this.setColspan(actionsLayout, this.getResponsiveSteps().size());

        this.add(cbTipoIde,tfIdentificacion,tfNombreProveedor, tfDireccion,tfTelefono, tfEmail, chkActivo, actionsLayout);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new ProveedorForm.SaveEvent(this, binder.getBean()));
        } else {
            NotificacionesUtil.showError("Validar Proveedor: "+binder.validate().getValidationErrors());
        }
    }

    public void setProveedor(Proveedor proveedor, String title) {
        binder.setBean(proveedor);
        titleForm.setText(title);
        cbTipoIde.focus();
    }

    // Events
    public static abstract class ProveedorFormEvent extends ComponentEvent<ProveedorForm> {
        private Proveedor proveedor;

        ProveedorFormEvent(ProveedorForm source, Proveedor proveedor) {
            super(source, false);
            this.proveedor = proveedor;
        }

        public Proveedor getProveedor() {
            return proveedor;
        }
    }

    public static class SaveEvent extends ProveedorForm.ProveedorFormEvent {
        SaveEvent(ProveedorForm source, Proveedor proveedor) {
            super(source, proveedor);
        }
    }

    public static class DeleteEvent extends ProveedorForm.ProveedorFormEvent {
        DeleteEvent(ProveedorForm source, Proveedor proveedor) {
            super(source, proveedor);
        }
    }

    public static class CloseEvent extends ProveedorForm.ProveedorFormEvent {
        CloseEvent(ProveedorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
