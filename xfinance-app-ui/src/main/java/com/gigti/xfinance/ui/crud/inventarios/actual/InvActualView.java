/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.actual;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.InventarioActualCosto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.InventarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.io.ByteArrayInputStream;
import java.util.Objects;

@Route(value = Constantes.VIEW_R_INVENTARIO_ACTUAL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_INVENTARIO_ACTUAL +" | "+ Constantes.VIEW_MAIN)
public class InvActualView extends VerticalLayout  implements ICrudView<InventarioActual> {

    private final Empresa empresa;
    private final InvActualGrid grid;
    private TextField filter;
    private final InventarioService inventarioService;
    private SearchFilterComponent searchLayout;
    private DataProvider<InventarioActual, Void> dataProvider;

    public InvActualView(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);


        H1 title = new H1(Constantes.VIEW_INVENTARIO_ACTUAL.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureProvider();

        configureGrid(grid = new InvActualGrid());

        add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    public void closeEditor() {
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> inventarioService.findInvActual(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                query -> inventarioService.countInvActual(filter.getValue(), empresa));
    }

    @Override
    public void editItem(Object inventario) {
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("", false,
                "", "Filtro Nombre Producto",
                "", true,
                "", false,
                "", false,
                true);
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> dataProvider.refreshAll());
        searchLayout.getBtnSearch().addClickListener(click -> dataProvider.refreshAll());
        searchLayout.getBtnToPdf().addClickListener(click -> {
            Response response = inventarioService.generateReportInvActual(filter.getValue(),empresa,"pdf");
            if(response.isSuccess()) {
                StreamResource resource = new StreamResource("reporteInvActual.pdf", () -> new ByteArrayInputStream((byte[]) response.getObject()));
                resource.setContentType("application/pdf");
                StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
                UI.getCurrent().getPage().executeJs("window.open($0,\"_blank\",\"toolbar=yes,scrollbars=yes,resizable=yes\")",registration.getResourceUri().toString());

            } else {
                NotificacionesUtil.showError(response.getMessage());
            }
        });
        searchLayout.getFilter().focus();
        filter = searchLayout.getFilter();
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
    public void deleteItem(Object obj) {

    }

}
