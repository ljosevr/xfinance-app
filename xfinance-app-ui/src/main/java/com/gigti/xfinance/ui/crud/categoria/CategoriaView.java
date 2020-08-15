package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.Objects;

@Route(value = Constantes.VIEW_R_CATEGORIA, layout = MainLayout.class)
@RouteAlias(value = "categoria", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_CATEGORIA +" | "+ Constantes.VIEW_MAIN)
public class CategoriaView extends VerticalLayout implements ICrudView<CategoriaProducto> {

    private final CategoriaGrid grid;
    private CategoriaForm form;
    private TextField filter;
    private final CategoriaProductoService categoriaProductoService;
    private final Empresa empresa;
    private SearchFilterComponent searchLayout;
    private DataProvider<CategoriaProducto, Void> dataProvider;

    public CategoriaView(CategoriaProductoService iService) {
        this.categoriaProductoService = iService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_CATEGORIA.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureProvider();

        configureGrid(grid = new CategoriaGrid());

        configureForm();

        this.add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    @Override
    public void closeEditor() {
        form.setCategoria(null, "", "");
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    @Override
    public void configureForm() {
        form = new CategoriaForm();
        form.addListener(CategoriaForm.SaveEvent.class, this::save);
        form.addListener(CategoriaForm.DeleteEvent.class, this::delete);
        form.addListener(CategoriaForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> categoriaProductoService.findAll(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                query -> categoriaProductoService.count(filter.getValue(), empresa));
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
            CategoriaProducto c = new CategoriaProducto();
            c.setActivo(true);
            addItem(grid, c);
        });
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));
        filter = searchLayout.getFilter();
    }

    @Override
    public void editItem(Object categoria) {
        if (categoria == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((CategoriaProducto) categoria).getId())){
                form.setCategoria((CategoriaProducto) categoria, Constantes.CREATE_CATEGORY, ICrudView.OPTION_ADD);
            } else {
                form.setCategoria((CategoriaProducto) categoria, Constantes.EDIT_CATEGORY, ICrudView.OPTION_EDIT);
            }
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void deleteItem(Object obj) {
        if (obj == null) {
            closeEditor();
        } else {
            form.setCategoria((CategoriaProducto) obj, Constantes.DELETE_CATEGORIA, ICrudView.OPTION_DELETE);
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void save(ComponentEvent event) {
        CategoriaProducto categoria = ((CategoriaForm.SaveEvent) event).getCategoria();
        categoria.setEmpresa(empresa);
        Response response = categoriaProductoService.saveCategoria(categoria);
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
        CategoriaProducto categoria = ((CategoriaForm.DeleteEvent) event).getCategoria();
        Response response = categoriaProductoService.delete(categoria.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}
