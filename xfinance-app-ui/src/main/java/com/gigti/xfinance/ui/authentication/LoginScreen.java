/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.InitBackService;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UI content when the user is not logged in yet.
 */
@Route(value = Constantes.VIEW_R_LOGIN)
@PageTitle(value = Constantes.VIEW_MAIN)
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout {

    public static final String VIEW_NAME = "Login";

    private AccessControl accessControl;
    private InitBackService initBackService;
    private UsuarioService usuarioService;

    @Autowired
    public LoginScreen(InitBackService init, UsuarioService iusuario) {
        this.initBackService = init;
        this.usuarioService = iusuario;
        initBackService.initBackTipos();
        initBackService.initBackObjetos();

        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
        UI.getCurrent().getPage().executeJavaScript("document.getElementById(\"vaadinLoginUsername\").focus();");
    }

    private void buildUI() {
        setClassName("login-screen-flex");

        VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setClassName("login-screen");

        H1 loginInfoHeader = new H1("X Finance App");
        loginInfoHeader.setClassName("h1-login");
        loginLayout.add(loginInfoHeader);

        LoginForm loginForm = new LoginForm();
        loginForm.getElement().setAttribute("border"," 1px solid black");
        loginForm.getElement().setAttribute("border-radius","7px");
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Tip: Contacta al Admin del App"));
        loginForm.setI18n(createSpanishI18n());
        loginLayout.add(loginForm);

        add(loginLayout);
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword(), usuarioService)) {
            UI.getCurrent().navigate(MainLayout.class);
        } else {
            event.getSource().setError(true);
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
