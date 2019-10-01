/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.services.IinitBackService;
import com.gigti.xfinance.backend.services.IusuarioService;
import com.gigti.xfinance.ui.HomeView;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UI content when the user is not logged in yet.
 */
@Route(value = "Login")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout /*implements HasUrlParameter<String>*/ {

    public static final String VIEW_NAME = "Login";

    private AccessControl accessControl;
    //private String parameter = "";
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
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(event -> login(event));//this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Tip: Contacta al Admin del App"));

        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        // information text about logging in


        add(buildLoginInformation());
        add(loginForm);
    }

    private Component buildLoginInformation() {

        VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        H1 loginInfoHeader = new H1("X Finance App");
        loginInfoHeader.setWidth("100%");
//        Span loginInfoText = new Span(
//                "X Finance App");
//        loginInfoText.setWidth("100%");

        H2 loginInfoText = new H2("X Finance App");
        loginInfoText.setClassName("titleView");

        loginInformation.add(loginInfoHeader);
        //loginInformation.add(loginInfoText);

        return loginInformation;

    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword(), iusuarioService)) {

            //TODO
            //Permisos y Roles
            //registerHomeViewIfApplicable();
            //getUI().get().navigate("");
            UI.getCurrent().navigate(HomeView.class);
        } else {
            event.getSource().setError(true);
        }
    }

    private void registerHomeViewIfApplicable() {
        // register the admin view dynamically only for any admin user logged in
        if (accessControl.isUserInRole(CurrentUser.get())) {
            RouteConfiguration.forSessionScope().setRoute(HomeView.VIEW_NAME,HomeView.class, MainLayout.class);

            // as logout will purge the session route registry, no need to
            // unregister the view on logout
        }
    }

//    @Override
//    public void setParameter(BeforeEvent beforeEvent, String parameter) {
//        this.parameter = parameter;
//    }
}
