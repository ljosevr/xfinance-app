/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.gigti.xfinance.backend.data.TipoUsuario;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.authentication.AccessControl;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.authentication.LoginScreen;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
    private Button menu_venta;
    private Accordion menus_varios;
    private Button menu_salir;
    private VerticalLayout menuUsuarios;
    private VerticalLayout menuEmpresas;
    private VerticalLayout menu_productos;
    private Tab tabVentas;
    private Tab tabMenusVarios;
    private Tabs tabsMenuGeneral;
    private VerticalLayout layoutDrawer;

    public MainLayout() {
        String username = "";
        String personname = "";
        String empresaname = "";
        if(CurrentUser.get() != null){
            username = CurrentUser.get().getNombreUsuario();
            personname = CurrentUser.get().getPersona().getCompleteName();
            empresaname = CurrentUser.get().getEmpresa().getNombreEmpresa();
        };

        this.setDrawerOpened(true);

        //Title
        H4 title = new H4("X Finance App");
        title.setClassName("titleBar");

        VerticalLayout vDetail = new VerticalLayout();
        vDetail.add(new Span("Nombre: "+personname));
        vDetail.add(new Span("Empresa: "+empresaname));

        this.addToNavbar(true, new DrawerToggle(), title);
        this.setPrimarySection(Section.DRAWER);
    }

    private void createDrawer(){
        if(CurrentUser.get() != null) {
            layoutDrawer = new VerticalLayout();

            Image logo = new Image("/frontend/images/icon.png", "Logo");
            //logo.addClassName("hide-on-mobile");
            logo.setHeight("45px");
            logo.setWidth("45px");


            Image imgUser = new Image("/frontend/images/IconoUser.png", "Perfil");
            imgUser.setHeight("70px");
            imgUser.setWidth("70px");
            imgUser.getStyle().set("border","1px solid mediumseagreen");
            imgUser.getStyle().set("border-radius","50px");

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(imgUser);
            contextMenu.addItem("Perfil", null);

            H2 titleMenu = new H2("MENU");
            titleMenu.setClassName("titleMenu");

            layoutDrawer.add(logo);
            layoutDrawer.add(imgUser);
            layoutDrawer.add(titleMenu);
            layoutDrawer.add(createMenu());
            this.addToDrawer(layoutDrawer);
        }
    }

    private VerticalLayout createMenu() {
        VerticalLayout layoutMenu = new VerticalLayout();
        layoutMenu.setClassName("menuLayout");
        Usuario user = CurrentUser.get();
        List<Vista> listVista =  user.getRol().getVistas().stream().sorted(Comparator.comparing(Vista::getOrderVista))
                .collect(Collectors.toList());
        for(Vista view : listVista){
            if(view.getVistaPadre() == null) {
                if(view.getSubVistas().isEmpty()){
                    //SOLO MENU
                    Button menu = new Button(view.getNombreVista(), new Icon(VaadinIcon.valueOf(view.getIconMenu())));
                    menu.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                    menu.addClickListener(l -> UI.getCurrent().navigate(view.getRouteVista()));
                    menu.setClassName("menubutton");
                    layoutMenu.add(menu);
                } else {
                  //SubMenu
                    VerticalLayout  layout_menu_sub = new VerticalLayout();
                    if(menus_varios == null) {
                        menus_varios = new Accordion();
                    }

                    for(Vista sub : view.getSubVistas()){
                        if(sub.getVistaPadre().getId().equals(view.getId())) {
                            Button menu_sub = new Button(sub.getNombreVista(), new Icon(VaadinIcon.valueOf(sub.getIconMenu())));
                            menu_sub.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                            menu_sub.addClickListener(l -> UI.getCurrent().navigate(sub.getRouteVista()));
                            menu_sub.setClassName("subMenu-Layout");
                            layout_menu_sub.add(menu_sub);
                        }
                    }
                    menus_varios.add(view.getNombreVista(), layout_menu_sub);
                }
            }
        }
        if(menus_varios != null){
            layoutMenu.add(menus_varios);
        }

//
//
//        menu_venta = new Button("Punto de Venta", VaadinIcon.ANGLE_RIGHT.create());
//        menu_venta.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
//        menu_venta.addClickListener(l -> UI.getCurrent().navigate(PventaView.class));
//        menu_venta.setClassName("menubutton");
//
//        menu_productos = new VerticalLayout();
//        Button submenu_prod_administrar = new Button("Administrar", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_prod_administrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
//        submenu_prod_administrar.addClickListener(l -> UI.getCurrent().navigate(ProductoCrudView.class));
//        submenu_prod_administrar.setClassName("menubutton");
//
//        Button submenu_prod_categoria = new Button("Categorias", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_prod_categoria.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
//        submenu_prod_categoria.addClickListener(l -> UI.getCurrent().navigate(CategoriaView.class));
//        submenu_prod_categoria.setClassName("menubutton");
//
//        Button submenu_prod_compras = new Button("Compras", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_prod_compras.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
//        submenu_prod_compras.addClickListener(l -> UI.getCurrent().navigate(CategoriaView.class));
//        submenu_prod_compras.setClassName("menubutton");
//        //CART_O
//        //STOCK
//        Button submenu_prod_invInicial = new Button("Inventario Inicial", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_prod_invInicial.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
//        submenu_prod_invInicial.addClickListener(l -> UI.getCurrent().navigate(CategoriaView.class));
//        submenu_prod_invInicial.setClassName("menubutton");
//        //STORAGE
//        menu_productos.add(submenu_prod_administrar, submenu_prod_categoria, submenu_prod_compras, submenu_prod_invInicial);
//        //menus_varios.add("Productos", menu_productos).addThemeVariants(DetailsVariant.REVERSE);
//        menus_varios.add("Productos", menu_productos);
//
//        menuUsuarios = new VerticalLayout();
//        Button submenu_usu_administrar = new Button("Administrar", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_usu_administrar.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
//        submenu_usu_administrar.addClickListener(l -> UI.getCurrent().navigate(UsuarioAdminCrudView.class));
//        submenu_usu_administrar.setClassName("menubutton");
//
//        Button submenu_usu_roles = new Button("Roles", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_usu_roles.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
//        submenu_usu_roles.addClickListener(l -> UI.getCurrent().navigate(UsuarioAdminCrudView.class));
//        submenu_usu_roles.setClassName("menubutton");
//
//        menuUsuarios.add(submenu_usu_administrar, submenu_usu_roles);
//        menus_varios.add("Usuarios", menuUsuarios);
//
//        menuEmpresas = new VerticalLayout();
//        Button submenu_emp_administrar = new Button("Administrar", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_emp_administrar.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
//        submenu_emp_administrar.addClickListener(l -> UI.getCurrent().navigate(EmpresaView.class));
//        submenu_emp_administrar.setClassName("menubutton");
//        //OFFICE
//        Button submenu_emp_usuarios = new Button("Usuarios Empresas", new Icon(VaadinIcon.ANGLE_RIGHT));
//        submenu_emp_usuarios.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
//        submenu_emp_usuarios.addClickListener(l -> UI.getCurrent().navigate(EmpresaView.class));
//        submenu_emp_usuarios.setClassName("menubutton");
//        //GROUP
//        menuEmpresas.add(submenu_emp_administrar, submenu_emp_usuarios);
//        menus_varios.add("Empresas", menuEmpresas);

        menu_salir = new Button("Salir", new Icon(VaadinIcon.EXIT));
        menu_salir.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_ICON);
        menu_salir.addClickListener(listener -> signOut());
        menu_salir.setClassName("menubutton");

        layoutMenu.add(menu_salir);
        return layoutMenu;
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
            createDrawer();
            //accessShowMenu();
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
            menu_salir.setEnabled(true);

            if(user.getTipoUsuario().equals(TipoUsuario.ROOT)){
                menuEmpresas.setVisible(true);
                menu_venta.setVisible(false);
                //tabsMenuGeneral.remove(tabVentas);
                menus_varios.remove(menu_productos);
                menus_varios.remove(menuUsuarios);
            } else{
                menu_venta.setVisible(true);
                menu_productos.setVisible(true);
                menuUsuarios.setVisible(true);
                menus_varios.remove(menuEmpresas);
            }
        }
    }

    private void signOut(){
        NotificacionesUtil.openConfirmationDialog("¿Está Seguro de Salir?", true, false);
        Objects.requireNonNull(NotificacionesUtil.getDialogConfirmation()).addDialogCloseActionListener(event -> menu_salir.setEnabled(true));
        NotificacionesUtil.getConfirmButton().addClickListener(event -> {
            menu_salir.setEnabled(false);
            if(NotificacionesUtil.getDialogConfirmation().isOpened())
                NotificacionesUtil.getDialogConfirmation().close();
            AccessControlFactory.getInstance().createAccessControl().signOut();
        });
        NotificacionesUtil.getCancelButton().addClickListener(event -> {
            if(NotificacionesUtil.getDialogConfirmation().isOpened())
                NotificacionesUtil.getDialogConfirmation().close();
            menu_salir.setEnabled(true);
        });
    }
}
