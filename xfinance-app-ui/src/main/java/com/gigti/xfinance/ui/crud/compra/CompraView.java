/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.compra;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.CompraService;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.backend.services.ProveedorService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterAndDatesComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.time.LocalDate;
import java.util.Objects;

@Route(value = Constantes.VIEW_R_COMPRAS, layout = MainLayout.class)
@RouteAlias(value = "compra", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_COMPRAS +" | "+ Constantes.VIEW_MAIN)
@PreserveOnRefresh
public class CompraView extends VerticalLayout implements ICrudView<Compra> {

    //private final VerticalLayout gridLayout;
    private CompraGrid grid;
    private final CompraDetailForm form;
    private TextField filter;
    private final CompraService compraService;
    private final Empresa empresa;
    private SearchFilterAndDatesComponent searchLayout;
    private DatePicker dateEnd;
    private DatePicker dateStart;
    private DataProvider<Compra, Void> dataProvider;


    public CompraView(CompraService compraService, ProductoService productoService, ProveedorService proveedorService) {
        this.compraService = compraService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        addClassName("CompraView");
        setSizeFull();
        setSpacing(true);
        setPadding(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 title = new H1(Constantes.VIEW_COMPRAS.toUpperCase());
        title.setClassName("titleView2");

        configureProvider();

        configureSearchLayout();

        configureGrid(grid = new CompraGrid());
        form = new CompraDetailForm(productoService, proveedorService);
        configureForm();

        add(title, searchLayout, grid, form);

        updateList(grid, dataProvider);
        closeEditor();
    }

    public void configureForm() {
        form.addListener(CompraDetailForm.SaveEvent.class, this::save);
        form.addListener(CompraDetailForm.DeleteEvent.class, this::delete);
        form.addListener(CompraDetailForm.CloseEvent.class, e -> closeEditor());
    }

    public void closeEditor() {
        form.setCompra(null, "");
        form.setVisible(false);
        grid.setVisible(true);
        searchLayout.setVisible(true);
        grid.deselectAll();
        removeClassName("editing");
    }

    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> compraService.findAll(filter.getValue(), empresa,
                        dateStart.getValue(), dateEnd.getValue(),
                        new OffsetBasedPageRequest(query)).stream(),
                query -> compraService.count(filter.getValue(), empresa, dateStart.getValue(), dateEnd.getValue()));
    }

    public void configureSearchLayout() {

        searchLayout = new SearchFilterAndDatesComponent("", true,
                "", "Filtro x Número de Factura",
                "", true,
                "", true,
                "", false, "Fecha Inicio", "Fecha Fin");
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList(grid, dataProvider));
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> addItem());
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));
        filter = searchLayout.getFilter();

        filter = searchLayout.getFilter();
        dateStart = searchLayout.getDateStart();
        dateEnd = searchLayout.getDateEnd();

        dateStart.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate endDate = dateEnd.getValue();
            if (selectedDate != null) {
                dateEnd.setMin(selectedDate);
                if (endDate == null) {
                    dateEnd.setOpened(true);
                }
            } else {
                dateEnd.setMin(null);
            }
        });

        dateEnd.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            //LocalDate startDate = dateStart.getValue();
            dateStart.setMax(selectedDate);
        });
    }

    public void addItem() {
        grid.asSingleSelect().clear();
        editItem(new Compra());
    }

    @Override
    public void deleteItem(Object obj) {

    }

    public void editItem(Object compra) {
        if (compra == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Compra) compra).getId())){
                form.setCompra((Compra) compra, Constantes.CREATE_BUY);
            } else {
                form.setCompra((Compra) compra, Constantes.EDIT_BUY);
            }
            form.setVisible(true);
            grid.setVisible(false);
            searchLayout.setVisible(false);
            addClassName("editing");
        }
    }

    public void save(ComponentEvent evt) {
        Compra compra = ((CompraDetailForm.SaveEvent) evt).getCompra();
        compra.setEmpresa(empresa);
        Response response = compraService.saveCompra(compra, empresa, CurrentUser.get());
        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    public void delete(ComponentEvent evt) {
        Compra compra = ((CompraDetailForm.SaveEvent) evt).getCompra();
        Response response = compraService.delete(compra.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}