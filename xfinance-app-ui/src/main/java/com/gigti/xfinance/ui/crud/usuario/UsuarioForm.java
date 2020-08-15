/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
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
    private final H2 titleForm;
    private final Button btnDelete;
    private final EmailField tfEmail;
    private final Checkbox chkActivo;
    private final ComboBox<Rol> cbRoles;
    private final ComboBox<TipoIde> cbTipoIdePersona;
    private final TextField tfTelefono;
    private final TextField tfIdentificacionPersona;
    private final TextField tfprimerNombreUsuario;
    private final TextField tfSegundoNombreUsuario;
    private final TextField tfPrimerApellidoUsuario;
    private final TextField tfSegundoApellidoUsuario;
    private final TextField tfDireccion;
    private Button btnSave;
    private final Binder<UsuarioDTO> binderUsuario;

    public UsuarioForm(List<Rol> listRoles, List<TipoIde> tipoIdes) {
        binderUsuario = new BeanValidationBinder<>(UsuarioDTO.class);
        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        titleForm = new H2();
        titleForm.addClassName("titleView");


        tfUsuario = new TextField("Nombre Usuario");
        tfUsuario.setRequired(true);
        tfUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfUsuario.focus();

        cbRoles = new ComboBox<>();
        cbRoles.setLabel("Rol Usuario");
        cbRoles.setItems(listRoles);
        cbRoles.setItemLabelGenerator(Rol::getNombre);
        cbRoles.setRequired(true);
        cbRoles.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

        cbTipoIdePersona = new ComboBox<>();
        cbTipoIdePersona.setLabel("Tipo Identificación");
        cbTipoIdePersona.setItems(tipoIdes);
        cbTipoIdePersona.setRequired(true);
        cbTipoIdePersona.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

        tfIdentificacionPersona = new TextField("N° Identificación");
        tfIdentificacionPersona.setRequired(true);
        tfIdentificacionPersona.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfprimerNombreUsuario = new TextField("Primer Nombre");
        tfprimerNombreUsuario.setRequired(true);
        tfprimerNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfSegundoNombreUsuario = new TextField("Segundo Nombre");
        tfSegundoNombreUsuario.setRequired(false);
        tfSegundoNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfPrimerApellidoUsuario = new TextField("Primer Apellido");
        tfPrimerApellidoUsuario.setRequired(true);
        tfPrimerApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfSegundoApellidoUsuario = new TextField("Segundo Apellido");
        tfSegundoApellidoUsuario.setRequired(false);
        tfSegundoApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfDireccion = new TextField("Dirección Residencia");
        tfDireccion.setRequired(false);
        tfDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);
        tfTelefono.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfEmail = new EmailField("Email");
        tfEmail.setClearButtonVisible(true);
        tfEmail.setErrorMessage("Agregue un Email Valido");
        tfEmail.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        chkActivo = new Checkbox("Activo");
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
        binderUsuario.forField(tfEmail).withValidator(new EmailValidator("Ingresa un Email Valido")).asRequired("Digite Email").bind(UsuarioDTO::getEmail, UsuarioDTO::setEmail);
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

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binderUsuario.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);

        content.add(titleForm, tfUsuario, cbRoles,cbTipoIdePersona,tfIdentificacionPersona,tfprimerNombreUsuario,tfSegundoNombreUsuario,
                tfPrimerApellidoUsuario,tfSegundoApellidoUsuario,tfDireccion,tfTelefono, tfEmail, chkActivo, actionsLayout);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setUser(UsuarioDTO usuario, String title, String type) {
        titleForm.setText(title);
        btnDelete.setEnabled(!ICrudView.OPTION_ADD.equals(type));
        setReadOnlyByDelete(type.equals(ICrudView.OPTION_DELETE));
        tfUsuario.focus();
        binderUsuario.setBean(usuario);
    }

    private void setReadOnlyByDelete(boolean readOnly) {
        btnSave.setVisible(!readOnly);
        btnDelete.setText(readOnly ? "Sí, Eliminar" : "Eliminar");
        tfUsuario.setReadOnly(readOnly);
        tfDireccion.setReadOnly(readOnly);
        tfEmail.setReadOnly(readOnly);
        tfIdentificacionPersona.setReadOnly(readOnly);
        tfPrimerApellidoUsuario.setReadOnly(readOnly);
        tfprimerNombreUsuario.setReadOnly(readOnly);
        tfSegundoApellidoUsuario.setReadOnly(readOnly);
        tfSegundoNombreUsuario.setReadOnly(readOnly);
        tfTelefono.setReadOnly(readOnly);
        cbRoles.setReadOnly(readOnly);
        cbTipoIdePersona.setReadOnly(readOnly);
        chkActivo.setReadOnly(readOnly);
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
