/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.TipoService;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.Objects;

@Route(value = Constantes.VIEW_R_USUARIOS, layout = MainLayout.class)
@RouteAlias(value = "usuario", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_USUARIO +" | "+ Constantes.VIEW_MAIN)
public class UsuarioView extends VerticalLayout implements ICrudView<Usuario> {

    private UsuarioGrid grid;
    private UsuarioForm form;
    private TextField filter;
    private UsuarioService usuarioService;
    private Empresa empresa;
    private SearchFilterComponent component;
    private DataProvider<UsuarioDTO, Void> dataProvider;
    private TipoService tipoService;

    public UsuarioView(UsuarioService iService, TipoService tipoService) {
        this.usuarioService = iService;
        this.tipoService = tipoService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        configureSearchLayout();
        filter = component.getFilter();

        configureGrid();

        configureForm();

        H1 title = new H1(Constantes.VIEW_USUARIO.toUpperCase());
        title.addClassName("titleView2");

        this.add(title, component, grid);

        updateList();
        closeEditor();
    }

    public void configureGrid() {
        grid = new UsuarioGrid();
        configureProvider();
        grid.setSizeFull();
        grid.addItemDoubleClickListener(evt -> editItem(evt.getItem()));
    }

    public void closeEditor() {
        form.setUser(null, "", "");
        grid.deselectAll();
        showForm(false);
    }

    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> usuarioService.findAll(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                query -> usuarioService.count(filter.getValue(), empresa));
    }

    public void updateList() {
        grid.setDataProvider(dataProvider);
        //grid.setItems(usuarioService.findAll(filter.getValue(), empresa, grid.getPage(), grid.getPageSize()));
    }

    public void configureSearchLayout() {

        component = new SearchFilterComponent("", true,
                "", "Filtro por Nombre",
                "", true,
                "", true,
                "", true);
        component.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList());
        component.getFilter().focus();
        component.getBtnAdd().addClickListener(click -> addItem());
        component.getBtnSearch().addClickListener(click -> updateList());
        component.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        component.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));
    }

    @Override
    public void configureForm() {
        form = new UsuarioForm(usuarioService.findAllRol(empresa, false), tipoService.getTiposIdentificacion());
        form.addListener(UsuarioForm.SaveEvent.class, this::save);
        form.addListener(UsuarioForm.DeleteEvent.class, this::delete);
        form.addListener(UsuarioForm.CloseEvent.class, e -> closeEditor());
    }

    public void save(ComponentEvent evt) {
        UsuarioDTO usuario = ((UsuarioForm.SaveEvent) evt).getUsuario();
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

    public void delete(ComponentEvent evt) {
        UsuarioDTO usuario = ((UsuarioForm.DeleteEvent) evt).getUsuario();
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
            form.setVisible(true);
            this.addClassName("editing");
            form.open();
        }else{
            this.removeClassName("editing");
            form.close();
            filter.focus();
        }
    }

    public void addItem() {
        grid.asSingleSelect().clear();
        UsuarioDTO user = new UsuarioDTO();
        user.setActivo(true);
        editItem(user);
    }

    public void editItem(Object object) {
        if (object == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((UsuarioDTO) object).getId())){
                form.setUser((UsuarioDTO) object, Constantes.CREATE_USER, ICrudView.OPTION_ADD);
            } else {
                form.setUser((UsuarioDTO) object, Constantes.EDIT_USER, ICrudView.OPTION_EDIT);
            }

            showForm(true);
        }
    }

    public void deleteItem(Object object) {
        if (object == null) {
            closeEditor();
        } else {
            form.setUser((UsuarioDTO) object, Constantes.DELETE_USER, ICrudView.OPTION_DELETE);

            showForm(true);
        }
    }
}
