package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.EmpresaService;
import com.gigti.xfinance.backend.services.TipoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_EMPRESA_MASTER, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_EMPRESA_ADMIN + " | " + Constantes.VIEW_MAIN)
public class EmpresaMasterView extends VerticalLayout {

    private EmpresaMasterGrid grid;
    private final EmpresaMasterForm form;
    private final TextField filter;
    private final EmpresaService empresaService;
    private SearchFilterComponent component;

    public EmpresaMasterView(EmpresaService iService, TipoService tipoService) {
        this.empresaService = iService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        configureGrid();

        form = new EmpresaMasterForm(tipoService.getTiposIdentificacion());
        form.addListener(EmpresaMasterForm.SaveEvent.class, this::saveEmpresa);
        form.addListener(EmpresaMasterForm.DeleteEvent.class, this::deleteEmpresa);
        form.addListener(EmpresaMasterForm.CloseEvent.class, e -> closeEditor());

        H3 title = new H3(Constantes.VIEW_EMPRESAS);
        title.addClassName("titleView2");

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        configureTopBar();
        filter = component.getFilter();

        barAndGridLayout.add(component);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, component);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);

        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid = new EmpresaMasterGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> editEmpresa(evt.getValue()));
    }

    private void addEmpresa() {
        grid.asSingleSelect().clear();
        editEmpresa(new EmpresaDTO());
    }

    private void editEmpresa(EmpresaDTO empresaDTO) {
        if (empresaDTO == null) {
            closeEditor();
        } else {
            form.setEmpresa(empresaDTO);
            form.setVisible(true);
            showForm(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setEmpresa(null);
        grid.deselectAll();
        showForm(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(empresaService.findAll(filter.getValue(), grid.getPage(), grid.getPageSize()));
    }

    public void configureTopBar() {

        component = new SearchFilterComponent("Nueva", "", "Buscar Empresa por Nombre ó NIT", false, true);
        component.getFilter().addValueChangeListener(event -> updateList());
        component.getFilter().focus();
        component.getBtnAdd().addClickListener(click -> addEmpresa());

    }

    private void saveEmpresa(EmpresaMasterForm.SaveEvent evt) {
        EmpresaDTO empresaDTO = evt.getEmpresaDTO();

        Response response = empresaService.saveEmpresa(empresaDTO);
        if (response.isSuccess()) {
            NotificacionesUtil.showSuccess(response.getMessage());
            updateList();
            closeEditor();
        } else {
            NotificacionesUtil.showError(response.getMessage());
        }
    }

    private void deleteEmpresa(EmpresaMasterForm.DeleteEvent evt) {
        EmpresaDTO empresaDTO = evt.getEmpresaDTO();
        Response response = empresaService.deleteEmpresa(empresaDTO.getEmpresaId());
        if(response.isSuccess()){
            Notification.show(response.getMessage(), 3000, Notification.Position.MIDDLE);
            updateList();
            closeEditor();
        } else {
            Notification.show(response.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    public void showForm(boolean show) {
        if(show){
            form.open();
        }else{
            form.close();
            filter.focus();
        }
    }
}
