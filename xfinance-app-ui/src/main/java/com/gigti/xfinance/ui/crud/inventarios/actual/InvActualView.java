/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.actual;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActualCosto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.InventarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = Constantes.VIEW_R_INVENTARIO_ACTUAL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_INVENTARIO_ACTUAL +" | "+ Constantes.VIEW_MAIN)
public class InvActualView extends VerticalLayout  implements ICrudView {

    private Empresa empresa;
    private InvActualGrid grid;
    private TextField filter;
    private InventarioService inventarioService;
    private SearchFilterComponent searchLayout;
    private DataProvider<InventarioActualCosto, Void> dataProvider;

    public InvActualView(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        configureProvider();

        addClassName("InvActualView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 title = new H1(Constantes.VIEW_INVENTARIO_ACTUAL.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();
        filter = searchLayout.getFilter();

        configureGrid();

        VerticalLayout gridLayout = new VerticalLayout(grid);
        gridLayout.addClassName("grid");

        FlexLayout flexLayout = new FlexLayout(gridLayout);
        flexLayout.addClassName("content");
        flexLayout.setSizeFull();
        //flexLayout.setFlexGrow(3, gridLayout);

        add(title, searchLayout, flexLayout);

        updateList();
        closeEditor();
    }

    public void closeEditor() {
    }

    public void configureGrid() {
        grid = new InvActualGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        grid.addPageChangeListener(evt -> grid.setPage(evt.getNewPage()));
    }

    public void updateList() {
        grid.setDataProvider(dataProvider);
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    List<InventarioActualCosto> listResult = inventarioService.
                            findInvActual(filter.getValue(), empresa, grid.getPage(), grid.getPageSize());

                    return listResult.stream();
                },
                // Second callback fetches the number of items
                // for a query
                query -> inventarioService.countInvActual(filter.getValue(), empresa)
        );
    }

    @Override
    public void edit(Object inventario) {
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("", "", "Filtro por Nombre Producto", false, false);
        searchLayout.getFilter().addValueChangeListener(event -> updateList());
        searchLayout.getFilter().focus();
    }

    @Override
    public void save(ComponentEvent evt) {
    }


    @Override
    public void configureForm() {
    }

    @Override
    public void delete(ComponentEvent event) {
    }

    @Override
    public void addItem() {
    }

}
