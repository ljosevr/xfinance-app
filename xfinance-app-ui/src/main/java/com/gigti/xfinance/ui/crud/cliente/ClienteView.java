package com.gigti.xfinance.ui.crud.cliente;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ClienteService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jsoup.internal.StringUtil;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.Objects;

@Route(value = Constantes.VIEW_R_ADMIN_CLIENTE, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_ADMIN_CLIENTE +" | "+Constantes.VIEW_MAIN)
public class ClienteView extends VerticalLayout implements ICrudView {

    private ClienteGrid grid;
    private final ClienteForm form;
    private final TextField filter;
    private final ClienteService clienteService;
    private final Empresa empresa;
    private SearchFilterComponent searchLayout;

    public ClienteView(ClienteService clienteService) {
        this.clienteService = clienteService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        H1 title = new H1(Constantes.VIEW_ADMIN_CLIENTE.toUpperCase());
        title.addClassName("titleView2");

        addClassName("view");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureProvider();

        configureSearchLayout();
        filter = searchLayout.getFilter();

        configureGrid();

        form = new ClienteForm();

        configureForm();

        FlexLayout flexLayout = new FlexLayout(grid, form);
        flexLayout.addClassName("content");
        flexLayout.setSizeFull();
        flexLayout.setFlexGrow(3, grid);
        flexLayout.setFlexGrow(1, form);

        add(title, searchLayout, flexLayout);

        updateList();
        closeEditor();
    }

    @Override
    public void closeEditor() {
        form.setCliente(null, "");
        form.setVisible(false);
        grid.deselectAll();
        removeClassName("editing");
    }

    @Override
    public void updateList() {
        grid.setDataProvider(
                DataProvider.fromCallbacks(
                        query -> clienteService.find(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                        query -> (int) clienteService.countSearch(filter.getValue(), empresa)
                ));
    }

    @Override
    public void configureProvider() {

    }

    @Override
    public void configureGrid() {
        grid = new ClienteGrid();
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("", "", "Filtro por Nombre o Identificación", false, true);
        searchLayout.getFilter().addValueChangeListener(event -> updateList());
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> addItem());
    }

    @Override
    public void configureForm() {
        form.addListener(ClienteForm.SaveEvent.class, this::save);
        form.addListener(ClienteForm.DeleteEvent.class, this::delete);
        form.addListener(ClienteForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void edit(Object cliente) {
        if (cliente == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Cliente) cliente).getId())){
                form.setCliente((Cliente) cliente, Constantes.CREATE_CLIENT);
            } else {
                form.setCliente((Cliente) cliente, Constantes.EDIT_CLIENT);
            }
            form.setVisible(true);
            addClassName("editing");
        }
    }

    @Override
    public void save(ComponentEvent event) {
        Cliente cliente = ((ClienteForm.SaveEvent) event).getCliente();
        cliente.getPersona().setEmpresa(empresa);
        Response response = clienteService.save(cliente, CurrentUser.get());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    @Override
    public void delete(ComponentEvent event) {
        Cliente cliente = ((ClienteForm.DeleteEvent) event).getCliente();
        Response response = clienteService.delete(cliente, CurrentUser.get());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    @Override
    public void addItem() {
        grid.asSingleSelect().clear();
        Cliente c = new Cliente();
        c.setPersona(new Persona());
        edit(c);
    }
}
