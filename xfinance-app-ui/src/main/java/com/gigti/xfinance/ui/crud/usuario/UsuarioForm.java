/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
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
import java.util.logging.Logger;

public class UsuarioForm extends Dialog {

    private static final Logger logger = Logger.getLogger(UsuarioForm.class.getName());
    private final TextField tfUsuario;
    private Button btnSave;

    private Binder<UsuarioDTO> binderUsuario;
    public UsuarioForm(List<Rol> listRoles) {
        binderUsuario = new BeanValidationBinder<>(UsuarioDTO.class);
        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        H2 title = new H2("Crear o Editar Usuario");
        title.addClassName("titleView");
        content.add(title,3);

        tfUsuario = new TextField("Nombre Usuario");
        tfUsuario.setRequired(true);
        tfUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfUsuario.focus();

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

        binderUsuario.forField(tfUsuario).asRequired("Digite el Nombre del Usuario").bind(UsuarioDTO::getNombreUsuario, UsuarioDTO::setNombreUsuario);
        binderUsuario.forField(chkActivo).bind(UsuarioDTO::isActivo, UsuarioDTO::setActivo);
        binderUsuario.forField(cbRoles).asRequired("Selecciona Un Rol").bind(UsuarioDTO::getRol, UsuarioDTO::setRol);

        binderUsuario.forField(cbTipoIdePersona).asRequired("Seleccione el Tipo de Identificación").bind(UsuarioDTO::getTipoIde, UsuarioDTO::setTipoIde);
        binderUsuario.forField(tfIdentificacionPersona).asRequired("Digite Identificación").bind(UsuarioDTO::getIdentificacion, UsuarioDTO::setIdentificacion);
        binderUsuario.forField(tfprimerNombreUsuario).asRequired("Digite Nombre").bind(UsuarioDTO::getPrimerNombre, UsuarioDTO::setPrimerNombre);
        binderUsuario.bind(tfSegundoNombreUsuario, UsuarioDTO::getSegundoNombre, UsuarioDTO::setSegundoNombre);
        binderUsuario.forField(tfPrimerApellidoUsuario).asRequired("Digite el Primer Apellido").bind(UsuarioDTO::getPrimerApellido, UsuarioDTO::setPrimerApellido);
        binderUsuario.bind(tfSegundoApellidoUsuario, UsuarioDTO::getSegundoApellido, UsuarioDTO::setSegundoApellido);
        binderUsuario.bind(tfDireccion, UsuarioDTO::getDireccion, UsuarioDTO::setDireccion);
        binderUsuario.bind(tfTelefono, UsuarioDTO::getTelefono, UsuarioDTO::setTelefono);
        binderUsuario.forField(tfEmail).withValidator(new EmailValidator("Ingresa un Email Valido")).asRequired("Digite Dirección").bind(UsuarioDTO::getEmail, UsuarioDTO::setEmail);
        //binderPersona.bindInstanceFields(this);

        binderUsuario.addStatusChangeListener(event -> {
             btnSave.setEnabled(binderUsuario.isValid());
        });

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binderUsuario.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);

        content.add(tfUsuario, cbRoles,cbTipoIdePersona,tfIdentificacionPersona,tfprimerNombreUsuario,tfSegundoNombreUsuario,
                tfPrimerApellidoUsuario,tfSegundoApellidoUsuario,tfDireccion,tfTelefono, tfEmail, chkActivo, actionsLayout);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setUser(UsuarioDTO usuario) {
        tfUsuario.focus();
        binderUsuario.setBean(usuario);
    }

    private void validateAndSave() {
        logger.info("validateAndSave");
        if (binderUsuario.isValid()) {
            logger.info("usuario: "+binderUsuario.isValid() + " - "+binderUsuario.getBean());
            fireEvent(new SaveEvent(this, binderUsuario.getBean()));
        } else {
            Notification.show("Validar Usuario: "+binderUsuario.validate().getValidationErrors(),3000, Notification.Position.TOP_CENTER);
        }
    }

    // Events
    public static abstract class UsuarioFormEvent extends ComponentEvent<UsuarioForm> {
        private UsuarioDTO usuario;

        protected UsuarioFormEvent(UsuarioForm source, UsuarioDTO usuario) {
            super(source, false);
            this.usuario = usuario;
        }

        public UsuarioDTO getUsuario() {
            return usuario;
        }
    }

    public static class SaveEvent extends UsuarioFormEvent {
        SaveEvent(UsuarioForm source, UsuarioDTO usuario) {
            super(source, usuario);
        }
    }

    public static class DeleteEvent extends UsuarioFormEvent {
        DeleteEvent(UsuarioForm source, UsuarioDTO usuario) {
            super(source, usuario);
        }
    }

    public static class CloseEvent extends UsuarioFormEvent {
        CloseEvent(UsuarioForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
