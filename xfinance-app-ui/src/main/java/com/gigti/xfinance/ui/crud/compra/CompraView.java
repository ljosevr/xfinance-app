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
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterAndDatesComponent;
import com.vaadin.flow.component.ComponentEvent;
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

import java.time.LocalDate;
import java.util.List;

@Route(value = Constantes.VIEW_R_COMPRAS, layout = MainLayout.class)
@RouteAlias(value = "compra", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_COMPRAS +" | "+ Constantes.VIEW_MAIN)
@PreserveOnRefresh
public class CompraView extends VerticalLayout implements ICrudView {

    private final VerticalLayout gridLayout;
    private CompraGrid grid;
    private CompraDetailForm form;
    private TextField filter;
    private CompraService compraService;
    private Empresa empresa;
    private SearchFilterAndDatesComponent searchLayout;
    //private DataProvider<Compra, Void> dataProvider;
    private DatePicker dateEnd;
    private DatePicker dateStart;


    public CompraView(CompraService compraService, ProductoService productoService) {
        this.compraService = compraService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        addClassName("CompraView");
        setSizeFull();
        setSpacing(true);
        setPadding(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 title = new H1(Constantes.VIEW_COMPRAS.toUpperCase());
        title.setClassName("titleView2");

        configureProvider();

        configureSearchLayout();

        configureGrid();
        form = new CompraDetailForm(productoService);
        configureForm();

        gridLayout = new VerticalLayout(grid);
        gridLayout.addClassName("grid");
        gridLayout.setPadding(false);

        add(title, searchLayout, gridLayout, form);

        updateList();
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
        gridLayout.setVisible(true);
        searchLayout.setVisible(true);
        grid.deselectAll();
        removeClassName("editing");
    }

    public void configureProvider() {
        //dataProvider = ;
    }

    public void configureGrid() {
        grid = new CompraGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));

        grid.addPageChangeListener(evt -> {
            grid.setPage(evt.getNewPage());
        });
    }

    public void updateList() {
        grid.setDataProvider(DataProvider.fromCallbacks(
                query -> {
                    List<Compra> compras = compraService.
                            findAll(filter.getValue(), empresa,
                                    dateStart.getValue(), dateEnd.getValue(),
                                    grid.getPage(), grid.getPageSize());

                    return compras.stream();
                },
                query -> compraService.count(filter.getValue(), empresa, dateStart.getValue(), dateEnd.getValue())
        ));
    }

    public void configureSearchLayout() {

        searchLayout = new SearchFilterAndDatesComponent("Nueva", "", "Filtro por # Numero de Factura", false, true, "Fecha Inicio", "Fecha Fin");
        searchLayout.getFilter().addValueChangeListener(event -> updateList());
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> addItem());
        searchLayout.getBtnAdd().setMaxWidth("100px");

        filter = searchLayout.getFilter();
        dateStart = searchLayout.getDateStart();
        dateStart.setMaxWidth("150px");
        dateEnd = searchLayout.getDateEnd();
        dateEnd.setMaxWidth("150px");

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
            LocalDate startDate = dateStart.getValue();
            if (selectedDate != null) {
                dateStart.setMax(selectedDate);
            } else {
                dateStart.setMax(null);
            }
        });
    }

    public void addItem() {
        grid.asSingleSelect().clear();
        edit(new Compra());
    }

    public void edit(Object compra) {
        if (compra == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Compra) compra).getId())){
                form.setCompra((Compra) compra, Constantes.CREATE_BUY);
            } else {
                form.setCompra((Compra) compra, Constantes.EDIT_BUY);
            }
            form.setVisible(true);
            gridLayout.setVisible(false);
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
            updateList();
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
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}