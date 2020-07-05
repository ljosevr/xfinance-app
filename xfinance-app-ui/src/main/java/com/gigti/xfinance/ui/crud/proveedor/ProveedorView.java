package com.gigti.xfinance.ui.crud.proveedor;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ProveedorService;
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

@Route(value = Constantes.VIEW_R_ADMIN_PROVEEDOR, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_ADMIN_PROVEEDOR +" | "+Constantes.VIEW_MAIN)
public class ProveedorView extends VerticalLayout implements ICrudView {

    private ProveedorGrid grid;
    private ProveedorForm form;
    private TextField filter;
    private final ProveedorService proveedorService;
    private final Empresa empresa;
    private SearchFilterComponent searchLayout;

    public ProveedorView(ProveedorService service) {
        this.proveedorService = service;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getPersona().getEmpresa() : null;

        H1 title = new H1(Constantes.VIEW_ADMIN_PROVEEDOR.toUpperCase());
        title.addClassName("titleView2");

        addClassName("view");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureProvider();

        configureSearchLayout();
        filter = searchLayout.getFilter();

        configureGrid();

        form = new ProveedorForm();

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
        form.setProveedor(null, "");
        form.setVisible(false);
        grid.deselectAll();
        removeClassName("editing");
    }

    @Override
    public void updateList() {
        grid.setDataProvider(
                DataProvider.fromCallbacks(
                        query -> proveedorService.find(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                        query -> (int) proveedorService.countSearch(filter.getValue(), empresa)
        ));
    }

    @Override
    public void configureProvider() {

    }

    @Override
    public void configureGrid() {
        grid = new ProveedorGrid();
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
        form.addListener(ProveedorForm.SaveEvent.class, this::save);
        form.addListener(ProveedorForm.DeleteEvent.class, this::delete);
        form.addListener(ProveedorForm.CloseEvent.class, e -> closeEditor());
    }

    @Override
    public void edit(Object proveedor) {
        if (proveedor == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Proveedor) proveedor).getId())){
                form.setProveedor((Proveedor) proveedor, Constantes.CREATE_PROVEEDOR);
            } else {
                form.setProveedor((Proveedor) proveedor, Constantes.EDIT_PROVEEDOR);
            }
            form.setVisible(true);
            addClassName("editing");
        }
    }

    @Override
    public void save(ComponentEvent event) {
        Proveedor proveedor = ((ProveedorForm.SaveEvent) event).getProveedor();
        proveedor.setEmpresa(empresa);
        Response response = proveedorService.save(proveedor, CurrentUser.get());
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
        Proveedor proveedor = ((ProveedorForm.DeleteEvent) event).getProveedor();
        Response response = proveedorService.delete(proveedor, CurrentUser.get());
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
        Proveedor p = new Proveedor();
        p.setActivo(true);
        edit(p);
    }
}
