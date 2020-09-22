package com.gigti.xfinance.ui.crud.cliente;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ClienteForm extends Dialog {
    private final H2 titleForm;
    private final ComboBox<TipoIde> cbTipoIde;
    private final Button btnDelete;
    private Button btnSave;
    private final Binder<Cliente> binder;

    public ClienteForm(List<TipoIde> tipoIdeList) {

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

        TextField tfIdentificacion = new TextField("N° Identificación");
        tfIdentificacion.setRequired(true);
        tfIdentificacion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfPrimerNombre = new TextField("Primer Nombre");
        tfPrimerNombre.setRequired(true);
        tfPrimerNombre.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoNombre = new TextField("Segundo Nombre");
        tfSegundoNombre.setRequired(false);
        tfSegundoNombre.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfPrimerApellido = new TextField("Primer Apellido");
        tfPrimerApellido.setRequired(true);
        tfPrimerApellido.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoApellido = new TextField("Segundo Apellido");
        tfSegundoApellido.setRequired(false);
        tfSegundoApellido.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfDireccion = new TextField("Dirección");
        tfDireccion.setRequired(false);
        tfDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);
        tfTelefono.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        EmailField tfEmail = new EmailField("Email");
        tfEmail.setClearButtonVisible(false);
        tfEmail.setErrorMessage("Agregue un Email Valido");
        tfEmail.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        binder = new BeanValidationBinder<>(Cliente.class);
        binder.forField(cbTipoIde).asRequired("Seleccione el Tipo de Identificación")
                .bind("persona.tipoIde");
        binder.forField(tfIdentificacion).asRequired("Digite Identificación").bind("persona.identificacion");
        binder.forField(tfPrimerNombre).asRequired("Digite Primer Nombre").bind("persona.primerNombre");
        binder.forField(tfSegundoNombre).bind("persona.segundoNombre");
        binder.forField(tfPrimerApellido).asRequired("Digite Primer Apellido").bind("persona.primerApellido");
        binder.forField(tfSegundoApellido).bind("persona.segundoApellido");
        binder.bind(tfDireccion, "persona.direccion");
        binder.bind(tfTelefono, "persona.telefono");
        binder.forField(tfEmail)
                //.withValidator(email -> StringUtils.isNoneBlank(email) ? new EmailValidator("Ingresa un Email Valido") : true)
                .bind(Cliente::getEmail, Cliente::setEmail);

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new ClienteForm.CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new ClienteForm.DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete,btnClose);

        content.add(titleForm,cbTipoIde,tfIdentificacion,tfPrimerNombre,tfSegundoNombre,tfPrimerApellido, tfSegundoApellido, tfDireccion,tfTelefono, tfEmail, actionsLayout);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    private void validateAndSave() {
        binder.validate();
        if (binder.isValid()) {
            System.out.println("Persona: "+binder.getBean().toString());
            Cliente cliente = binder.getBean();
            System.out.println("Cliente: "+cliente.toString());
            fireEvent(new ClienteForm.SaveEvent(this, cliente));
        } else {
            NotificacionesUtil.showError("Validar Cliente: "+binder.validate().getValidationErrors());
        }
    }

    public void setCliente(Cliente cliente, String title) {
        binder.setBean(cliente);
        titleForm.setText(title);
        cbTipoIde.focus();
        if(cliente != null)
            btnDelete.setEnabled(cliente.getId() != null);
    }

    // Events
    public static abstract class ClienteFormEvent extends ComponentEvent<ClienteForm> {
        private Cliente cliente;

        ClienteFormEvent(ClienteForm source, Cliente cliente) {
            super(source, false);
            this.cliente = cliente;
        }

        public Cliente getCliente() {
            return cliente;
        }
    }

    public static class SaveEvent extends ClienteForm.ClienteFormEvent {
        SaveEvent(ClienteForm source, Cliente cliente) {
            super(source, cliente);
        }
    }

    public static class DeleteEvent extends ClienteForm.ClienteFormEvent {
        DeleteEvent(ClienteForm source, Cliente cliente) {
            super(source, cliente);
        }
    }

    public static class CloseEvent extends ClienteForm.ClienteFormEvent {
        CloseEvent(ClienteForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
