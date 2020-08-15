package com.gigti.xfinance.ui.crud.proveedor;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ProveedorService;
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

@Route(value = Constantes.VIEW_R_ADMIN_PROVEEDOR, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_ADMIN_PROVEEDOR +" | "+Constantes.VIEW_MAIN)
public class ProveedorView extends VerticalLayout implements ICrudView<Proveedor> {

    private final Empresa empresa;
    private final ProveedorService proveedorService;
    private final ProveedorGrid grid;
    private ProveedorForm form;
    private TextField filter;
    private SearchFilterComponent searchLayout;
    private DataProvider<Proveedor, Void> dataProvider;

    public ProveedorView(ProveedorService service, TipoService tipoService) {
        this.proveedorService = service;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;

        detailLayout(this);

        H1 title = new H1(Constantes.VIEW_ADMIN_PROVEEDOR.toUpperCase());
        title.addClassName("titleView2");

        configureSearchLayout();

        configureProvider();

        configureGrid(grid = new ProveedorGrid());
        form = new ProveedorForm(tipoService.getTiposIdentificacion());
        configureForm();

        this.add(title, searchLayout, grid);

        updateList(grid, dataProvider);
        closeEditor();
    }

    public void closeEditor() {
        form.setProveedor(null, "", "");
        grid.deselectAll();
        showForm(false, form, this, filter);
    }

    public void configureForm() {
        form.addListener(ProveedorForm.SaveEvent.class, this::save);
        form.addListener(ProveedorForm.DeleteEvent.class, this::delete);
        form.addListener(ProveedorForm.CloseEvent.class, e -> closeEditor());
    }

    public void configureProvider() {
        dataProvider = DataProvider.fromCallbacks(
                query -> proveedorService.find(filter.getValue(), empresa, new OffsetBasedPageRequest(query)).stream(),
                query -> proveedorService.countSearch(filter.getValue(), empresa));
    }

    public void configureSearchLayout() {

        searchLayout = new SearchFilterComponent("", true,
                "", "Filtro Nombre Proveedor",
                "", true,
                "", true,
                "", true);
        searchLayout.getFilter().addKeyPressListener(Key.ENTER, enter -> updateList(grid, dataProvider));
        searchLayout.getFilter().focus();
        searchLayout.getBtnAdd().addClickListener(click -> {
            Proveedor p = new Proveedor();
            p.setActivo(true);
            addItem(grid, p);
        });
        searchLayout.getBtnSearch().addClickListener(click -> updateList(grid, dataProvider));
        searchLayout.getBtnEdit().addClickListener(click -> editItem(grid.asSingleSelect().getValue()));
        searchLayout.getBtnDelete().addClickListener(click -> deleteItem(grid.asSingleSelect().getValue()));
        filter = searchLayout.getFilter();
    }

    public void editItem(Object proveedor) {
        if (proveedor == null) {
            closeEditor();
        } else {
            if(StringUtil.isBlank(((Proveedor) proveedor).getId())){
                form.setProveedor((Proveedor) proveedor, Constantes.CREATE_PRODUCT, ICrudView.OPTION_ADD);
            } else {
                form.setProveedor((Proveedor) proveedor, Constantes.EDIT_PRODUCT, ICrudView.OPTION_EDIT);
            }
            showForm(true, form, this, filter);
        }
    }

    @Override
    public void deleteItem(Object obj) {
        if (obj == null) {
            closeEditor();
        } else {
            form.setProveedor((Proveedor) obj, Constantes.DELETE_PRODUCTO, ICrudView.OPTION_DELETE);

            showForm(true, form, this, filter);
        }
    }

    public void save(ComponentEvent evt) {
        Proveedor proveedor = ((ProveedorForm.SaveEvent) evt).getProveedor();
        proveedor.setEmpresa(empresa);
        Response response = proveedorService.save(proveedor, CurrentUser.get());
        if(response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    public void delete(ComponentEvent evt) {
        Proveedor proveedor = ((ProveedorForm.DeleteEvent) evt).getProveedor();
        Response response = proveedorService.delete(proveedor, CurrentUser.get());
        if(response.isSuccess()){
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList(grid, dataProvider);
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }
}
