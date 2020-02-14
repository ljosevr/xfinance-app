/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.TopBarComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.List;

/**
 * A view for performing create-read-update-delete operations on Usuario Admin.
 * <p>
 * See also {@link UsuarioCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = Constantes.VIEW_R_USUARIOS, layout = MainLayout.class)
@RouteAlias(value = "usuario", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_USUARIO +" | "+ Constantes.VIEW_MAIN)
public class UsuarioCrudView extends VerticalLayout
        /*implements HasUrlParameter<String> */{

    private UsuarioGrid grid;
    private UsuarioForm form;
    private TextField filter;

    private UsuarioCrudLogic viewLogic;
    private List<Usuario> listaUsuarios;
    private VerticalLayout barAndGridLayout;

    public UsuarioCrudView(UsuarioService iService) {
        addClassName("UsuarioCrudView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        viewLogic = new UsuarioCrudLogic(iService, this);
        HorizontalLayout topLayout = createTopBar();

        grid = new UsuarioGrid();
        listaUsuarios = viewLogic.findAll();
        grid.setItems(listaUsuarios);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        form = new UsuarioForm(viewLogic, viewLogic.findAllRoles());

        H3 title = new H3(Constantes.VIEW_USUARIO);
        title.setClassName("titleView");

        barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        //TODO Realizar Filtro por Categoria Tambien
        filter.setPlaceholder("Filtro Nombre");
        filter.addValueChangeListener(event -> {
            listaUsuarios = viewLogic.setFilter(event.getValue());
            if (listaUsuarios != null)
                grid.setItems(listaUsuarios);
        });
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        filter.focus();

        Button btnNewProduct = new Button("Nuevo");
        btnNewProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewProduct.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewProduct.addClickListener(click -> viewLogic.nuevo());
        btnNewProduct.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        return new TopBarComponent(filter, btnNewProduct);
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void selectRow(Usuario row) {
        grid.getSelectionModel().select(row);
    }

//    @Override
//    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
//        viewLogic.enter(parameter);
//    }

    public UsuarioGrid getGrid() {
        return grid;
    }

    public TextField getFilter() {
        return filter;
    }

    public List<Usuario> getItemsGrid(){
        return listaUsuarios;
    }

    public UsuarioForm getForm() {
        return form;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
}
