/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.backend.services.ImpuestoService;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.backend.services.TipoService;
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

import java.util.List;
import java.util.Objects;

@Route(value = Constantes.VIEW_R_PRODUCTO, layout = MainLayout.class)
@RouteAlias(value = "producto", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_PRODUCTO +" | "+ Constantes.VIEW_MAIN)
public class ProductoView extends VerticalLayout implements ICrudView<Producto> {

    private final ProductoGrid grid;
    private final ProductoForm form;
    private TextField filter;
    private final ProductoService productoService;
    private final Empresa empresa;
    private SearchFilterComponent searchLayout;
    private final List<CategoriaProducto> listCategoria;
    private DataProvider<Producto, Void> dataProvider;

    public ProductoView(CategoriaProductoService categoriaProductoService, ProductoService iServiceProd, ImpuestoService impuestoService, TipoService tipoService) {
        this.productoService = iServiceProd;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_PRODUCTO_ADMIN.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureProvider();

        configureGrid(grid = new ProductoGrid());
        listCategoria = categoriaProductoService.findAll(empresa);
        form = new ProductoForm(
                listCategoria,
                tipoService.findAllTiposMedidas(empresa),
                impuestoService.findAll(empresa)
        );

        configureForm();

        this.add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    public void closeEditor() {
        //form.setProducto(null, "", "");
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    public void configureForm() {
        form.addListener(ProductoForm.SaveEvent.class, this::save);
        form.addListener(ProductoForm.DeleteEvent.class, this::delete);
        form.addListener(ProductoForm.CloseEvent.class, e -> closeEditor());
    }

    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> productoService.findAll(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                query -> productoService.count(filter.getValue(), empresa));
    }

    public void configureSearchLayout() {

        searchLayout = new SearchFilterComponent("", true,
                "", "Filtro Nombre Producto",
                "", true,
                "", true,
                "", true);
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList(grid, dataProvider));
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> {
            Producto p = new Producto();
            p.setActivo(true);
            p.setCategoria(listCategoria.get(0));
            addItem(grid, p);
        });
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));
        filter = searchLayout.getFilter();
    }

    public void editItem(Object producto) {
        if (producto == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Producto) producto).getId())){
                form.setProducto((Producto) producto, Constantes.CREATE_PRODUCT, ICrudView.OPTION_ADD);
            } else {
                form.setProducto((Producto) producto, Constantes.EDIT_PRODUCT, ICrudView.OPTION_EDIT);
            }
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void deleteItem(Object obj) {
        if (obj == null) {
            closeEditor();
        } else {
            form.setProducto((Producto) obj, Constantes.DELETE_PRODUCTO, ICrudView.OPTION_DELETE);

            showForm(true, form, this, filter);
        }
    }

    public void save(ComponentEvent evt) {
        Producto producto = ((ProductoForm.SaveEvent) evt).getProducto();
        producto.setEmpresa(empresa);
        Response response = productoService.saveProduct(producto, CurrentUser.get());
        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    public void delete(ComponentEvent evt) {
        Producto producto = ((ProductoForm.DeleteEvent) evt).getProducto();
        Response response = productoService.delete(producto.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}