package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
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
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class EmpresaMasterForm extends Dialog {

    private Button btnSave;
    private Binder<EmpresaDTO> binder;

    public EmpresaMasterForm(List<TipoIde> tipoIdeList) {

        binder = new BeanValidationBinder<>(EmpresaDTO.class);

        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        H2 title = new H2("Crear o Editar Empresa");
        title.addClassName("titleView");
        content.add(title, 3);

        TextField tfNombreEmpresa = new TextField("Nombre Empresa");
        tfNombreEmpresa.setRequired(true);
        tfNombreEmpresa.focus();
        tfNombreEmpresa.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfCodigoEmpresa = new TextField("Código");
        tfCodigoEmpresa.setRequired(true);
        tfCodigoEmpresa.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<TipoIde> cbTipoIde = new ComboBox<>();
        cbTipoIde.setLabel("Tipo Ide");
        cbTipoIde.setItems(tipoIdeList);
        cbTipoIde.setRequired(true);
        cbTipoIde.getElement().setAttribute("theme", "small");

        TextField tfIdentificacion = new TextField("N° Identificación");
        tfIdentificacion.setRequired(true);
        tfIdentificacion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfDireccion = new TextField("Dirección");
        tfDireccion.setRequired(false);
        tfDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);
        tfTelefono.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        Checkbox chkActivo = new Checkbox("Activo");
        chkActivo.setValue(true);
        chkActivo.setRequiredIndicatorVisible(true);
        cbTipoIde.getElement().setAttribute("theme", "small");

        TextField tfUsuarioAdmin = new TextField("Usuario Admin");
        tfUsuarioAdmin.setRequired(true);
        tfUsuarioAdmin.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<TipoIde> cbTipoIdePersona = new ComboBox<>();
        cbTipoIdePersona.setLabel("Tipo Ide Usuario");
        cbTipoIdePersona.setItems(tipoIdeList);
        cbTipoIdePersona.setRequired(true);
        //cbTipoIdePersona.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));
        cbTipoIdePersona.getElement().setAttribute("theme", "small");

        TextField tfIdentificacionPersona = new TextField("N° Identificación Usuario");
        tfIdentificacionPersona.setRequired(true);
        tfIdentificacionPersona.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfprimerNombreUsuario = new TextField("Primer Nombre Usuario");
        tfprimerNombreUsuario.setRequired(true);
        tfprimerNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoNombreUsuario = new TextField("Segundo Nombre Usuario");
        tfSegundoNombreUsuario.setRequired(true);
        tfSegundoNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfPrimerApellidoUsuario = new TextField("Primer Apellido Usuario");
        tfPrimerApellidoUsuario.setRequired(true);
        tfPrimerApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoApellidoUsuario = new TextField("Segundo Apellido Usuario");
        tfSegundoApellidoUsuario.setRequired(true);
        tfSegundoApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfUserDireccion = new TextField("Dirección Usuario");
        tfUserDireccion.setRequired(true);
        tfUserDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfUserPhone = new TextField("Telefono Usuario");
        tfUserPhone.setRequired(true);
        tfUserPhone.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        EmailField tfUserEmail = new EmailField("Email Usuario");
        tfUserEmail.setClearButtonVisible(true);
        tfUserEmail.setErrorMessage("Agregue un Email Valido");
        tfUserEmail.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        binder.forField(tfNombreEmpresa)
                .withValidator(name -> name.length() >= 4,
                        "El Nombre de la Empresa debe contener Minimo 4 caracteres")
                .bind(EmpresaDTO::getNombreEmpresa, EmpresaDTO::setNombreEmpresa);

        binder.forField(tfCodigoEmpresa)
                .withValidator(codigo -> codigo.length() >= 3 && codigo.length() <= 6,
                        "El Codigo de la Empresa debe tener entre 3 y 6 caracteres")
                .bind(EmpresaDTO::getCodigoEmpresa, EmpresaDTO::setCodigoEmpresa);

        binder.forField(cbTipoIde).bind(EmpresaDTO::getTipoIde, EmpresaDTO::setTipoIde);
        binder.forField(tfIdentificacion)
                .withValidator(ide -> ide.length() >= 4,
                        "El numero de Identificación de la Empresa debe contener Minimo 4 caracteres")
                .bind(EmpresaDTO::getIdentificacion, EmpresaDTO::setIdentificacion);

        binder.forField(tfDireccion).bind(EmpresaDTO::getDireccion, EmpresaDTO::setDireccion);
        binder.forField(tfTelefono).bind(EmpresaDTO::getTelefono, EmpresaDTO::setTelefono);
        binder.forField(chkActivo).bind(EmpresaDTO::isActivo, EmpresaDTO::setActivo);
        binder.forField(tfUsuarioAdmin)
                .withValidator(name -> name.length() >= 4,
                        "El Nombre de usuario debe contener Minimo 4 caracteres")
                .bind(EmpresaDTO::getUsuarioNombre, EmpresaDTO::setUsuarioNombre);
        binder.forField(tfprimerNombreUsuario).bind(EmpresaDTO::getPrimerNombrePersona, EmpresaDTO::setPrimerNombrePersona);
        binder.forField(tfSegundoNombreUsuario).bind(EmpresaDTO::getSegundoNombrePersona, EmpresaDTO::setSegundoNombrePersona);
        binder.forField(tfPrimerApellidoUsuario).bind(EmpresaDTO::getPrimerApellidoPersona, EmpresaDTO::setPrimerApellidoPersona);
        binder.forField(tfSegundoApellidoUsuario).bind(EmpresaDTO::getSegundoApellidoPersona, EmpresaDTO::setSegundoApellidoPersona);
        binder.forField(tfUserDireccion).bind(EmpresaDTO::getDireccionPersona, EmpresaDTO::setDireccionPersona);
        binder.forField(tfUserPhone).bind(EmpresaDTO::getTelefonoPersona, EmpresaDTO::setTelefonoPersona);
        binder.forField(tfUserEmail).bind(EmpresaDTO::getEmailPersona, EmpresaDTO::setEmailPersona);
        binder.forField(cbTipoIdePersona).bind(EmpresaDTO::getTipoIdePersona, EmpresaDTO::setTipoIdePersona);
        binder.forField(tfIdentificacionPersona).bind(EmpresaDTO::getIdentificacionPersona, EmpresaDTO::setIdentificacionPersona);
        binder.bindInstanceFields(this);

        binder.addStatusChangeListener(event -> {
            btnSave.setEnabled(binder.isValid());
        });

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickShortcut(Key.ENTER);

        btnSave.addClickListener(event -> validateAndSave());

        Button btnClose = new Button("Cerrar");
        btnClose.setWidth("100%");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnDelete = new Button("Eliminar");
        btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);

        content.add(tfNombreEmpresa, tfCodigoEmpresa, cbTipoIde, tfIdentificacion, tfDireccion, tfTelefono, chkActivo,
                tfUsuarioAdmin, cbTipoIdePersona, tfIdentificacionPersona, tfprimerNombreUsuario,
                tfSegundoNombreUsuario, tfPrimerApellidoUsuario,
                tfSegundoApellidoUsuario, tfUserDireccion, tfUserPhone, tfUserEmail
                , actionsLayout);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setEmpresa(EmpresaDTO empresa) {
        binder.setBean(empresa);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        } else {
            Notification.show("Validar Datos: " + binder.validate().getValidationErrors(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    // Events
    public static abstract class EmpresaFormEvent extends ComponentEvent<EmpresaMasterForm> {
        private EmpresaDTO empresaDTO;

        protected EmpresaFormEvent(EmpresaMasterForm source, EmpresaDTO empresaDTO) {
            super(source, false);
            this.empresaDTO = empresaDTO;
        }

        public EmpresaDTO getEmpresaDTO() {
            return empresaDTO;
        }
    }

    public static class SaveEvent extends EmpresaFormEvent {
        SaveEvent(EmpresaMasterForm source, EmpresaDTO empresaDTO) {
            super(source, empresaDTO);
        }
    }

    public static class DeleteEvent extends EmpresaFormEvent {
        DeleteEvent(EmpresaMasterForm source, EmpresaDTO empresaDTO) {
            super(source, empresaDTO);
        }
    }

    public static class CloseEvent extends EmpresaFormEvent {
        CloseEvent(EmpresaMasterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}