/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.gigti.xfinance.backend.data.TipoUsuario;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.authentication.AccessControl;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.authentication.LoginScreen;
import com.gigti.xfinance.ui.crud.categoria.CategoriaView;
import com.gigti.xfinance.ui.crud.empresa.EmpresaView;
import com.gigti.xfinance.ui.crud.producto.ProductoCrudView;
import com.gigti.xfinance.ui.crud.pventa.PventaView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
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
@SpringComponent
@UIScope
@Primary
@Route("")
//@PreserveOnRefresh
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@PWA(name = "XFinance App", shortName = "XFinApp", backgroundColor = "#233348", themeColor = "#233348")
@CssImport("./styles/shared-styles.css")
@Theme(value = Lumo.class)
@PageTitle(value = Constantes.VIEW_MAIN)
//@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
    private MenuBar menu;
    private MenuItem m_ventas;
    private MenuItem m_productos;
    private MenuItem m_usuarios;
    private MenuItem m_empresas;
    private MenuItem m_salir;

    public MainLayout() {
        //String username = CurrentUser.get() != null ? .getNombreUsuario();

        //this.setDrawerOpened(true);
        //createDrawer();

        Image img = new Image("/frontend/images/logo.png", "Logo");
        img.addClassName("hide-on-mobile");
        img.setHeight("60px");
        img.setWidth("60px");
        //this.addToNavbar(false, img);

        //Title
        H4 title = new H4("X Finance App");
        title.setClassName("titleBar");
        //this.addToNavbar(false, title);

        menu = createMenu();
        this.addToNavbar(true, new DrawerToggle(), img, title, menu);
        this.setPrimarySection(Section.NAVBAR);

        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            if(details.getWindowInnerWidth() < 600){
                img.setVisible(false);
                title.setVisible(false);
                menu.addThemeVariants(MenuBarVariant.LUMO_SMALL);
            } else {
                img.setVisible(true);
                title.setVisible(true);
                menu.addThemeVariants(MenuBarVariant.LUMO_LARGE);
            }
        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            Notification.show(event.getWidth()+"",6000, Notification.Position.BOTTOM_CENTER);
            if(event.getWidth() < 600){
                img.setVisible(false);
                title.setVisible(false);
                menu.addThemeVariants(MenuBarVariant.LUMO_SMALL);
            } else {
                img.setVisible(true);
                title.setVisible(true);
                menu.addThemeVariants(MenuBarVariant.LUMO_LARGE);
            }
        });

    }

//    private void createDrawer(){
//        VerticalLayout layout = new VerticalLayout();
//
//        Image img = new Image("/frontend/images/logo.png", "Logo");
//        img.addClassName("hide-on-mobile");
//        img.setHeight("60px");
//        img.setWidth("60px");
//
//        H4 title = new H4("X Finance App");
//        title.setClassName("titleBar");
//
//        Tab tab1 = new Tab("Prueba");
//
//        Tabs tabs = new Tabs(false, tab1, new Tab("Tab2"),new Tab("Tab3"),new Tab("Tab4"),new Tab("Prueba"), new Tab("Usuario Admin"));
//        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
//        tabs.setAutoselect(true);
//
//        this.addToNavbar(true, new DrawerToggle(), tabs);
//    }



    private MenuBar createMenu() {
        menu = new MenuBar();
        //m_ventas = menu.addItem(new RouterLink("Ventas", PventaView.class));
        m_ventas = menu.addItem("Ventas",event -> getUI().get().navigate(PventaView.class));

        m_productos = menu.addItem("Productos");
        m_usuarios = menu.addItem("Usuarios");
        m_empresas = menu.addItem("Empresas");
        m_salir = menu.addItem("Salir", event -> {
            m_salir.setEnabled(false);
            signOut();});
        SubMenu sm_productos = m_productos.getSubMenu();
        sm_productos.addItem(new RouterLink("Administrar", ProductoCrudView.class));//"Administrar", event -> getUI().get().navigate(ProductoCrudView.class));
        sm_productos.addItem("Compras", event -> getUI().get().navigate("adminProd"));
        sm_productos.addItem(new RouterLink("Categoria", CategoriaView.class));//event -> getUI().get().navigate(CategoriaView_CrudUI.class));
        sm_productos.addItem("Inventario Hoy",event -> getUI().get().navigate("adminProd"));
        sm_productos.addItem("Inv. Inicial",event -> getUI().get().navigate("adminProd"));

        SubMenu sm_usuarios = m_usuarios.getSubMenu();
        sm_usuarios.addItem("Administrar",event -> getUI().get().navigate("adminUserView"));
        sm_usuarios.addItem("Roles",event -> getUI().get().navigate("rolView"));
        sm_usuarios.addItem("Permisos",event -> getUI().get().navigate("permisoView"));

        SubMenu sm_empresas = m_empresas.getSubMenu();
        sm_empresas.addItem(new RouterLink("Administrar", EmpresaView.class));
        //sm_empresas.addItem("Usuario Admin",event -> getUI().get().navigate("adminProd"));
        sm_empresas.addItem("Usuarios Empresas",event -> getUI().get().navigate("adminProd"));

        menu.addThemeVariants(MenuBarVariant.LUMO_PRIMARY);
        return menu;
    }

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
        } else {
            accessShowMenu();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!accessControl.isUserSignedIn()) {
            beforeEnterEvent.rerouteTo(LoginScreen.class);
        }
    }

    private void accessShowMenu() {
        if(CurrentUser.get() != null) {
            Usuario user = CurrentUser.get();
            m_salir.setEnabled(true);
            if(user.getTipoUsuario().equals(TipoUsuario.ROOT)){
                m_empresas.setVisible(true);
                m_ventas.setVisible(false);
                m_productos.setVisible(false);
                m_usuarios.setVisible(false);
            } else{
                m_ventas.setVisible(true);
                m_productos.setVisible(true);
                m_usuarios.setVisible(true);
                m_empresas.setVisible(false);
            }
        }
    }

    private void signOut(){
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.addDialogCloseActionListener(event -> m_salir.setEnabled(true));

        Button confirmButton = new Button("SI", event -> {
            AccessControlFactory.getInstance().createAccessControl().signOut();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        Button cancelButton = new Button("NO", event -> {
            dialog.close();
            m_salir.setEnabled(true);
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Label("¿Está Seguro de Salir?"));
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.add(confirmButton,cancelButton);
        layout.add(hlayout);
        dialog.add(layout);
        dialog.open();
    }
}
