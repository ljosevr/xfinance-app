/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.authentication.AccessControl;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.authentication.LoginView;
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
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The main layout. Contains the navigation menu.
 */
@Route("")
@PWA(
        name = "Tu Punto De Venta",
        shortName = "TPV",
        offlineResources = {
                "./styles/offline.css",
                "./images/offline.png"
        }
)
@CssImport("./styles/shared-styles.css")
@Theme(value = Lumo.class)
//@Theme(value = Material.class)
@PageTitle(value = Constantes.VIEW_MAIN)
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
//@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
    private Accordion menus_varios;
    private Button menu_salir;

    public MainLayout() {

        this.setDrawerOpened(true);
        createHeader();

        this.setPrimarySection(Section.DRAWER);
    }

    private void createHeader(){
        H1 appTitle = new H1("TPV");
        appTitle.addClassName("titlesAppBar");


        menu_salir = new Button("Salir", new Icon(VaadinIcon.EXIT));
        menu_salir.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_ICON);
        menu_salir.addClickListener(listener -> signOut());
        menu_salir.setClassName("menubutton");

        String username = "";
        String personname = "";
        String empresaname = "";
        if(CurrentUser.get() != null){
            username = CurrentUser.get().getNombreUsuario();
            personname = CurrentUser.get().getPersona().getPrimerNombre();
            empresaname = CurrentUser.get().getEmpresa().getNombreEmpresa();
        }

        H1 bienvenida = new H1(empresaname.toUpperCase() + " - Bienvenido: "+personname);
        bienvenida.setClassName("appBarSaludo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), appTitle, bienvenida, menu_salir);

        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER); //
        header.setWidth("100%");
        header.addClassName("header");
        header.expand(appTitle);
        addToNavbar(header);
    }

    private void createDrawer(){
        if(CurrentUser.get() != null) {
            VerticalLayout layoutDrawer = new VerticalLayout();

            Image logo = new Image("/frontend/images/Logo5_2.png", "Logo");
            logo.addClassName("logoDrawer");

            H2 titleMenu = new H2("MENU");
            titleMenu.setClassName("titleMenu");

            layoutDrawer.add(logo);
            layoutDrawer.add(titleMenu);
            layoutDrawer.add(createMenu());
            this.addToDrawer(layoutDrawer);
        }
    }

    private VerticalLayout createMenu() {
        VerticalLayout layoutMenu = new VerticalLayout();
        layoutMenu.setClassName("menuLayout");
        Usuario user = CurrentUser.get();
        List<Vista> listVista =  user.getRol().getVistas().stream()
                .sorted(Comparator.comparing(Vista::getOrderVista))
                .collect(Collectors.toList());

        Map<Vista, List<Vista>> mapMenu = new HashMap<>();
        for(Vista view : listVista) {

            if(view.getVistaPadre() == null) {
                if (view.getSubVistas().isEmpty()) {
                    //SOLO MENU
                    Button menu = new Button(view.getNombreVista(), new Icon(VaadinIcon.valueOf(view.getIconMenu())));
                    menu.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                    menu.addClickListener(l -> UI.getCurrent().navigate(view.getRouteVista()));
                    menu.setClassName("menubutton");
                    layoutMenu.add(menu);
                }
            }

            if (view.getVistaPadre() != null) {
                List<Vista> list = mapMenu.get(view.getVistaPadre());
                if (CollectionUtils.isEmpty(list)) {
                    list = new ArrayList<>();
                    list.add(view);
                    mapMenu.put(view.getVistaPadre(), list);
                } else {
                    list.add(view);
                    mapMenu.putIfAbsent(view.getVistaPadre(), list);
                }
            }
        }

        Map<Vista, List<Vista>> result2 = new LinkedHashMap<>();
        mapMenu.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Vista::getOrderVista)))
                .forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));

        for(Map.Entry<Vista, List<Vista>> entry : result2.entrySet()){
            Vista vista = entry.getKey();
            List<Vista> vistas = entry.getValue();

            VerticalLayout  layout_menu_sub = new VerticalLayout();
            if(menus_varios == null) {
                menus_varios = new Accordion();
            }

            for(Vista sub : vistas){
                Button menu_sub = new Button(sub.getNombreVista(), new Icon(VaadinIcon.valueOf(sub.getIconMenu())));
                menu_sub.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                menu_sub.addClickListener(l -> UI.getCurrent().navigate(sub.getRouteVista()));
                menu_sub.setClassName("subMenu-Layout");
                layout_menu_sub.add(menu_sub);
            }
            menus_varios.add(vista.getNombreVista(), layout_menu_sub);
        }

        if(menus_varios != null){
            layoutMenu.add(menus_varios);
        }

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

        if (accessControl.isUserSignedIn()) {
            createDrawer();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!accessControl.isUserSignedIn()) {
            beforeEnterEvent.forwardTo(LoginView.class);
        }
    }

    private void signOut(){
        NotificacionesUtil.openConfirmationDialog("¿Está Seguro de Salir?", true, false);
        Objects.requireNonNull(NotificacionesUtil.getDialog()).addDialogCloseActionListener(event -> menu_salir.setEnabled(true));
        NotificacionesUtil.getSiButton().addClickListener(event -> {
            menu_salir.setEnabled(false);
            if(NotificacionesUtil.getDialog().isOpened())
                NotificacionesUtil.getDialog().close();
            AccessControlFactory.getInstance().createAccessControl().signOut();
        });
        NotificacionesUtil.getNoButton().addClickListener(event -> {
            if(NotificacionesUtil.getDialog().isOpened())
                NotificacionesUtil.getDialog().close();
            menu_salir.setEnabled(true);
        });
    }
}
