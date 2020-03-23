package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.EmpresaService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.TopBarComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_EMPRESA_MASTER, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_EMPRESA_ADMIN + " | " + Constantes.VIEW_MAIN)
public class EmpresaMasterView extends VerticalLayout {

    private EmpresaMasterGrid grid;
    private EmpresaMasterForm form;
    private TextField filter;
    private EmpresaService empresaService;
    private Empresa empresa;

    public EmpresaMasterView(EmpresaService iService) {
        this.empresaService = iService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout topLayout = createTopBar();
        configureGrid();

        form = new EmpresaMasterForm();
        form.addListener(EmpresaMasterForm.SaveEvent.class, this::saveEmpresa);
        form.addListener(EmpresaMasterForm.DeleteEvent.class, this::deleteEmpresa);
        form.addListener(EmpresaMasterForm.CloseEvent.class, e -> closeEditor());

        H3 title = new H3(Constantes.VIEW_EMPRESAS);
        title.setClassName("titleView");

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
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
        //form.setVisible(false);
        grid.deselectAll();
        showForm(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(empresaService.findAll(filter.getValue(), grid.getPage(), grid.getPageSize()));
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Buscar Empresa por Nombre ó NIT");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> updateList());
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        filter.focus();

        Button btnNewEmpresa = new Button("Nueva");
        btnNewEmpresa.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewEmpresa.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewEmpresa.addClickListener(click -> addEmpresa());
        btnNewEmpresa.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        return new TopBarComponent(filter, btnNewEmpresa);
    }

    private void saveEmpresa(EmpresaMasterForm.SaveEvent evt) {
        EmpresaDTO empresaDTO = evt.getEmpresaDTO();

        empresaService.saveEmpresa(empresaDTO);
        updateList();
        closeEditor();
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
