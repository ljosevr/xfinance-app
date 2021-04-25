package com.gigti.xfinance.ui.crud.empresa;

import java.util.List;

import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.EmpresaService;
import com.gigti.xfinance.backend.services.TipoService;
import com.gigti.xfinance.ui.authentication.LoginView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_REGISTRO)
@PageTitle(value = Constantes.VIEW_REGISTRO_EMPRESA +" | "+ Constantes.VIEW_MAIN)
public class RegistroEmpresaView extends VerticalLayout {

    private EmpresaService empresaService;
    private Button btnSave;
    private Binder<EmpresaDTO> binder;
    private List<TipoIde> tipoIdeList;

    public RegistroEmpresaView(EmpresaService empresaService, TipoService tipoService) {
        this.empresaService = empresaService;
        this.setSizeFull();
        this.setDefaultHorizontalComponentAlignment(Alignment.START);
        this.setSizeUndefined();
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setBoxSizing(BoxSizing.CONTENT_BOX);
        tipoIdeList = tipoService.getTiposIdentificacion();

        H1 title = new H1(Constantes.VIEW_REGISTRO_EMPRESA.toUpperCase());
        title.addClassName("titleView2");

        binder = new BeanValidationBinder<>(EmpresaDTO.class);

        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        TextField tfNombreEmpresa = new TextField("Nombre Empresa");
        tfNombreEmpresa.setRequired(true);
        tfNombreEmpresa.focus();
        tfNombreEmpresa.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfCodigoEmpresa = new TextField("Código Empresa");
        tfCodigoEmpresa.setRequired(true);
        tfCodigoEmpresa.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfCodigoEmpresa.setTitle("Codigo de Empresa para Realizar Login. Diferenciar de Otras Empresa");

        ComboBox<TipoIde> cbTipoIde = new ComboBox<>();
        cbTipoIde.setLabel("Tipo Identificación");
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

        TextField tfUsuarioAdmin = new TextField("Usuario Administrador");
        tfUsuarioAdmin.setRequired(true);
        tfUsuarioAdmin.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<TipoIde> cbTipoIdePersona = new ComboBox<>();
        cbTipoIdePersona.setLabel("Tipo Identificación Usuario");
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
        tfSegundoNombreUsuario.setRequired(false);
        tfSegundoNombreUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfPrimerApellidoUsuario = new TextField("Primer Apellido Usuario");
        tfPrimerApellidoUsuario.setRequired(true);
        tfPrimerApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfSegundoApellidoUsuario = new TextField("Segundo Apellido Usuario");
        tfSegundoApellidoUsuario.setRequired(false);
        tfSegundoApellidoUsuario.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfUserDireccion = new TextField("Dirección Usuario");
        tfUserDireccion.setRequired(false);
        tfUserDireccion.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        TextField tfUserPhone = new TextField("Telefono Usuario");
        tfUserPhone.setRequired(true);
        tfUserPhone.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        EmailField tfUserEmail = new EmailField("Email Usuario");
        tfUserEmail.setClearButtonVisible(true);
        tfUserEmail.setErrorMessage("Agregue un Email Valido");
        tfUserEmail.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        binder.forField(tfNombreEmpresa).asRequired("Digite Nombre Empresa")
                .bind(EmpresaDTO::getNombreEmpresa, EmpresaDTO::setNombreEmpresa);

        binder.forField(tfCodigoEmpresa).asRequired("Digite Codigo Empresa")
                .withValidator(codigo -> codigo.length() >= 3,
                        "El Codigo de la Empresa debe tener Minimo 3 Digitos")
                .bind(EmpresaDTO::getCodigoEmpresa, EmpresaDTO::setCodigoEmpresa);

        binder.forField(cbTipoIde).asRequired("Seleccione Tipo de Identificación").bind(EmpresaDTO::getTipoIde, EmpresaDTO::setTipoIde);
        binder.forField(tfIdentificacion).asRequired("Digite Número de Identificación")
                .withValidator(ide -> ide.length() >= 4,
                        "El numero de Identificación de la Empresa debe contener Minimo 4 caracteres")
                .bind(EmpresaDTO::getIdentificacion, EmpresaDTO::setIdentificacion);

        binder.forField(tfDireccion).asRequired("Digite Dirección Empresa").bind(EmpresaDTO::getDireccion, EmpresaDTO::setDireccion);
        binder.forField(tfTelefono).asRequired("Digite Telefono Empresa").bind(EmpresaDTO::getTelefono, EmpresaDTO::setTelefono);
        binder.forField(tfUsuarioAdmin)
                .withValidator(name -> name.length() >= 4,
                        "El Nombre de usuario debe contener Minimo 4 caracteres")
                .bind(EmpresaDTO::getUsuarioNombre, EmpresaDTO::setUsuarioNombre);

        binder.forField(tfprimerNombreUsuario).asRequired("Digite Primer Nombre Usuario").bind(EmpresaDTO::getPrimerNombrePersona, EmpresaDTO::setPrimerNombrePersona);
        binder.forField(tfSegundoNombreUsuario).bind(EmpresaDTO::getSegundoNombrePersona, EmpresaDTO::setSegundoNombrePersona);
        binder.forField(tfPrimerApellidoUsuario).asRequired("Digite Primer Apellido Usuario").bind(EmpresaDTO::getPrimerApellidoPersona, EmpresaDTO::setPrimerApellidoPersona);
        binder.forField(tfSegundoApellidoUsuario).bind(EmpresaDTO::getSegundoApellidoPersona, EmpresaDTO::setSegundoApellidoPersona);
        binder.forField(tfUserDireccion).bind(EmpresaDTO::getDireccionPersona, EmpresaDTO::setDireccionPersona);
        binder.forField(tfUserPhone).asRequired("Digite Telefono Usuario").bind(EmpresaDTO::getTelefonoPersona, EmpresaDTO::setTelefonoPersona);
        binder.forField(tfUserEmail).asRequired("Digite Email de Usuario").bind(EmpresaDTO::getEmailPersona, EmpresaDTO::setEmailPersona);
        binder.forField(cbTipoIdePersona).asRequired("Seleccione Tipo de Identificación").bind(EmpresaDTO::getTipoIdePersona, EmpresaDTO::setTipoIdePersona);
        binder.forField(tfIdentificacionPersona).asRequired("Digite Número de Identificación").bind(EmpresaDTO::getIdentificacionPersona, EmpresaDTO::setIdentificacionPersona);
        binder.setBean(new EmpresaDTO());

        //binder.addStatusChangeListener(event -> btnSave.setEnabled(binder.isValid()));

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickShortcut(Key.ENTER);
        //btnSave.setWidth("100%");
        btnSave.addClickListener(event -> validateAndSave());

        Button btnClose = new Button("Cerrar");
        //btnClose.setWidth("100%");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> goToLogin());
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnClean = new Button("Limpiar");
        //btnClean.setWidth("100%");
        btnClean.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnClean.addClickListener(event -> cleanForm());

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnClean, btnClose);

