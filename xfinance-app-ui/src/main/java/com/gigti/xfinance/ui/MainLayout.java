/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.gigti.xfinance.ui.authentication.AccessControl;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.LoginScreen;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.context.annotation.Primary;

/**
 * The main layout. Contains the navigation menu.
 */
//@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@SpringComponent
@UIScope
@Primary
@Route
@PreserveOnRefresh
@PWA(name = "XFinance App", shortName = "XFinApp", backgroundColor = "#233348", themeColor = "#233348")
@CssImport("./styles/shared-styles.css")
@Theme(value = Lumo.class)
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private MenuB menu;
    final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

    public MainLayout() {
//        setSizeFull();
//        setClassName("main-layout");


        VaadinSession vaadinSession = VaadinSession.getCurrent();
        String username = (String) vaadinSession.getAttribute("username"/*ATTRIBUTE_USERNAME*/);

        addToNavbar(true, createHeader());
        setPrimarySection(Section.NAVBAR);
        //addToDrawer(new MenuB());
        //setDrawerOpened(true);
        //setPrimarySection(Section.DRAWER);
        //addToDrawer();
    }

    private Component createHeader(){
        HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        top.setSpacing(true);
        top.setClassName("menu-header");

        H4 title = new H4("X Finance App");
        title.setClassName("titleBar");
        String resolvedImage = VaadinServletService.getCurrent()
                .resolveResource("webapp/img/table-logo.png",
                        VaadinSession.getCurrent().getBrowser());
        Image img = new Image(resolvedImage, "");
        //img.setHeight("44px");

        menu = new MenuB();

        top.add(img, title, menu);

        return top;
    }

//    private Component createFooter(){
//
//    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        attachEvent.getUI()
                .addShortcutListener(
                        () -> AccessControlFactory.getInstance()
                                .createAccessControl().signOut(),
                        Key.KEY_L, KeyModifier.CONTROL);

        if (!accessControl.isUserSignedIn()) {
            UI.getCurrent().navigate(LoginScreen.class);
        }

        // add the admin view menu item if/when it is registered dynamically
//        Command addAdminMenuItemCommand = () -> getUI().get().navigate(LoginScreen.class);
//        RouteConfiguration sessionScopedConfiguration = RouteConfiguration.forSessionScope();
//        if (sessionScopedConfiguration.isRouteRegistered(CategoriaView_CrudUI.class)) {
//            addAdminMenuItemCommand.execute();
//        } else {
//            sessionScopedConfiguration.addRoutesChangeListener(event -> {
//                for (RouteBaseData data : event.getAddedRoutes()) {
//                    if (data.getNavigationTarget().equals(CategoriaView_CrudUI.class)) {
//                        addAdminMenuItemCommand.execute();
//                    }
//                }
//            });
//        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Validate authenticaion in session
        // Check project source code for an example
        if (!accessControl.isUserSignedIn()) {
            beforeEnterEvent.rerouteTo(LoginScreen.class);
        }else {
            Notification.show("Usuario SI esta logueado");
        }

    }
}
