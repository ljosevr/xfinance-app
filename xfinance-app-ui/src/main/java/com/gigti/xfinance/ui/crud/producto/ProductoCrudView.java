/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.LoginScreen;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaCrudLogic;
import com.gigti.xfinance.ui.util.TopBarComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link ProductoCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = Constantes.VIEW_R_PRODUCTO, layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_PRODUCTO, layout = MainLayout.class)
public class ProductoCrudView extends HorizontalLayout
        implements HasUrlParameter<String> {

    private ProductoGrid grid;
    private ProductoForm form;
    private TextField filter;

    private ProductoCrudLogic viewLogic;
    private Button btnNewProduct;
    private CategoriaCrudLogic categoriaCrudLogic;

    @Autowired
    public ProductoCrudView(IcategoriaProductoService iServiceCat, IProductoService iServiceProd) {

        viewLogic = new ProductoCrudLogic(iServiceProd,this);
        if(viewLogic.access()) {
            setSizeFull();
            categoriaCrudLogic = new CategoriaCrudLogic(iServiceCat);

            HorizontalLayout topLayout = createTopBar();

            grid = new ProductoGrid();
            grid.setItems(viewLogic.findAll());
            grid.asSingleSelect().addValueChangeListener(
                    event -> viewLogic.rowSelected(event.getValue()));

            form = new ProductoForm(viewLogic);
            form.setCategories(categoriaCrudLogic.findAll());

            H3 title = new H3(Constantes.VIEW_PRODUCTO);
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
        }else{
            UI.getCurrent().navigate(MainLayout.class);
        }
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        //TODO Realizar Filtro por Categoria Tambien
        filter.setPlaceholder("Filtro Nombre");
        filter.addValueChangeListener(event -> {
            grid.setItems(viewLogic.setFilter(event.getValue()));
        });
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        btnNewProduct = new Button("Nuevo");
        btnNewProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewProduct.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewProduct.addClickListener(click -> viewLogic.newProducto());
        btnNewProduct.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        return new TopBarComponent(filter, btnNewProduct);
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

//    public void setNewProductEnabled(boolean enabled) {
//        btnNewProduct.setEnabled(enabled);
//    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Producto row) {
        grid.getSelectionModel().select(row);
    }

    public void editProducto(Producto producto) {
        showForm(producto != null);
        form.editProducto(producto);
    }

    public Producto findById(String productoId) {
        return viewLogic.findById(productoId);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if(viewLogic.access()) {
            viewLogic.enter(parameter);
        }
    }

    public void refresh(){
        grid.setItems(viewLogic.findAll());
    }

    public void refresh(Producto producto){
        grid.refresh(producto);
    }
}
