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

import java.util.List;

/**
 * A form for editing a single Usuario Admin.
 */
public class UsuarioForm extends Dialog {

    private FormLayout content;

    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private UsuarioCrudLogic viewLogic;
    private Binder<Usuario> binderUsuario;
    private Binder<Persona> binderPersona;
    private Usuario currentUsuario;
    private Persona currentPersona;
    private List<Rol> listRoles;
    private final ComboBox<Rol> cbRoles;

    public UsuarioForm(UsuarioCrudLogic usuarioCrudLogic, List<Rol> listRoles) {
        content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Producto");
        content.add(title,3);

        viewLogic = usuarioCrudLogic;

        TextField tfUsuario = new TextField("Nombre Usuario");
        tfUsuario.setRequired(true);
        tfUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        cbRoles = new ComboBox<>();
        cbRoles.setLabel("Rol Usuario");
        cbRoles.setItems(listRoles);
        this.listRoles = listRoles;
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

        binderUsuario = new BeanValidationBinder<>(Usuario.class);
        binderUsuario.forField(tfUsuario).asRequired("Digite el Nombre del Usuario").bind(Usuario::getNombreUsuario, Usuario::setNombreUsuario);
        binderUsuario.forField(chkActivo).bind(Usuario::isActivo, Usuario::setActivo);
        binderUsuario.forField(cbRoles).asRequired("Selecciona Un Rol").bind(Usuario::getRol, Usuario::setRol);
        binderUsuario.bindInstanceFields(this);


        binderPersona = new BeanValidationBinder<>(Persona.class);
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

        // enable/disable btnSave button while editing
        binderUsuario.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binderUsuario.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
//            if(hasChanges && isValid)
//                binderUsuario.writeBeanIfValid(currentUsuario);
        });

        binderPersona.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binderPersona.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
            //if(hasChanges && isValid)
            //    binderPersona.writeBeanIfValid(currentPersona);
        });


        btnSave = new Button("Guardar");
        btnSave.setWidth("100%");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
//            if()
//                binderPersona.readBean(currentPersona);
//            if()
//                binderPersona.readBean(currentPersona);
            if (binderUsuario.validate().isOk() && binderUsuario.writeBeanIfValid(binderUsuario.getBean())){
                if(binderPersona.validate().isOk() && binderPersona.writeBeanIfValid(binderPersona.getBean())) {
                    currentUsuario = binderUsuario.getBean();
                    currentUsuario.setPersona(binderPersona.getBean());
                    viewLogic.guardar(currentUsuario);
                }else {
                    Notification.show("Validar Persona: "+binderPersona.validate().getValidationErrors(),3000, Notification.Position.TOP_CENTER);
                }
            } else {
                Notification.show("Validar Usuario: "+binderUsuario.validate().getValidationErrors(),3000, Notification.Position.TOP_CENTER);
            }
        });
        btnSave.addClickShortcut(Key.ENTER);

        btnDiscard = new Button("Descartar Cambios");
        btnDiscard.setWidth("100%");
        btnDiscard.addClickListener(
                event -> viewLogic.editar(currentUsuario));

        Button btnCancel = new Button("Cancelar");
        btnCancel.setWidth("100%");
        btnCancel.addClickListener(event -> viewLogic.cancelar());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelar())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentUsuario != null) {
                viewLogic.eliminar(currentUsuario);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave,btnDiscard);
        HorizontalLayout actionsLayout2 = new HorizontalLayout();
        actionsLayout.add(btnDelete,btnCancel);

        content.add(tfUsuario, cbRoles,cbTipoIdePersona,tfIdentificacionPersona,tfprimerNombreUsuario,tfSegundoNombreUsuario,
                tfPrimerApellidoUsuario,tfSegundoApellidoUsuario,tfDireccion,tfTelefono, tfEmail, chkActivo, actionsLayout, actionsLayout2);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setListRoles(List<Rol> listRoles) {
        this.listRoles = listRoles;
    }

    public void editUsuario(Usuario usuario) {
        if (usuario == null) {
            currentUsuario = new Usuario();
            currentUsuario.setActivo(true);
            currentUsuario.setPersona(new Persona());
            btnDelete.setEnabled(false);
            cbRoles.setEnabled(true);
            cbRoles.setValue(null);
            currentPersona = new Persona();
        } else /*if(StringUtils.isBlank(usuario.getId())){
            usuario.setActivo(true);
            btnDelete.setEnabled(false);
            cbRoles.setEnabled(true);
            cbRoles.setValue(null);
            currentUsuario = usuario;
            currentUsuario.setPersona(new Persona());
        } else*/ {
            btnDelete.setEnabled(true);
            if (usuario.isAdminDefecto()) {
                cbRoles.setEnabled(false);
            } else {
                cbRoles.setEnabled(true);
            }
            currentUsuario = usuario;
            currentPersona = usuario.getPersona();
            cbRoles.setValue(usuario.getRol());
        }
        binderUsuario.readBean(currentUsuario);
        binderPersona.readBean(currentPersona);
    }
}
