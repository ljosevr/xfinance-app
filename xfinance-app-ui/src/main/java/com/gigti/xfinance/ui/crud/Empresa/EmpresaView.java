package com.gigti.xfinance.ui.crud.Empresa;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
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

@Route(value = Constantes.VIEW_R_EMPRESA,layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_EMPRESA,layout = MainLayout.class)
public class EmpresaView extends HorizontalLayout
        implements HasUrlParameter<String> {

    private EmpresaGrid grid;
    private EmpresaForm form;
    private TextField filter;
    private EmpresaCrudLogic viewLogic;
    private Button btnNewEmpresa;
    private List<Empresa> lista;
    private VerticalLayout barAndGridLayout;

    @Autowired
    public EmpresaView(IEmpresaService iService) {
            viewLogic = new EmpresaCrudLogic(iService,this);
//        if(viewLogic.access()) {
            setSizeFull();
            HorizontalLayout topLayout = createTopBar();

            grid = new EmpresaGrid();
            lista = viewLogic.findAll();
            grid.setItems(lista);
            grid.asSingleSelect().addValueChangeListener(
                    event -> viewLogic.rowSelected(event.getValue()));

            form = new EmpresaForm(viewLogic);

            H3 title = new H3(Constantes.VIEW_EMPRESA);
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
        btnNewEmpresa.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

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

    public void selectRow(Empresa row) {
        grid.getSelectionModel().select(row);
    }

    public void edit(Empresa empresa) {
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

    public void refresh(Empresa empresa){
        for(Iterator<Empresa> it = lista.iterator(); it.hasNext();){
            Empresa e = it.next();
            if(e.getId().equals(empresa.getId())) {
                it.remove();
                lista.remove(e);
                break;
            }
        }

        lista.add(empresa);
        grid.setItems(lista);
        grid.refresh(empresa);
    }

    public EmpresaGrid getGrid() {
        return grid;
    }

    public List<Empresa> getItemsGrid(){
        return lista;
    }
}
