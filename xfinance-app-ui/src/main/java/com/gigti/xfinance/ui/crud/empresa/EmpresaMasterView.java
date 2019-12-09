package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.IEmpresaService;
import com.gigti.xfinance.ui.MainLayout;
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
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

@Route(value = Constantes.VIEW_R_EMPRESA_MASTER, layout = MainLayout.class)
public class EmpresaMasterView extends HorizontalLayout
        implements HasUrlParameter<String> {

    private EmpresaMasterGrid grid;
    private EmpresaMasterForm form;
    private TextField filter;
    private EmpresaMasterCrudLogic viewLogic;
    private Button btnNewEmpresa;
    private List<EmpresaDTO> lista;
    private VerticalLayout barAndGridLayout;

    @Autowired
    public EmpresaMasterView(IEmpresaService iService) {
            viewLogic = new EmpresaMasterCrudLogic(iService,this);
//        if(viewLogic.access()) {
            setSizeFull();
            HorizontalLayout topLayout = createTopBar();

            grid = new EmpresaMasterGrid();
            lista = viewLogic.findAll();
            grid.setItems(lista);
            grid.asSingleSelect().addValueChangeListener(
                    event -> viewLogic.rowSelected(event.getValue()));

            form = new EmpresaMasterForm(viewLogic);

            H3 title = new H3(Constantes.VIEW_EMPRESAS);
            title.setClassName("titleView");

            barAndGridLayout = new VerticalLayout();
            barAndGridLayout.add(title);

            barAndGridLayout.add(topLayout);
            barAndGridLayout.add(grid);
            barAndGridLayout.setFlexGrow(1, grid);
            barAndGridLayout.setFlexGrow(0, topLayout);
            barAndGridLayout.setSizeFull();
            barAndGridLayout.expand(grid);

            add(barAndGridLayout);
            add(form);

            viewLogic.init();
//        }else{
//            UI.getCurrent().navigate(MainLayout.class);
//        }
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Buscar Empresa por Nombre ó NIT");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> {
            lista = viewLogic.setFilter(event.getValue());
            if(lista != null)
                grid.setItems(lista);
            }
        );
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        btnNewEmpresa = new Button("Nueva");
        btnNewEmpresa.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewEmpresa.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewEmpresa.addClickListener(click -> viewLogic.nuevo());
        // CTRL+N will create a new window which is unavoidable
        btnNewEmpresa.addClickShortcut(Key.F4);

        return new TopBarComponent(filter, btnNewEmpresa);
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(EmpresaDTO row) {
        grid.getSelectionModel().select(row);
    }

    public void edit(EmpresaDTO empresa) {
        form.edit(empresa);
        showForm(empresa != null);
    }

    public void showForm(boolean show) {
        if(show){
            barAndGridLayout.setVisible(false);
        }else{
            barAndGridLayout.setVisible(true);
        }
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

    public void refresh(){
        lista = viewLogic.findAll();
        grid.setItems(lista);
    }

    public void refresh(EmpresaDTO empresa){
        for(Iterator<EmpresaDTO> it = lista.iterator(); it.hasNext();){
            EmpresaDTO e = it.next();
            if(e.getEmpresaId().equals(empresa.getEmpresaId())) {
                it.remove();
                lista.remove(e);
                break;
            }
        }

        lista.add(empresa);
        grid.setItems(lista);
        grid.refresh(empresa);
    }

    public EmpresaMasterGrid getGrid() {
        return grid;
    }

    public List<EmpresaDTO> getItemsGrid(){
        return lista;
    }
}
