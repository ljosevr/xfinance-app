package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.apache.commons.lang3.StringUtils;

public class EmpresaForm extends FormLayout {

    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private EmpresaCrudLogic viewLogic;
    private Binder<EmpresaDTO> binder;
    private Binder<Usuario> binderUser;
    private EmpresaDTO currentEmpresa;
    private Usuario usuarioAdmin;

    public EmpresaForm(EmpresaCrudLogic empresaCrudLogic) {

        this.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Empresa");
        this.add(title,3);

        viewLogic = empresaCrudLogic;

        TextField tfNombreEmpresa = new TextField("Nombre Empresa");
        tfNombreEmpresa.setRequired(true);
        tfNombreEmpresa.focus();
        tfNombreEmpresa.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<TipoIde> cbTipoIde = new ComboBox<>();
        cbTipoIde.setLabel("Tipo Ide");
        cbTipoIde.setItems(TipoIde.getListTipos());
        cbTipoIde.setRequired(true);

        TextField tfIdentificacion = new TextField("N° Identificación");
        tfIdentificacion.setRequired(true);
        tfIdentificacion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfDireccion = new TextField("Dirección");
        tfDireccion.setRequired(false);
        tfDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);
        tfTelefono.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        Checkbox cbActivo = new Checkbox("Activo");
        cbActivo.setValue(true);
        cbActivo.setRequiredIndicatorVisible(true);

        TextField tfUsuarioAdmin = new TextField("Usuario Admin");
        tfUsuarioAdmin.setRequired(true);
        tfUsuarioAdmin.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<TipoIde> cbTipoIdePersona = new ComboBox<>();
        cbTipoIdePersona.setLabel("Tipo Ide Usuario");
        cbTipoIdePersona.setItems(TipoIde.getListTipos());
        cbTipoIdePersona.setRequired(true);
        cbTipoIdePersona.getElement().setAttribute("theme", String.valueOf(TextFieldVariant.LUMO_SMALL));

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

        binder = new BeanValidationBinder<>(EmpresaDTO.class);
        binder.forField(tfNombreEmpresa).bind(EmpresaDTO::getNombreEmpresa, EmpresaDTO::setNombreEmpresa);
        binder.forField(cbTipoIde).bind(EmpresaDTO::getTipoIde, EmpresaDTO::setTipoIde);
        binder.forField(tfIdentificacion).bind(EmpresaDTO::getIdentificacion, EmpresaDTO::setIdentificacion);
        binder.forField(tfDireccion).bind(EmpresaDTO::getDireccion, EmpresaDTO::setDireccion);
        binder.forField(tfTelefono).bind(EmpresaDTO::getTelefono, EmpresaDTO::setTelefono);
        binder.forField(cbActivo).bind(EmpresaDTO::isActivo, EmpresaDTO::setActivo);
        binder.forField(tfUsuarioAdmin).bind(EmpresaDTO::getUsuarioNombre, EmpresaDTO::setUsuarioNombre);
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
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
        });

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
            if (currentEmpresa != null && binder.writeBeanIfValid(currentEmpresa)) {
                viewLogic.guardar(currentEmpresa);
            }
        });
        btnSave.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        btnDiscard = new Button("Descartar Cambios");
        btnDiscard.addClickListener(
                event -> viewLogic.editar(currentEmpresa));

        Button btnCancel = new Button("Cancelar");
        btnCancel.addClickListener(event -> viewLogic.cancelar());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelar())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentEmpresa != null) {
                viewLogic.eliminar(currentEmpresa);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave,btnDiscard);
        HorizontalLayout actionsLayout2 = new HorizontalLayout();
        actionsLayout.add(btnDelete,btnCancel);
        this.add(tfNombreEmpresa,cbTipoIde,tfIdentificacion,tfDireccion, tfTelefono,cbActivo,
                tfUsuarioAdmin,cbTipoIdePersona, tfIdentificacionPersona,tfprimerNombreUsuario,
                tfSegundoNombreUsuario,tfPrimerApellidoUsuario,
                tfSegundoApellidoUsuario,tfUserDireccion,tfUserPhone,tfUserEmail
                ,actionsLayout,actionsLayout2);
        this.setColspan(actionsLayout, 3);
        this.setColspan(actionsLayout2,2);
    }

    public void edit(EmpresaDTO empresa) {
        if (empresa == null) {
            empresa = new EmpresaDTO();
            empresa.setActivo(true);
            btnDelete.setEnabled(false);
        } else if(StringUtils.isBlank(empresa.getEmpresaId())){
            empresa.setActivo(true);
            btnDelete.setEnabled(false);
        } else {
            btnDelete.setEnabled(true);
        }
        currentEmpresa = empresa;
        binder.readBean(empresa);
    }
}
