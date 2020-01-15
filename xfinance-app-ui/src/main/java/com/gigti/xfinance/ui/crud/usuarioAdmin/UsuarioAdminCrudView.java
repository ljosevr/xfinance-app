/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuarioAdmin;

import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

/**
 * A view for performing create-read-update-delete operations on Usuario Admin.
 * <p>
 * See also {@link UsuarioAdminCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = Constantes.VIEW_R_USUARIOADMIN, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_MAIN)
public class UsuarioAdminCrudView extends HorizontalLayout
         {


    /*private UsuarioAdminGrid grid;
    private UsuarioAdminForm form;
    private TextField filter;

    private UsuarioAdminCrudLogic viewLogic;
    private List<Usuario> listUsuarioAdmin;
    private VerticalLayout barAndGridLayout;

    @Autowired
    public UsuarioAdminCrudView(IusuarioService iService) {

        viewLogic = new UsuarioAdminCrudLogic(iServiceProd, iServiceCat, this);
//        if(viewLogic.access()) {
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new UsuarioAdminGrid();
        listaProducto = viewLogic.findAll();
        grid.setItems(listaProducto);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        List<CategoriaProducto> listaCategoria = viewLogic.findAllCategoria();
        form = new UsuarioAdminForm(viewLogic, listaCategoria);
        form.setCategories(listaCategoria);

        H3 title = new H3(Constantes.VIEW_PRODUCTO);
        title.setClassName("titleView");

        barAndGridLayout = new VerticalLayout();
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
//        }else{
//            UI.getCurrent().navigate(MainLayout.class);
//        }
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        //TODO Realizar Filtro por Categoria Tambien
        filter.setPlaceholder("Filtro Nombre");
        filter.addValueChangeListener(event -> {
            listaProducto = viewLogic.setFilter(event.getValue());
            if (listaProducto != null)
                grid.setItems(listaProducto);
        });
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        filter.focus();

        Button btnNewProduct = new Button("Nuevo");
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

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Producto row) {
        grid.getSelectionModel().select(row);
    }

    public void editProducto(Producto producto) {
        form.editProducto(producto);
        showForm(producto != null);
    }

    public void showForm(boolean show) {
        if(show){
            barAndGridLayout.setVisible(false);
        } else{
            barAndGridLayout.setVisible(true);
            filter.focus();
        }
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

    public void refresh() {
        listaProducto = viewLogic.findAll();
        grid.setItems(listaProducto);
    }

    public void refresh(Producto producto) {
        for(Iterator<Producto> it = listaProducto.iterator(); it.hasNext();){
            Producto p = it.next();
            if(p.getId().equals(producto.getId())) {
                it.remove();
                listaProducto.remove(p);
                break;
            }
        }
        listaProducto.add(producto);
        grid.setItems(listaProducto);
        grid.refresh(producto);
    }

    public UsuarioAdminGrid getGrid() {
        return grid;
    }

    public TextField getFilter() {
        return filter;
    }*/
}