        content.add(title, tfNombreEmpresa, tfCodigoEmpresa, cbTipoIde, tfIdentificacion, tfDireccion,
                tfTelefono, tfUsuarioAdmin, cbTipoIdePersona, tfIdentificacionPersona,
                tfprimerNombreUsuario, tfSegundoNombreUsuario, tfPrimerApellidoUsuario,
                tfSegundoApellidoUsuario, tfUserDireccion, tfUserPhone, tfUserEmail);

        content.setColspan(title, content.getResponsiveSteps().size() + 1);
        //content.setColspan(actionsLayout, MyResponsiveStep.getMyListForm2Columns().size());
        this.add(content);
        this.add(actionsLayout);
    }

    private void goToLogin() {
        UI.getCurrent().navigate(LoginView.class);
    }

    private void cleanForm() {

    }

    public void validateAndSave() {

        BinderValidationStatus status = binder.validate();
        if(status.hasErrors()) {
            status.notifyBindingValidationStatusHandlers();
        } else {

            binder.getBean().setActivo(false);
            binder.getBean().setActivoUsuario(false);
            binder.getBean().setEliminado(false);

            Response response = empresaService.registerNewEmpresa(binder.getBean());
            if (response.isSuccess()) {
                NotificacionesUtil.showSuccess(response.getMessage());
                goToLogin();
            } else {
                NotificacionesUtil.showError(response.getMessage());
            }
        }
    }

}
