package com.gigti.xfinance.ui.crud.reportes.ventas;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.VentaService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.SearchFilterAndDatesComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.time.LocalDate;
import java.util.Objects;

@Route(value = Constantes.VIEW_R_VENTAS, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_VENTAS +" | "+Constantes.VIEW_MAIN)
public class VentasView extends VerticalLayout implements ICrudView<Venta> {

    private final VentaGrid grid;
    private final VentaDetailForm form;
    private TextField filter;
    private final VentaService ventaService;
    private final Empresa empresa;
    private SearchFilterAndDatesComponent searchLayout;
    private DatePicker dateEnd;
    private DatePicker dateStart;
    private DataProvider<Venta, Void> dataProvider;

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
        form = new VentaDetailForm();
        configureForm();

        add(title, searchLayout, grid, form);

        updateList(grid, dataProvider);
        closeEditor();
    }

    @Override
    public void closeEditor() {
        form.setVenta(null, "");
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> ventaService.findAll(filter.getValue(), empresa,
                        dateStart.getValue(), dateEnd.getValue(),
                        new OffsetBasedPageRequest(query)).stream(),
                query -> ventaService.count(filter.getValue(), empresa, dateStart.getValue(), dateEnd.getValue()));
    }

    @Override
    public void configureSearchLayout() {

        searchLayout = new SearchFilterAndDatesComponent("", false,
                "", " # factura",
                "", true,
                "", true,
                "", false, "Fecha Inicio", "Fecha Fin");

        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList(grid, dataProvider));
        filter = searchLayout.getFilter();
        searchLayout.getFilter().focus();

        searchLayout.getBtnAdd().getElement().setAttribute("title", "Nueva Venta - F4");
        searchLayout.getBtnAdd().addClickListener(click -> goToViewVentas());

        searchLayout.getBtnEdit().getElement().setAttribute("title", "Ver Detalle - F4");
        searchLayout.getBtnEdit().setIcon(new Icon(VaadinIcon.EYE));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));

        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));

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

    private void goToViewVentas() {
    }

    @Override
    public void configureForm() {
        form.addListener(VentaDetailForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void save(ComponentEvent event) {
        //Do Nothing
    }

    @Override
    public void delete(ComponentEvent event) {
        //Do Nothing
    }

    @Override
    public void editItem(Object venta) {
        if (venta == null) {
            closeEditor();
        } else {
            form.setVenta((Venta) venta, Constantes.VIEW_VENTA);
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void deleteItem(Object obj) {
        //Do Nothing
    }
}
