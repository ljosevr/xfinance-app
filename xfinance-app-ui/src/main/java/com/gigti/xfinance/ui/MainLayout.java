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
import com.gigti.xfinance.ui.crud.Categorias.CategoriaView;
import com.gigti.xfinance.ui.crud.Empresa.EmpresaView;
import com.gigti.xfinance.ui.crud.producto.ProductoCrudView;
import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.context.annotation.Primary;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

/**
 * The main layout. Contains the navigation menu.
 */
@SpringComponent
@UIScope
@Primary
//@Route("")
//@PreserveOnRefresh
@PWA(name = "XFinance App", shortName = "XFinApp", backgroundColor = "#233348", themeColor = "#233348")
@CssImport("./styles/shared-styles.css")
@Theme(value = Lumo.class)
@PageTitle(value = Constantes.VIEW_MAIN)
@Push
public class MainLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> implements RouterLayout, BeforeEnterObserver {
    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge;
    private LeftNavigationItem menuPVenta;
    private LeftNavigationItem menuAdminEmpresa;
    private LeftNavigationItem menuCrearAdminUserEmpresa;
    private LeftSubMenuBuilder menuEmpresas;
    private LeftNavigationItem menuUserAdmin;
    private LeftNavigationItem menuUserRol;
    private LeftNavigationItem menuUserPermisos;
    private LeftNavigationItem menuProdAdmin;
    private LeftNavigationItem menuProdCompras;
    private LeftNavigationItem menuProdCategoria;
    private LeftNavigationItem menuProdInvHoy;
    private LeftNavigationItem menuProdInvInicial;

    public MainLayout() {
        createMenu();
    }

    private void createMenu() {
        //TODO PERMISOS
        LeftAppMenuBuilder leftAppMenuBuilder = LeftAppMenuBuilder.get()
                .addToSection(HEADER,
                        new LeftHeaderItem("MENU", "", "/frontend/images/logo.png"));

        menuPVenta = new LeftNavigationItem("P. Venta", VaadinIcon.CART, ProductoCrudView.class);
        //badge.bind(menuPVenta.getBadge());

        leftAppMenuBuilder.add(menuPVenta);

        LeftSubMenuBuilder menuProductos = LeftSubMenuBuilder.get("Productos", VaadinIcon.ABACUS.create());
        menuProdAdmin = new LeftNavigationItem("Administrar", VaadinIcon.CLUSTER.create(), ProductoCrudView.class);
        menuProductos.add(menuProdAdmin);
        menuProdCompras = new LeftNavigationItem("Compras", VaadinIcon.CART.create(), ProductoCrudView.class);
        menuProductos.add(menuProdCompras);
        menuProdCategoria = new LeftNavigationItem("Categorias", VaadinIcon.BOOK.create(), CategoriaView.class);
        menuProductos.add(menuProdCategoria);
        menuProdInvHoy = new LeftNavigationItem("Inventario Hoy", VaadinIcon.FILE_TEXT_O.create(), ProductoCrudView.class);
        menuProductos.add(menuProdInvHoy);
        menuProdInvInicial = new LeftNavigationItem("Inventario Inicial", VaadinIcon.FILE_START.create(), ProductoCrudView.class);
        menuProductos.add(menuProdInvInicial);
        leftAppMenuBuilder.add(menuProductos.build());

        LeftSubMenuBuilder menuUsuarios = LeftSubMenuBuilder.get("Usuarios", VaadinIcon.USERS.create());
        menuUserAdmin = new LeftNavigationItem("Adminisitrar", VaadinIcon.USERS.create(), ProductoCrudView.class);
        menuUsuarios.add(menuUserAdmin);
        menuUserRol = new LeftNavigationItem("Roles", VaadinIcon.CONTROLLER.create(), ProductoCrudView.class);
        menuUsuarios.add(menuUserRol);
        menuUserPermisos = new LeftNavigationItem("Permisos", VaadinIcon.ACCESSIBILITY.create(), CategoriaView.class);
        menuUsuarios.add(menuUserPermisos);

        leftAppMenuBuilder.add(menuUsuarios.build());

        badge = new DefaultBadgeHolder(5);
        notifications.addClickListener(notification -> {/* ... */});

        // Usuario Root
        menuEmpresas = LeftSubMenuBuilder.get("Empresas", VaadinIcon.BUILDING.create());
        menuAdminEmpresa = new LeftNavigationItem("Adminisitrar", VaadinIcon.BUILDING_O.create(), EmpresaView.class);
        menuEmpresas.add(menuAdminEmpresa);
        menuCrearAdminUserEmpresa = new LeftNavigationItem("Crear Usuario Admin", VaadinIcon.USER.create(), ProductoCrudView.class);
        menuEmpresas.add(menuCrearAdminUserEmpresa);
        leftAppMenuBuilder.add(menuEmpresas.build());

        leftAppMenuBuilder.addToSection(FOOTER, new LeftClickableItem("Salir", VaadinIcon.EXIT.create(), clickEvent -> signOut()));

        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("X Finance App")
                .withAppBar(AppBarBuilder.get()
                        .add(new Label((CurrentUser.get() != null ? CurrentUser.get().getNombreUsuario() + " - " + CurrentUser.get().getEmpresa().getNombreEmpresa() : "NO CARGO")))
                        .add(new NotificationButton<>(VaadinIcon.BELL, notifications))
                        .build())
                .withAppMenu(leftAppMenuBuilder.build())
                .build());
    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
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

    private void signOut(){
        Dialog dialog = new Dialog(new Label("¿Está Seguro de Salir?"));

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        NativeButton confirmButton = new NativeButton("Confirmar", event -> {
            AccessControlFactory.getInstance().createAccessControl().signOut();
            dialog.close();
        });
        NativeButton cancelButton = new NativeButton("Cancelar", event -> {
            dialog.close();
        });
        dialog.add(confirmButton, cancelButton);
        dialog.open();
    }

    private void accessShowMenu() {
        if(CurrentUser.get() != null) {
            Usuario user = CurrentUser.get();

            if(user.getTipoUsuario().equals(TipoUsuario.ROOT)){
                menuPVenta.setVisible(false);
                menuAdminEmpresa.setVisible(true);
                menuCrearAdminUserEmpresa.setVisible(false);
                menuUserAdmin.setVisible(false);
                menuUserRol.setVisible(false);
                menuUserPermisos.setVisible(false);
                menuProdAdmin.setVisible(false);
                menuProdCompras.setVisible(false);
                menuProdCategoria.setVisible(false);
                menuProdInvHoy.setVisible(false);
                menuProdInvInicial.setVisible(false);
            } else{
                menuPVenta.setVisible(true);
                menuAdminEmpresa.setVisible(false);
                menuCrearAdminUserEmpresa.setVisible(false);
                menuUserAdmin.setVisible(true);
                menuUserRol.setVisible(true);
                menuUserPermisos.setVisible(true);
                menuProdAdmin.setVisible(true);
                menuProdCompras.setVisible(true);
                menuProdCategoria.setVisible(true);
                menuProdInvHoy.setVisible(true);
                menuProdInvInicial.setVisible(true);
            }
        }
    }
}
