/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaDataProvider;
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
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link ProductoCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "productos", layout = MainLayout.class)
@RouteAlias(value = "producto", layout = MainLayout.class)
@UIScope
public class ProductoCrudView extends HorizontalLayout
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Productos";
    private ProductoGrid grid;
    private ProductoForm form;
    private TextField filter;

    private ProductoCrudLogic viewLogic;
    private Button btnNewProduct;

    private ProductoDataProvider dataProvider;
    private CategoriaDataProvider categoriaDataProvider;

    @Autowired
    public ProductoCrudView(IcategoriaProductoService iServiceCat, IProductoService iServiceProd) {
        setSizeFull();
        viewLogic = new ProductoCrudLogic(this);
        HorizontalLayout topLayout = createTopBar();

        dataProvider = ProductoDataProvider.getInstance(iServiceProd);
        categoriaDataProvider = CategoriaDataProvider.getInstance(iServiceCat);

        grid = new ProductoGrid();
        grid.setDataProvider(dataProvider);
        grid.setItems(dataProvider.findAll());
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        form = new ProductoForm(viewLogic);
        form.setCategories(categoriaDataProvider.findAll());

        H3 title = new H3(this.VIEW_NAME);
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
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        //TODO Realizar Filtro por Categoria Tambien
        filter.setPlaceholder("Filtro Nombre");
        filter.addValueChangeListener(event -> {
            grid.setItems(dataProvider.setFilter(event.getValue()));
        });
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        btnNewProduct = new Button("Nuevo");
        btnNewProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewProduct.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewProduct.addClickListener(click -> viewLogic.newProducto());
        btnNewProduct.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(btnNewProduct);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewProductEnabled(boolean enabled) {
        btnNewProduct.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Producto row) {
        grid.getSelectionModel().select(row);
    }

    public Producto getSelectedRow() {
        return grid.getSelectedRow();
    }

    public boolean saveProducto(Producto producto) {
        return dataProvider.save(producto);
    }

    public boolean deleteProducto(Producto producto) {
        return dataProvider.delete(producto);
    }

    public void editProducto(Producto producto) {
        showForm(producto != null);
        form.editProducto(producto);
    }

    public Producto findById(String productoId) {
        return dataProvider.findById(productoId);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

    public void refresh(){
        grid.setItems(dataProvider.findAll());
    }

}
