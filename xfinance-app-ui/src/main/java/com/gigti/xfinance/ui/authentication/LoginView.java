/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.dto.LoginDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.InitBackService;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.Response;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
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
//public class LoginView extends FlexLayout {
public class LoginView extends VerticalLayout {

    private AccessControl accessControl;
    private UsuarioService usuarioService;
    private Binder<LoginDTO> binder;

    public LoginView(InitBackService init, UsuarioService iusuario) {
        this.usuarioService = iusuario;
        init.initBackTipos();
        init.initBackObjetos();

        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);

        VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 loginInfoHeader = new H1(Constantes.VIEW_MAIN);
        loginInfoHeader.setClassName("h1-login");

        H2 titleLogin = new H2("INICIAR SESIÓN");

        TextField tfCodigoEmpresa = new TextField("Código Empresa");
        TextField tfUsername = new TextField("Usuario");
        PasswordField tfPassword = new PasswordField("Password");

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnIngresar.addClickListener(click ->
                login2(tfCodigoEmpresa.getValue(), tfUsername.getValue(), tfPassword.getValue()));
        btnIngresar.addClickShortcut(Key.ENTER);


        binder = new Binder<>();
        binder.forField(tfCodigoEmpresa).asRequired("Digite Código de la Empresa").bind(LoginDTO::getCodigoEmpresa, LoginDTO::setCodigoEmpresa);
        binder.forField(tfUsername).asRequired("Digite un Nombre de Usuario").bind(LoginDTO::getUserName, LoginDTO::setUserName);
        binder.forField(tfPassword).asRequired("Digite un Password").bind(LoginDTO::getPassword, LoginDTO::setPassword);
        //binder.bindInstanceFields(this);

        binder.addStatusChangeListener(event -> {
            btnIngresar.setEnabled(binder.isValid());
        });

        RouterLink forgot = new RouterLink("Olvide el Password", ForgotPasswordView.class);
        //content.add(tfCodigoEmpresa, tfUsername, tfPassword, btnIngresar);

        //loginLayout.add();
        add(loginInfoHeader, titleLogin, tfCodigoEmpresa, tfUsername, tfPassword, btnIngresar, forgot);
        tfCodigoEmpresa.focus();
    }

    private void login2(String codigoEmpresa, String userName, String password) {
        if(binder.isValid()) {
            Response response = accessControl.signIn(codigoEmpresa, userName, password, usuarioService);
            if (response.isSuccess()) {
                UI.getCurrent().navigate(MainLayout.class);
            } else {
                Notification.show(response.getMessage(),5000, Notification.Position.MIDDLE);
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
