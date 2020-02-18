/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.Usuario;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 * A form for editing a single Usuario Admin.
 */
public class UsuarioForm extends Dialog {

    private Button btnSave;

    private Binder<Usuario> binderUsuario;
    private Binder<Persona> binderPersona;
    private Usuario currentUsuario;

    public UsuarioForm(List<Rol> listRoles) {
        binderUsuario = new BeanValidationBinder<>(Usuario.class);
        //binderUsuario.bindInstanceFields(content);
        binderPersona = new BeanValidationBinder<>(Persona.class);
        //binderPersona.bindInstanceFields(content);

        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Producto");
        content.add(title,3);

        TextField tfUsuario = new TextField("Nombre Usuario");
        tfUsuario.setRequired(true);
        tfUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<Rol> cbRoles = new ComboBox<>();
        cbRoles.setLabel("Rol Usuario");
        cbRoles.setItems(listRoles);
        cbRoles.setItemLabelGenerator(Rol::getNombre);
        cbRoles.setRequired(true);
        cbRoles.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

        ComboBox<TipoIde> cbTipoIdePersona = new ComboBox<>();
        cbTipoIdePersona.setLabel("Tipo Identificación");
        cbTipoIdePersona.setItems(TipoIde.getListTipos());
        cbTipoIdePersona.setRequired(true);
        cbTipoIdePersona.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

        TextField tfIdentificacionPersona = new TextField("N° Identificación");
        tfIdentificacionPersona.setRequired(true);
        tfIdentificacionPersona.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfprimerNombreUsuario = new TextField("Primer Nombre");
        tfprimerNombreUsuario.setRequired(true);
        tfprimerNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoNombreUsuario = new TextField("Segundo Nombre");
        tfSegundoNombreUsuario.setRequired(false);
        tfSegundoNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfPrimerApellidoUsuario = new TextField("Primer Apellido");
        tfPrimerApellidoUsuario.setRequired(true);
        tfPrimerApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoApellidoUsuario = new TextField("Segundo Apellido");
        tfSegundoApellidoUsuario.setRequired(false);
        tfSegundoApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfDireccion = new TextField("Dirección Residencia");
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


        binderUsuario.forField(tfUsuario).asRequired("Digite el Nombre del Usuario").bind(Usuario::getNombreUsuario, Usuario::setNombreUsuario);
        binderUsuario.forField(chkActivo).bind(Usuario::isActivo, Usuario::setActivo);
        binderUsuario.forField(cbRoles).asRequired("Selecciona Un Rol").bind(Usuario::getRol, Usuario::setRol);
        binderUsuario.bindInstanceFields(this);



        binderPersona.forField(cbTipoIdePersona).asRequired("Seleccione el Tipo de Identificación").bind(Persona::getTipoIde, Persona::setTipoIde);
        binderPersona.forField(tfIdentificacionPersona).asRequired("Digite Identificación").bind(Persona::getIdentificacion, Persona::setIdentificacion);
        binderPersona.forField(tfprimerNombreUsuario).asRequired("Digite Nombre").bind(Persona::getPrimerNombre, Persona::setPrimerNombre);
        binderPersona.bind(tfSegundoNombreUsuario, Persona::getSegundoNombre, Persona::setSegundoNombre);
        binderPersona.forField(tfPrimerApellidoUsuario).asRequired("Digite el Primer Apellido").bind(Persona::getPrimerApellido, Persona::setPrimerApellido);
        binderPersona.bind(tfSegundoApellidoUsuario, Persona::getSegundoApellido, Persona::setSegundoApellido);
        binderPersona.bind(tfDireccion, Persona::getDireccion, Persona::setDireccion);
        binderPersona.bind(tfTelefono, Persona::getTelefono, Persona::setTelefono);
        binderPersona.forField(tfEmail).withValidator(new EmailValidator("Ingresa un Email Valido")).asRequired("Digite Dirección").bind(Persona::getEmail, Persona::setEmail);
        binderPersona.bindInstanceFields(this);

         binderUsuario.addStatusChangeListener(event -> {
             btnSave.setEnabled(binderUsuario.isValid() && binderPersona.isValid());
        });

        binderPersona.addStatusChangeListener(event -> {
            btnSave.setEnabled(binderUsuario.isValid() && binderPersona.isValid());
        });

        btnSave = new Button("Guardar");
        btnSave.setWidth("100%");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.setWidth("100%");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnDelete = new Button("Eliminar");
        btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binderUsuario.getBean(), null)));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);

        content.add(tfUsuario, cbRoles,cbTipoIdePersona,tfIdentificacionPersona,tfprimerNombreUsuario,tfSegundoNombreUsuario,
                tfPrimerApellidoUsuario,tfSegundoApellidoUsuario,tfDireccion,tfTelefono, tfEmail, chkActivo, actionsLayout);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setUser(Usuario usuario) {
        binderUsuario.setBean(usuario);
        if(usuario != null){
            binderPersona.setBean(usuario.getPersona());
        }
    }

    private void validateAndSave() {
        if (binderUsuario.isValid() && binderPersona.isValid()) {
            binderUsuario.getBean().setPersona(binderPersona.getBean());
            fireEvent(new SaveEvent(this, binderUsuario.getBean(), binderPersona.getBean()));
        } else {
            Notification.show("Validar Usuario: "+binderUsuario.validate().getValidationErrors(),3000, Notification.Position.TOP_CENTER);
        }
    }

    // Events
    public static abstract class UsuarioFormEvent extends ComponentEvent<UsuarioForm> {
        private Usuario usuario;
        private Persona persona;

        protected UsuarioFormEvent(UsuarioForm source, Usuario usuario, Persona persona) {
            super(source, false);
            this.usuario = usuario;
            this.persona = persona;
        }

        public Usuario getUsuario() {
            return usuario;
        }
        public Persona getPersona() { return persona; }
    }

    public static class SaveEvent extends UsuarioFormEvent {
        SaveEvent(UsuarioForm source, Usuario usuario, Persona persona) {
            super(source, usuario, persona);
        }
    }

    public static class DeleteEvent extends UsuarioFormEvent {
        DeleteEvent(UsuarioForm source, Usuario usuario, Persona persona) {
            super(source, usuario, persona);
        }

    }

    public static class CloseEvent extends UsuarioFormEvent {
        CloseEvent(UsuarioForm source) {
            super(source, null, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
