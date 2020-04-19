/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.backend.services.ImpuestoService;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.List;

@Route(value = Constantes.VIEW_R_PRODUCTO, layout = MainLayout.class)
@RouteAlias(value = "producto", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_PRODUCTO +" | "+ Constantes.VIEW_MAIN)
public class ProductoView extends VerticalLayout implements ICrudView {

    private ProductoGrid grid;
    private ProductoForm form;
    private TextField filter;
    private ProductoService productoService;
    private Empresa empresa;
    private SearchFilterComponent searchLayout;
    private DataProvider<Producto, Void> dataProvider;

    public ProductoView(CategoriaProductoService categoriaProductoService, ProductoService iServiceProd, ImpuestoService impuestoService) {
        this.productoService = iServiceProd;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        addClassName("ProductoView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H3 title = new H3(Constantes.VIEW_PRODUCTO);
        title.addClassName("titleView2");

        configureProvider();

        configureSearchLayout();
        filter = searchLayout.getFilter();

        configureGrid();
        form = new ProductoForm(
                categoriaProductoService.findAll(empresa),
                productoService.getAllTipoMedidaEnum(),
                impuestoService.findAll(empresa)
        );

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

    public void configureForm() {
        form.addListener(ProductoForm.SaveEvent.class, this::save);
        form.addListener(ProductoForm.DeleteEvent.class, this::delete);
        form.addListener(ProductoForm.CloseEvent.class, e -> closeEditor());
    }

    public void closeEditor() {
        form.setProducto(null);
        form.setVisible(false);
        grid.deselectAll();
        removeClassName("editing");
    }

    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    List<Producto> productos = productoService.
                            findAll(filter.getValue(), empresa, grid.getPage(), grid.getPageSize());

                    return productos.stream();
                },
                // Second callback fetches the number of items
                // for a query
                query -> productoService.count(filter.getValue(), empresa)
        );
    }

    public void configureGrid() {
        grid = new ProductoGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));

        grid.addPageChangeListener(evt -> {
            grid.setPage(evt.getNewPage());
        });
    }

    public void updateList() {
        grid.setDataProvider(dataProvider);
    }

    public void configureSearchLayout() {

        searchLayout = new SearchFilterComponent("Nuevo", "", "Filtro por Nombre", false, true);
        searchLayout.getFilter().addValueChangeListener(event -> updateList());
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> addItem());
    }

    public void addItem() {
        grid.asSingleSelect().clear();
        edit(new Producto());
    }

    public void edit(Object producto) {
        if (producto == null) {
            closeEditor();
        } else {
            form.setProducto((Producto) producto);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void save(ComponentEvent evt) {
        Producto producto = ((ProductoForm.SaveEvent) evt).getProducto();
        producto.setEmpresa(empresa);
        productoService.saveProduct(producto, CurrentUser.get());
        updateList();
        closeEditor();
    }

    public void delete(ComponentEvent evt) {
        Producto producto = ((ProductoForm.SaveEvent) evt).getProducto();
        Response response = productoService.delete(producto.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}