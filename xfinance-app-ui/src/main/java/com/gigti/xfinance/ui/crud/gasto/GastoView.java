package com.gigti.xfinance.ui.crud.gasto;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Gasto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.GastoService;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.time.LocalDate;
import java.util.Objects;

@Route(value = Constantes.VIEW_R_GASTO, layout = MainLayout.class)
@RouteAlias(value = "gasto", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_GASTO +" | "+ Constantes.VIEW_MAIN)
public class GastoView extends VerticalLayout implements ICrudView<Gasto> {

    private final GastoGrid grid;
    private GastoForm form;
    private final GastoService gastoService;
    private final Empresa empresa;
    private DatePicker dateEnd;
    private DatePicker dateStart;
    private SearchFilterAndDatesComponent searchLayout;
    private DataProvider<Gasto, Void> dataProvider;

    public GastoView(GastoService iService) {
        this.gastoService = iService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_GASTO.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureProvider();

        configureGrid(grid = new GastoGrid());

        configureForm();

        this.add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    @Override
    public void closeEditor() {
        form.setGasto(null, "", "");
        grid.deselectAll();
        showForm(false, form, this, null);
        dateStart.focus();
    }

    @Override
    public void configureForm() {
        form = new GastoForm();
        form.addListener(GastoForm.SaveEvent.class, this::save);
        form.addListener(GastoForm.DeleteEvent.class, this::delete);
        form.addListener(GastoForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> gastoService.findAll(empresa, dateStart.getValue(), dateEnd.getValue()
                        , new OffsetBasedPageRequest(query)).stream(),
                query -> gastoService.count(empresa, dateStart.getValue(), dateEnd.getValue()));
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterAndDatesComponent("", true,
                "", "",
                "", true,
                "", true,
                "", true, "Fecha Inicio", "Fecha Fin");
        searchLayout.getFilter().setVisible(false);
        dateStart = searchLayout.getDateStart();
        dateStart.setLabel("De");
        searchLayout.getDateStart().focus();
        dateEnd = searchLayout.getDateEnd();
        dateEnd.setLabel("A");

        dateStart.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());

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

        searchLayout.getBtnAdd().addClickListener(click -> {
            Gasto g = new Gasto();
            g.setEmpresa(empresa);
            addItem(grid, g);
        });
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));

    }

    @Override
    public void editItem(Object gasto) {
        if (gasto == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Gasto) gasto).getId())){
                form.setGasto((Gasto) gasto, Constantes.CREATE_GASTO, ICrudView.OPTION_ADD);
            } else {
                form.setGasto((Gasto) gasto, Constantes.EDIT_GASTO, ICrudView.OPTION_EDIT);
            }
            showForm(true, form, this, null);
            dateStart.focus();
        }
    }

    @Override
    public void deleteItem(Object obj) {
        if (obj == null) {
            closeEditor();
        } else {
            form.setGasto((Gasto) obj, Constantes.DELETE_GASTO, ICrudView.OPTION_DELETE);
            showForm(true, form, this, null);
            dateStart.focus();
        }
    }

    @Override
    public void save(ComponentEvent event) {
        Gasto gasto = ((GastoForm.SaveEvent) event).getGasto();
        gasto.setEmpresa(empresa);
        Response response = gastoService.save(gasto, CurrentUser.get());
        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    @Override
    public void delete(ComponentEvent event) {
        Gasto gasto = ((GastoForm.DeleteEvent) event).getGasto();
        Response response = gastoService.delete(gasto.getId());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}
