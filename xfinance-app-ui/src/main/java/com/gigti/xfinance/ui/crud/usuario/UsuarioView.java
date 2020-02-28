/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

//@Component
@Route(value = Constantes.VIEW_R_USUARIOS, layout = MainLayout.class)
@RouteAlias(value = "usuario", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_USUARIO +" | "+ Constantes.VIEW_MAIN)
public class UsuarioView extends VerticalLayout {

    private UsuarioGrid grid;
    private UsuarioForm form;
    private TextField filter;
    private UsuarioService usuarioService;
    private Empresa empresa;

    public UsuarioView(UsuarioService iService) {
        this.usuarioService = iService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        addClassName("UsuarioCrudView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout topLayout = createTopBar();
        configureGrid();

        form = new UsuarioForm(usuarioService.findAllRol(empresa, false));
        form.addListener(UsuarioForm.SaveEvent.class, this::saveUser);
        form.addListener(UsuarioForm.DeleteEvent.class, this::deleteContact);
        form.addListener(UsuarioForm.CloseEvent.class, e -> closeEditor());

        H3 title = new H3(Constantes.VIEW_USUARIO);
        title.setClassName("titleView");

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);

        updateList();
        closeEditor();
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new UsuarioDTO());
    }

    private void configureGrid() {
        grid = new UsuarioGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> editUser(evt.getValue()));
    }

    private void editUser(UsuarioDTO usuario) {
        if (usuario == null) {
            closeEditor();
        } else {
            form.setUser(usuario);
            form.setVisible(true);
            showForm(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUser(null);
        //form.setVisible(false);
        grid.deselectAll();
        showForm(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(usuarioService.findAll(filter.getValue(), empresa, grid.getPage(), grid.getPageSize()));
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filtro Nombre");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> updateList());
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        filter.focus();

        Button btnAdduser = new Button("Nuevo");
        btnAdduser.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdduser.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdduser.addClickListener(click -> addUser());
        btnAdduser.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        return new TopBarComponent(filter, btnAdduser);
    }

    private void saveUser(UsuarioForm.SaveEvent evt) {
        UsuarioDTO usuario = evt.getUsuario();
        usuario.setEmpresa(empresa);
        usuarioService.saveUsuario(usuario);
        updateList();
        closeEditor();
    }

    private void deleteContact(UsuarioForm.DeleteEvent evt) {
        UsuarioDTO usuario = evt.getUsuario();
        Response response = usuarioService.deleteUsuario(usuario.getUsuarioid());
        if(response.isSuccess()){
            Notification.show(response.getMessage(), 3000, Notification.Position.MIDDLE);
            updateList();
            closeEditor();
        } else {
            Notification.show(response.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    public void showForm(boolean show) {
        if(show){
            form.open();
        }else{
            form.close();
            filter.focus();
        }
    }
}
