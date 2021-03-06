/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.inicial;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ImpuestoService;
import com.gigti.xfinance.backend.services.InventarioInicialService;
import com.gigti.xfinance.backend.services.ProductoService;
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

import org.apache.commons.lang3.StringUtils;
import org.vaadin.data.spring.OffsetBasedPageRequest;

@Route(value = Constantes.VIEW_R_INVENTARIO_INICIAL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_INVENTARIO_INICIAL +" | "+ Constantes.VIEW_MAIN)
public class InvInicialView extends VerticalLayout  implements ICrudView<InventarioInicial> {

    private final Empresa empresa;
    private InvInicialGrid grid;
    private TextField filter;
    private final InventarioInicialService inventarioService;
    private final ProductoService productoService;
    private final InvInicialForm form;
    private SearchFilterComponent searchLayout;
    private DataProvider<InventarioInicial, Void> dataProvider;

    public InvInicialView(InventarioInicialService inventarioService, ImpuestoService impuestoService, ProductoService productoService){
        this.inventarioService = inventarioService;
        this.productoService = productoService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_INVENTARIO_INICIAL.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureGrid(grid = new InvInicialGrid());
        configureProvider();

        form = new InvInicialForm(impuestoService.findAll(empresa));
        configureForm();

        add(title, searchLayout, grid);

    }

    public void closeEditor() {
        //form.setInventario(null, Constantes.CREATE_INV_INICIAL, ICrudView.OPTION_ADD);
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
            query -> inventarioService.findAllInvInicial(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
            query -> productoService.count(filter.getValue(), empresa)
        );
        grid.setDataProvider(dataProvider);
    }

    public void editItem(Object inventario) {
        if (inventario == null) {
            closeEditor();
        } else {
            if(StringUtils.isBlank(((InventarioInicial)inventario).getId())) {
                form.setInventario((InventarioInicial)inventario, Constantes.CREATE_INV_INICIAL, ICrudView.OPTION_ADD);
                showForm(true, form, this, filter);
            } else {
                InventarioInicial invInicial = (InventarioInicial)inventario;
                if(invInicial.isDefinitivo()) {
                    NotificacionesUtil.showError("Este Inventario No se puede modificar, ya fue marcado como Definitivo");
                } else {
                    form.setInventario(invInicial, Constantes.EDIT_INV_INICIAL, ICrudView.OPTION_EDIT);
                    showForm(true, form, this, filter);
                }
            }
        }
    }

    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("", false,
                "", "Filtro Nombre Producto",
                "", true,
                "", true,
                "", false,
                true);
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> dataProvider.refreshAll());
        searchLayout.getFilter().focus();
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnSearch().addClickListener(click -> dataProvider.refreshAll());
        searchLayout.getBtnToPdf().addClickListener(click -> {
            Response response = inventarioService.generateReportInvInicial(filter.getValue(),empresa,"pdf");
            if(response.isSuccess()) {
                StreamResource resource = new StreamResource("reporteInvInicial.pdf", () -> new ByteArrayInputStream((byte[]) response.getObject()));
                resource.setContentType("application/pdf");
                StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
                UI.getCurrent().getPage().executeJs("window.open($0,\"_blank\",\"toolbar=yes,scrollbars=yes,resizable=yes\")",registration.getResourceUri().toString());

            } else {
                NotificacionesUtil.showError(response.getMessage());
            }
        });
        filter = searchLayout.getFilter();
    }

    private void exportReport(SearchFilterComponent searchLayout) {

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
    public void deleteItem(Object obj) {

    }

    public void save(ComponentEvent evt) {
        InventarioInicial inventario = ((InvInicialForm.SaveEvent) evt).getInventario();

        Response response = inventarioService.processInventarioInicial(inventario, CurrentUser.get());

        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            grid.getDataProvider().refreshAll();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}
