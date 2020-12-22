package com.gigti.xfinance.ui.crud.proveedor;

import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.MyToggleButton;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
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

import java.util.List;

public class ProveedorForm extends Dialog {

    private final H2 titleForm;
    private final ComboBox<TipoIde> cbTipoIde;
    private final EmailField tfEmail;
    private final TextField tfIdentificacion;
    private final TextField tfNombreProveedor;
    private final TextField tfDireccion;
    private final TextField tfTelefono;
    private final Button btnDelete;
    private final MyToggleButton tActivo;
    private Button btnSave;
    private Binder<Proveedor> binder;

    public ProveedorForm(List<TipoIde> tipoIdeList) {
        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        titleForm = new H2("");
        titleForm.addClassName("titleView");

        cbTipoIde = new ComboBox<>();
        cbTipoIde.setLabel("Tipo Identificación");
        cbTipoIde.setItems(tipoIdeList);
        cbTipoIde.setRequired(true);
        cbTipoIde.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

        tfIdentificacion = new TextField("N° Identificación");
        tfIdentificacion.setRequired(true);
        tfIdentificacion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfNombreProveedor = new TextField("Nombre Proveedor");
        tfNombreProveedor.setRequired(true);
        tfNombreProveedor.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfDireccion = new TextField("Dirección");
        tfDireccion.setRequired(false);
        tfDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);
        tfTelefono.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfEmail = new EmailField("Email");
        tfEmail.setClearButtonVisible(true);
        tfEmail.setErrorMessage("Agregue un Email Valido");
        tfEmail.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tActivo = new MyToggleButton();
        tActivo.setLabel("Inactivo");
        tActivo.setValue(false);
        tActivo.getElement().setAttribute("title", "Campo para Activar o Inactivar Categoría");

        tActivo.addValueChangeListener(value -> {
            if(value.getValue()){
                tActivo.setLabel("Activo");
            } else {
                tActivo.setLabel("Inactivo");
            }
        });

        binder = new BeanValidationBinder<>(Proveedor.class);
        binder.forField(tActivo).bind(Proveedor::isActivo, Proveedor::setActivo);
        binder.forField(cbTipoIde).asRequired("Seleccione el Tipo de Identificación").bind(Proveedor::getTipoIde, Proveedor::setTipoIde);
        binder.forField(tfIdentificacion).asRequired("Digite Identificación").bind(Proveedor::getIdentificacion, Proveedor::setIdentificacion);
        binder.forField(tfNombreProveedor).asRequired("Digite Nombre").bind(Proveedor::getNombre, Proveedor::setNombre);
        binder.bind(tfDireccion, Proveedor::getDireccion, Proveedor::setDireccion);
        binder.bind(tfTelefono, Proveedor::getTelefono, Proveedor::setTelefono);
        binder.forField(tfEmail)
                .withValidator(new EmailValidator("Ingresa un Email Valido"))
                .asRequired("Digite Email")
                .bind(Proveedor::getEmail, Proveedor::setEmail);

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new ProveedorForm.CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new ProveedorForm.DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete,btnClose);

        content.add(titleForm, cbTipoIde,tfIdentificacion,tfNombreProveedor, tfDireccion,tfTelefono,
                tfEmail, tActivo, actionsLayout);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new ProveedorForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setProveedor(Proveedor proveedor, String title, String type) {
        binder.setBean(proveedor);
        setReadOnlyByDelete(type.equals(ICrudView.OPTION_DELETE));
        btnDelete.setEnabled(!ICrudView.OPTION_ADD.equals(type));
        titleForm.setText(title);
        cbTipoIde.focus();
    }

    private void setReadOnlyByDelete(boolean readOnly) {
        btnSave.setVisible(!readOnly);
        btnDelete.setText(readOnly ? "Sí, Eliminar" : "Eliminar");
        tfDireccion.setReadOnly(readOnly);
        tfEmail.setReadOnly(readOnly);
        tfIdentificacion.setReadOnly(readOnly);
        tfNombreProveedor.setReadOnly(readOnly);
        tfTelefono.setReadOnly(readOnly);
        tActivo.setReadOnly(readOnly);
        cbTipoIde.setReadOnly(readOnly);
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
