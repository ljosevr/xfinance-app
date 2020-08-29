package com.gigti.xfinance.ui.crud.reportes.ventas;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.VentaService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.compra.CompraDetailForm;
import com.gigti.xfinance.ui.crud.compra.CompraGrid;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.SearchFilterAndDatesComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;

@Route(value = Constantes.VIEW_R_VENTAS, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_VENTAS +" | "+Constantes.VIEW_MAIN)
public class VentasView extends VerticalLayout implements ICrudView<Venta> {

    private VentaGrid grid;
    private final CompraDetailForm form;
    private TextField filter;
    private final VentaService ventaService;
    private final Empresa empresa;
    private SearchFilterAndDatesComponent searchLayout;
    private DatePicker dateEnd;
    private DatePicker dateStart;
    private DataProvider<Compra, Void> dataProvider;

    public VentasView(VentaService ventaService) {
        this.ventaService = ventaService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        setSizeFull();
        setSpacing(true);
        setPadding(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 title = new H1(Constantes.VIEW_VENTAS.toUpperCase());
        title.setClassName("titleView2");

        configureProvider();

        configureSearchLayout();

        configureGrid(grid = new VentaGrid());
        form = new CompraDetailForm(productoService, proveedorService);
        configureForm();

        add(title, searchLayout, grid, form);

        updateList(grid, dataProvider);
        closeEditor();
    }

    @Override
    public void closeEditor() {

    }

    @Override
    public void configureProvider() {

    }

    @Override
    public void configureSearchLayout() {

    }

    @Override
    public void configureForm() {

    }

    @Override
    public void save(ComponentEvent event) {

    }

    @Override
    public void delete(ComponentEvent event) {

    }

    @Override
    public void editItem(Object obj) {

    }

    @Override
    public void deleteItem(Object obj) {

    }
}
