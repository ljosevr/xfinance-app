/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.dto.LoginDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.InitBackService;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

/**
 * UI content when the user is not logged in yet.
 */
@Route(value = Constantes.VIEW_R_LOGIN)
@PageTitle(value = Constantes.VIEW_LOGIN +" | "+ Constantes.VIEW_MAIN)
@CssImport("./styles/shared-styles.css")
public class LoginView extends VerticalLayout {

    private AccessControl accessControl;
    private UsuarioService usuarioService;
    private Binder<LoginDTO> binder;

    public LoginView(InitBackService init, UsuarioService iusuario) {
        this.usuarioService = iusuario;
        init.initBackTipos();
        init.initBackObjetos();
        init.initBackDemo();

        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.setSizeUndefined();
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.addClassName("panel-principal-login");

        VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        loginLayout.setBoxSizing(BoxSizing.CONTENT_BOX);
        loginLayout.addClassName("login-panel");
        loginLayout.setSizeUndefined();

        Image logo = new Image("/frontend/images/logo.png", "Logo");
        logo.addClassName("logoLogin");

        H2 titleLogin = new H2("INICIAR SESIÓN");

        TextField tfCodigoEmpresa = new TextField();
        tfCodigoEmpresa.setPrefixComponent(new Icon(VaadinIcon.FACTORY));
        tfCodigoEmpresa.setPlaceholder("Código Empresa");

        TextField tfUsername = new TextField();
        tfUsername.setPrefixComponent(new Icon(VaadinIcon.USER));
        tfUsername.setPlaceholder("Usuario");

        PasswordField tfPassword = new PasswordField();
        tfPassword.setPrefixComponent(new Icon(VaadinIcon.PASSWORD));
        tfPassword.setPlaceholder("Password");

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnIngresar.addClickListener(click ->
                login(tfCodigoEmpresa.getValue(), tfUsername.getValue(), tfPassword.getValue()));
        btnIngresar.addClickShortcut(Key.ENTER);

        binder = new Binder<>();
        binder.forField(tfCodigoEmpresa).asRequired("Digite Código de la Empresa").bind(LoginDTO::getCodigoEmpresa, LoginDTO::setCodigoEmpresa);
        binder.forField(tfUsername).asRequired("Digite un Nombre de Usuario").bind(LoginDTO::getUserName, LoginDTO::setUserName);
        binder.forField(tfPassword).asRequired("Digite un Password").bind(LoginDTO::getPassword, LoginDTO::setPassword);

        RouterLink forgot = new RouterLink("Olvide el Password", ForgotPasswordView.class);

        loginLayout.add(titleLogin, tfCodigoEmpresa, tfUsername, tfPassword, btnIngresar, forgot);
        this.add(logo, loginLayout);
        tfCodigoEmpresa.focus();
    }

    private void login(String codigoEmpresa, String userName, String password) {
        if(binder.isValid()) {
            Response response = accessControl.signIn(codigoEmpresa, userName, password, usuarioService);
            if (response.isSuccess()) {
                UI.getCurrent().navigate(MainLayout.class);
            } else {
                NotificacionesUtil.showError(response.getMessage());
            }
        } else {
            binder.validate();
        }
    }

    private LoginI18n createSpanishI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("X Finance App");
        i18n.getHeader().setDescription("Aplicativo Contable Multi-Dispositivo");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Iniciar Sesión");
        i18n.getForm().setSubmit("Ingresar");
        i18n.getForm().setPassword("Clave");
        i18n.getForm().setForgotPassword("Olvide Mi clave");
        i18n.getErrorMessage().setTitle("Usuario/Clave Invalido");
        i18n.getErrorMessage().setMessage("Confirma tu usuario y Contraseña e Intente nuevamente.");
//        i18n.setAdditionalInformation(
//                "Caso necessite apresentar alguma informação extra para o usuário"
//                        + " (como credenciais padrão), este é o lugar.");
        return i18n;
    }
}
