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
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = Constantes.VIEW_R_USUARIOS, layout = MainLayout.class)
@RouteAlias(value = "usuario", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_USUARIO +" | "+ Constantes.VIEW_MAIN)
public class UsuarioView extends VerticalLayout {

    private UsuarioGrid grid;
    private UsuarioForm form;
    private TextField filter;
    private UsuarioService usuarioService;
    private Empresa empresa;
    private SearchFilterComponent component;

    public UsuarioView(UsuarioService iService) {
        this.usuarioService = iService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        addClassName("UsuarioCrudView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureTopBar();
        filter = component.getFilter();

        configureGrid();

        form = new UsuarioForm(usuarioService.findAllRol(empresa, false));
        form.addListener(UsuarioForm.SaveEvent.class, this::save);
        form.addListener(UsuarioForm.DeleteEvent.class, this::delete);
        form.addListener(UsuarioForm.CloseEvent.class, e -> closeEditor());

        H3 title = new H3(Constantes.VIEW_USUARIO);
        title.addClassName("titleView2");
        title.setClassName("Date");

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(component);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, component);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);

        updateList();
        closeEditor();
    }

    public void addUser() {
        grid.asSingleSelect().clear();
        editUser(new UsuarioDTO());
    }

    public void configureGrid() {
        grid = new UsuarioGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> editUser(evt.getValue()));
    }

    public void editUser(UsuarioDTO usuario) {
        if (usuario == null) {
            closeEditor();
        } else {
            form.setUser(usuario);
            form.setVisible(true);
            showForm(true);
            addClassName("editing");
        }
    }

    public void closeEditor() {
        form.setUser(null);
        //form.setVisible(false);
        grid.deselectAll();
        showForm(false);
        removeClassName("editing");
    }

    public void updateList() {
        grid.setItems(usuarioService.findAll(filter.getValue(), empresa, grid.getPage(), grid.getPageSize()));
    }

    public void configureTopBar() {

        component = new SearchFilterComponent("Nuevo", "", "Filtro por Nombre Usuario", false, true);
        component.getFilter().addValueChangeListener(event -> updateList());
        component.getFilter().focus();
        component.getBtnAdd().addClickListener(click -> addUser());
    }

    public void save(UsuarioForm.SaveEvent evt) {
        UsuarioDTO usuario = evt.getUsuario();
        usuario.setEmpresa(empresa);
        usuarioService.saveUsuario(usuario);
        updateList();
        closeEditor();
    }

    private void delete(UsuarioForm.DeleteEvent evt) {
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

    private void showForm(boolean show) {
        if(show){
            form.open();
        }else{
            form.close();
            filter.focus();
        }
    }
}
