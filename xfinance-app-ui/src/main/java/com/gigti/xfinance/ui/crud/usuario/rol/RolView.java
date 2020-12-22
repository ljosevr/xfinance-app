package com.gigti.xfinance.ui.crud.usuario.rol;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.backend.services.RolService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.categoria.CategoriaForm;
import com.gigti.xfinance.ui.crud.categoria.CategoriaGrid;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Route(value = Constantes.VIEW_R_ROL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_ROL +" | "+ Constantes.VIEW_MAIN)
public class RolView extends VerticalLayout implements ICrudView<Rol> {

    private final RolGrid grid;
    private RolForm form;
    private TextField filter;
    private final RolService rolService;
    private final Empresa empresa;
    private SearchFilterComponent searchLayout;
    private DataProvider<Rol, Void> dataProvider;
    private List<Vista> allViews;

    public RolView(RolService rolService) {
        this.rolService = rolService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;
        allViews = rolService.findAllVistas();

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_ROL.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureProvider();

        configureGrid(grid = new RolGrid());

        configureForm();

        this.add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    @Override
    public void closeEditor() {
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> rolService.findAll(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                query -> rolService.count(filter.getValue(), empresa));
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("", true,
                "", "Filtro Nombre",
                "", true,
                "", true,
                "", true);
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList(grid, dataProvider));
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> {
            Rol r = new Rol();
            r.setActivo(true);
            addItem(grid, r);
        });
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));
        filter = searchLayout.getFilter();
    }

    @Override
    public void configureForm() {
        form = new RolForm();
        form.addListener(RolForm.SaveEvent.class, this::save);
        form.addListener(RolForm.DeleteEvent.class, this::delete);
        form.addListener(RolForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void save(ComponentEvent event) {
        Rol rol = ((RolForm.SaveEvent) event).getRol();
        rol.setEmpresa(empresa);
        Response response = rolService.save(rol);
        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    @Override
    public void delete(ComponentEvent event) {
        Rol categoria = ((RolForm.DeleteEvent) event).getRol();
        Response response = rolService.delete(categoria.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    @Override
    public void editItem(Object rol) {
        if (rol == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Rol) rol).getId())){
                form.setRol((Rol) rol, Constantes.CREATE_ROL, ICrudView.OPTION_ADD, allViews);
            } else {
                form.setRol((Rol) rol, Constantes.EDIT_ROL, ICrudView.OPTION_EDIT, allViews);
            }
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void deleteItem(Object obj) {
        if (obj == null) {
            closeEditor();
        } else {
            form.setRol((Rol) obj, Constantes.DELETE_ROL, ICrudView.OPTION_DELETE, new ArrayList<>());
            showForm(true, form, this, filter);
        }
    }
}
