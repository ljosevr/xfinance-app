/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.IinitBackService;
import com.gigti.xfinance.backend.services.IusuarioService;
import com.gigti.xfinance.ui.HomeView;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UI content when the user is not logged in yet.
 */
@Route(value = "Login")
@PageTitle(value = Constantes.VIEW_MAIN)
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout {

    public static final String VIEW_NAME = "Login";

    private AccessControl accessControl;
    private IinitBackService iinitBackService;
    private IusuarioService iusuarioService;

    @Autowired
    public LoginScreen(IinitBackService init, IusuarioService iusuario) {
        this.iinitBackService = init;
        this.iusuarioService = iusuario;
        iinitBackService.initBackTipos();
        iinitBackService.initBackObjetos();

        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
        UI.getCurrent().getPage().executeJavaScript("document.getElementById(\"vaadinLoginUsername\").focus();");
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setClassName("login-information");

        H1 loginInfoHeader = new H1("X Finance App");
        loginInfoHeader.setWidth("100%");

        H2 loginInfoText = new H2("Iniciar Sesión");
        loginInfoText.setClassName("titleView");

        loginLayout.add(loginInfoHeader);
        //loginLayout.add(loginInfoText);

        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(event -> login(event));//this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Tip: Contacta al Admin del App"));
        loginForm.setI18n(createSpanishI18n());
        loginLayout.add(loginForm);

        add(loginLayout);
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword(), iusuarioService)) {
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
