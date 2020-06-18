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
import com.gigti.xfinance.ui.MainLayout2;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = Constantes.VIEW_R_USUARIOS, layout = MainLayout2.class)
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

        addClassName("view");
        setSizeFull();
        setSpacing(false);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureTopBar();
        filter = component.getFilter();

        configureGrid();

        form = new UsuarioForm(usuarioService.findAllRol(empresa, false));
        form.addListener(UsuarioForm.SaveEvent.class, this::save);
        form.addListener(UsuarioForm.DeleteEvent.class, this::delete);
        form.addListener(UsuarioForm.CloseEvent.class, e -> closeEditor());

        H1 title = new H1(Constantes.VIEW_USUARIO.toUpperCase());
        title.addClassName("titleView2");

        VerticalLayout barAndGridLayout = new VerticalLayout();

        VerticalLayout gridLayout = new VerticalLayout(grid);
        gridLayout.addClassName("grid");

        barAndGridLayout.add(component);
        barAndGridLayout.add(gridLayout);
        barAndGridLayout.setFlexGrow(1, gridLayout);
        barAndGridLayout.setFlexGrow(0, component);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(gridLayout);

        add(title, barAndGridLayout);

        updateList();
        closeEditor();
    }

    public void addUser() {
        grid.asSingleSelect().clear();
        UsuarioDTO user = new UsuarioDTO();
        user.setActivo(true);
        editUser(user);
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
        Response response = usuarioService.saveUsuario(usuario);
        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
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
