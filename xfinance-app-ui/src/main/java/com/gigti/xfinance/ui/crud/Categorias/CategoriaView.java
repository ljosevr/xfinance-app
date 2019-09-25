package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.TopBarComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = Constantes.VIEW_R_CATEGORIA,layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_CATEGORIA,layout = MainLayout.class)
public class CategoriaView extends HorizontalLayout
        implements HasUrlParameter<String>/*, BeforeEnterObserver , AfterNavigationObserver */ {

    private CategoriaGrid grid;
    private CategoriaForm form;
    private TextField filter;
    private CategoriaCrudLogic viewLogic;
    private Button btnNewCategoria;
    private List<CategoriaProducto> lista;

    @Autowired
    public CategoriaView(IcategoriaProductoService iService) {
            viewLogic = new CategoriaCrudLogic(iService,this);
//        if(viewLogic.access()) {
            setSizeFull();
            HorizontalLayout topLayout = createTopBar();

            grid = new CategoriaGrid();
            lista = viewLogic.findAll();
            grid.setItems(lista);
            grid.asSingleSelect().addValueChangeListener(
                    event -> viewLogic.rowSelected(event.getValue()));

            form = new CategoriaForm(viewLogic);

            H3 title = new H3(Constantes.VIEW_CATEGORIA);
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
            add(form);

            viewLogic.init();
//        }else{
//            UI.getCurrent().navigate(MainLayout.class);
//        }
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filtro por Nombre, Descripción de Categoria a Buscar");
        filter.addValueChangeListener(event -> {
            lista = viewLogic.setFilter(event.getValue());
            if(lista != null)
                grid.setItems(lista);
            }
        );
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        btnNewCategoria = new Button("Nueva");
        btnNewCategoria.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewCategoria.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewCategoria.addClickListener(click -> viewLogic.newCategoria());
        // CTRL+N will create a new window which is unavoidable
        btnNewCategoria.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        return new TopBarComponent(filter, btnNewCategoria);
    }


    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

//    public void setNewCategoriaEnabled(boolean enabled) {
//        btnNewCategoria.setEnabled(enabled);
//    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(CategoriaProducto row) {
        grid.getSelectionModel().select(row);
    }

//    public CategoriaProducto getSelectedRow() {
//        return grid.getSelectedRow();
//    }
//
//    public void saveCategoria(CategoriaProducto categoria) {
//        viewLogic.saveCategoria(categoria);
//    }
//
//    public CategoriaProducto findById(String categoriaId) {
//        return viewLogic.findById(categoriaId);
//    }
//
//    public void deleteCategoria(CategoriaProducto categoria) {
//        viewLogic.deleteCategoria(categoria);
//    }

    public void editCategoria(CategoriaProducto categoria) {
        form.editCategoria(categoria);
        showForm(categoria != null);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        //if(viewLogic.access()) {
            viewLogic.enter(parameter);
        //}
    }

    public void refresh(){
        lista = viewLogic.findAll();
        grid.setItems(lista);
    }

    public void refresh(CategoriaProducto categoria){
        lista.add(categoria);
        grid.setItems(lista);
        grid.refresh(categoria);
    }

    public CategoriaGrid getGrid() {
        return grid;
    }

//    @Override
//    public void afterNavigation(AfterNavigationEvent event) {
//        if(lista != null && lista.size() <= 0){
//            refresh();
//        }
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
//        if(lista != null && lista.size() <= 0){
//            refresh();
//        }
//    }
}
