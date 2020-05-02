/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.inicial;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.InventarioService;
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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Route(value = Constantes.VIEW_R_INVENTARIO_INICIAL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_INVENTARIO_INICIAL +" | "+ Constantes.VIEW_MAIN)
public class InvInicialView extends VerticalLayout  implements ICrudView {

    private Empresa empresa;
    private InvInicialGrid grid;
    private TextField filter;
    private InventarioService inventarioService;
    private InvInicialForm form;
    private SearchFilterComponent searchLayout;
    private DataProvider<InventarioInicial, Void> dataProvider;

    public InvInicialView(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        configureProvider();

        addClassName("InventarioInicialView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H3 title = new H3(Constantes.VIEW_INVENTARIO_INICIAL);
        title.addClassName("titleView2");

        configureSearchLayout();
        filter = searchLayout.getFilter();

        configureGrid();

        form = new InvInicialForm();
        configureForm();

        VerticalLayout gridLayout = new VerticalLayout(grid);
        gridLayout.addClassName("grid");

        FlexLayout flexLayout = new FlexLayout(gridLayout, form);
        flexLayout.addClassName("content");
        flexLayout.setSizeFull();
        flexLayout.setFlexGrow(3, gridLayout);
        flexLayout.setFlexGrow(1, form);

        add(title, searchLayout, flexLayout);

        updateList();
        closeEditor();
    }

    public void closeEditor() {
        form.setInventario(null);
        form.setVisible(false);
        grid.deselectAll();
        removeClassName("editing");
    }

    public void configureGrid() {
        grid = new InvInicialGrid();
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
                    List<InventarioInicial> inventarioInicials = inventarioService.
                            findAllInvInicial(filter.getValue(), empresa, grid.getPage(), grid.getPageSize());

                    return inventarioInicials.stream();
                },
                // Second callback fetches the number of items
                // for a query
                query -> inventarioService.getCount(filter.getValue(), empresa)
        );
    }

    public void edit(Object inventario) {
        if (inventario == null) {
            closeEditor();
        } else {
            if(StringUtils.isBlank(((InventarioInicial)inventario).getId())) {
                form.setInventario(((InventarioInicial)inventario));
                form.setVisible(true);
                addClassName("editing");
            } else {
                NotificacionesUtil.showError("Este Inventario No se puede modificar, ya fue actualizado");
            }
        }
    }

    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("Nueva", "", "Filtro por Nombre", false, false);
        searchLayout.getFilter().addValueChangeListener(event -> updateList());
        searchLayout.getFilter().focus();
    }

    public void save(ComponentEvent evt) {
        InventarioInicial inventario = ((InvInicialForm.SaveEvent) evt).getInventario();

        Response response = inventarioService.saveInventarioInicial(inventario, CurrentUser.get());

        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }


    @Override
    public void configureForm() {
        form.addListener(InvInicialForm.SaveEvent.class, this::save);
        form.addListener(InvInicialForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void delete(ComponentEvent event) {
    }

    @Override
    public void addItem() {
    }

}
