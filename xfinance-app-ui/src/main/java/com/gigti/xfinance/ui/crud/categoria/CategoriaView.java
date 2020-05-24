package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.MainLayout2;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.jsoup.internal.StringUtil;

import java.util.List;

@Route(value = Constantes.VIEW_R_CATEGORIA, layout = MainLayout2.class)
@RouteAlias(value = "categoria", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_CATEGORIA +" | "+ Constantes.VIEW_MAIN)
public class CategoriaView extends VerticalLayout implements ICrudView {

    private CategoriaGrid grid;
    private CategoriaForm form;
    private TextField filter;
    private CategoriaProductoService categoriaProductoService;
    private Empresa empresa;
    private SearchFilterComponent searchLayout;


    public CategoriaView(CategoriaProductoService iService) {
        this.categoriaProductoService = iService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        addClassName("view");
        setSizeFull();
        setSpacing(false);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 title = new H1(Constantes.VIEW_CATEGORIA.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        filter = searchLayout.getFilter();

        configureGrid();
        form = new CategoriaForm();

        configureForm();

        VerticalLayout gridLayout = new VerticalLayout(grid);
        gridLayout.addClassName("grid");

        FlexLayout flexLayout = new FlexLayout(gridLayout, form);
        flexLayout.addClassName("content");
        flexLayout.setSizeFull();
        flexLayout.setFlexGrow(2, gridLayout);
        flexLayout.setFlexGrow(1, form);

        add(title, searchLayout, flexLayout);

        updateList();
        closeEditor();
    }

    @Override
    public void closeEditor() {
        form.setCategoria(null, "");
        form.setVisible(false);
        grid.deselectAll();
        removeClassName("editing");
    }

    @Override
    public void updateList() {
        grid.setDataProvider(DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    List<CategoriaProducto> categorias = categoriaProductoService.
                            findAll(filter.getValue(), empresa, grid.getPage(), grid.getPageSize());

                    return categorias.stream();
                },
                // Second callback fetches the number of items
                // for a query
                query -> categoriaProductoService.count(filter.getValue(), empresa)
        ));
    }

    @Override
    public void configureProvider() {
    }

    public void configureGrid() {
        grid = new CategoriaGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        grid.addPageChangeListener(evt -> grid.setPage(evt.getNewPage()));
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("Nuevo", "", "Filtro por Nombre", false, true);
        searchLayout.getFilter().addValueChangeListener(event -> updateList());
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> addItem());
    }

    @Override
    public void configureForm() {
        form.addListener(CategoriaForm.SaveEvent.class, this::save);
        form.addListener(CategoriaForm.DeleteEvent.class, this::delete);
        form.addListener(CategoriaForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void edit(Object categoria) {
        if (categoria == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((CategoriaProducto) categoria).getId())){
                form.setCategoria((CategoriaProducto) categoria, Constantes.CREATE_CATEGORY);
            } else {
                form.setCategoria((CategoriaProducto) categoria, Constantes.EDIT_CATEGORY);
            }
            form.setVisible(true);
            addClassName("editing");
        }
    }

    @Override
    public void save(ComponentEvent event) {
        CategoriaProducto categoria = ((CategoriaForm.SaveEvent) event).getCategoria();
        categoria.setEmpresa(empresa);
        categoriaProductoService.saveCategoria(categoria);
        updateList();
        closeEditor();
    }

    @Override
    public void delete(ComponentEvent event) {
        CategoriaProducto categoria = ((CategoriaForm.DeleteEvent) event).getCategoria();
        Response response = categoriaProductoService.delete(categoria.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    @Override
    public void addItem() {
        grid.asSingleSelect().clear();
        CategoriaProducto c = new CategoriaProducto();
        c.setActivo(true);
        edit(c);
    }
}
