package com.gigti.xfinance.ui.crud.cliente;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ClienteService;
import com.gigti.xfinance.backend.services.TipoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H1;
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
public class ClienteView extends VerticalLayout implements ICrudView<Cliente> {

    private ClienteGrid grid;
    private final ClienteForm form;
    private TextField filter;
    private final ClienteService clienteService;
    private final Empresa empresa;
    private SearchFilterComponent searchLayout;
    private DataProvider<Cliente, Void> dataProvider;

    public ClienteView(ClienteService clienteService, TipoService tipoService) {
        this.clienteService = clienteService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_ADMIN_CLIENTE.toUpperCase());
        title.addClassName("titleView2");

        configureProvider();

        configureSearchLayout();
        filter = searchLayout.getFilter();

        configureGrid(grid = new ClienteGrid());

        form = new ClienteForm(tipoService.getTiposIdentificacion());

        configureForm();

        add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    @Override
    public void closeEditor() {
        form.setCliente(null, "");
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    @Override
    public void configureProvider() {
        dataProvider =
                DataProvider.fromCallbacks(
                        query -> clienteService.find(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                        query -> clienteService.countSearch(filter.getValue(), empresa)
                );
    }

    @Override
    public void configureSearchLayout() {
        searchLayout = new SearchFilterComponent("", true,
                "", "Filtro Nombre Producto",
                "", true,
                "", true,
                "", false);
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList(grid, dataProvider));
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> {
            Cliente c = new Cliente();
            c.setPersona(new Persona());
            addItem(grid, c);
        });
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        filter = searchLayout.getFilter();
    }

    @Override
    public void configureForm() {
        form.addListener(ClienteForm.SaveEvent.class, this::save);
        form.addListener(ClienteForm.DeleteEvent.class, this::delete);
        form.addListener(ClienteForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void editItem(Object cliente) {
        if (cliente == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Cliente) cliente).getId())){
                form.setCliente((Cliente) cliente, Constantes.CREATE_CLIENT);
            } else {
                form.setCliente((Cliente) cliente, Constantes.EDIT_CLIENT);
            }
            form.setVisible(true);
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void deleteItem(Object obj) {

    }

    @Override
    public void save(ComponentEvent event) {
        Cliente cliente = ((ClienteForm.SaveEvent) event).getCliente();
        cliente.getPersona().setEmpresa(empresa);
        Response response = clienteService.save(cliente, CurrentUser.get());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
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
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

}
