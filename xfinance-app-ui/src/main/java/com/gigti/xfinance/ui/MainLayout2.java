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
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.appreciated.app.layout.entity.Section.HEADER;

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
        },
        enableInstallPrompt = true
)
@CssImport("./styles/shared-styles.css")
@Theme(value = Lumo.class)
//@CssImport("./styles/my-lumo-theme.css")
//@CssImport(value = "./styles/my-button-theme.css", themeFor = "theme for Vaadin Button")
@PageTitle(value = Constantes.VIEW_MAIN)
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
@PreserveOnRefresh
@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout2 extends AppLayoutRouterLayout<LeftLayouts.LeftHybrid> implements RouterLayout, BeforeEnterObserver {
    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
    private Accordion menus_varios;
    private Button menu_salir;

    public MainLayout2() {
        createHeader();
        createMenu();
    }

    private void createHeader(){
        H1 appTitle = new H1("TPV");
        appTitle.addClassName("titlesAppBar");

        menu_salir = new Button("Salir", new Icon(VaadinIcon.EXIT));
        menu_salir.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_ICON);
        menu_salir.addClickListener(listener -> signOut());
        menu_salir.setClassName("menubutton");


    }

    private void createMenu() {

        String username = "";
        String personname = "";
        String empresaname = "";
        if(CurrentUser.get() != null){
            username = CurrentUser.get().getNombreUsuario();
            personname = CurrentUser.get().getPersona().getPrimerNombre();
            empresaname = CurrentUser.get().getEmpresa().getNombreEmpresa();
        }

        Component appBar = AppBarBuilder.get()
                .add(new HorizontalLayout(new Span("Bienvenido: "+personname),
                        new Span("Empresa: "+ empresaname), menu_salir))
                .build();

        Usuario user = CurrentUser.get();
        System.out.println(user);
        System.out.println(user.getRol());

        List<Vista> listVista =  user.getRol().getVistas().stream()
                .sorted(Comparator.comparing(Vista::getOrderVista))
                .collect(Collectors.toList());
        System.out.println(listVista);
        Component appMenu = null;
        LeftAppMenuBuilder appMenuBuilder = LeftAppMenuBuilder.get()
                .addToSection(HEADER,
                        new LeftHeaderItem(null, null, "/frontend/images/Logo5.png")
                );

        for(Vista view : listVista) {
            try {
                if (view.getVistaPadre() == null) {
                    if (view.getSubVistas().size() == 0) {
                        try {
                            Class<?> cl = Class.forName(view.getRouteVista());
                            appMenuBuilder.add(new LeftNavigationItem(view.getNombreVista(), new Icon(VaadinIcon.valueOf(view.getIconMenu())), (Class<? extends Component>) cl));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    if (view.getSubVistas().size() > 0) {
                        LeftSubMenuBuilder subMenu = LeftSubMenuBuilder.get(view.getNombreVista(), new Icon(VaadinIcon.valueOf(view.getIconMenu())));
                        List<Vista> listTemp = getSubMenu(view, listVista);
                        for (Vista v : listTemp) {
                            subMenu.add(new LeftNavigationItem(v.getNombreVista(), new Icon(VaadinIcon.valueOf(v.getIconMenu())), (Class<? extends Component>) Class.forName(v.getRouteVista())));
                        }
                        appMenuBuilder.add(subMenu.build());
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        appMenu = appMenuBuilder.build();

        init(AppLayoutBuilder
                .get(LeftLayouts.LeftHybrid.class)
                .withTitle("TPV")
                .withAppBar(appBar)
                .withAppMenu(appMenu)
                .build());

    }

    private List<Vista> getSubMenu(Vista view, List<Vista> listVista) {
        return listVista.stream()
                .filter(v -> {
                    if(v.getVistaPadre() != null) {
                        return v.getVistaPadre().getId().equals(view.getId());
                    }
                    return false;
                })
                .collect(Collectors.toList());
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
