package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import java.util.Iterator;
import java.util.List;

@Route(value = Constantes.VIEW_R_CATEGORIA, layout = MainLayout.class)
@RouteAlias(value = "categoria", layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_MAIN)
public class CategoriaView extends HorizontalLayout
        implements HasUrlParameter<String> {

    private CategoriaGrid grid;
    private CategoriaForm form;
    private TextField filter;
    private CategoriaCrud viewLogic;
    private List<CategoriaProducto> lista;
    private SearchFilterComponent component;

    public CategoriaView(CategoriaProductoService iService) {
        viewLogic = new CategoriaCrud(iService, this);
        setSizeFull();
        configureTopBar();
        filter = component.getFilter();

        configureGrid();

        form = new CategoriaForm(viewLogic);

        H3 title = new H3(Constantes.VIEW_CATEGORIA);
        title.addClassName("titleView2");

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(component);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, component);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        //add(form);

        viewLogic.init();
//        }else{
//            UI.getCurrent().navigate(MainLayout.class);
//        }
    }

    private void configureGrid() {
        grid = new CategoriaGrid();
        lista = viewLogic.findAll();
        grid.setItems(lista);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        grid.getColumns().forEach(col -> col.setAutoWidth(true)); //
    }

    public void configureTopBar() {

        component = new SearchFilterComponent("Nueva", "", "Filtro por Nombre", false, true);
        component.getFilter().addValueChangeListener(event -> {
            lista = viewLogic.setFilter(event.getValue());
            if (lista != null)
                grid.setItems(lista);
        });
        component.getFilter().focus();
        component.getBtnAdd().addClickListener(click -> viewLogic.newItem());
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

    public void selectRow(CategoriaProducto row) {
        grid.getSelectionModel().select(row);
    }

    public void editCategoria(CategoriaProducto categoria) {
        form.editCategoria(categoria);
        showForm(categoria != null);
    }

    public void showForm(boolean show) {
        if (show) {
            form.open();
        } else {
            filter.focus();
            form.close();
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

    public void refresh() {
        lista = viewLogic.findAll();
        grid.setItems(lista);
    }

    public void refresh(CategoriaProducto categoria) {
        for (Iterator<CategoriaProducto> it = lista.iterator(); it.hasNext(); ) {
            CategoriaProducto p = it.next();
            if (p.getId().equals(categoria.getId())) {
                it.remove();
                lista.remove(p);
                break;
            }
        }
        lista.add(categoria);
        grid.setItems(lista);
        grid.refresh(categoria);
    }

    public CategoriaGrid getGrid() {
        return grid;
    }

    public List<CategoriaProducto> getItemsGrid() {
        return lista;
    }

    public TextField getFilter() {
        return filter;
    }
}
