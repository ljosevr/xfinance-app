package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.ui.MainLayout;
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
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SpringComponent
@UIScope
@Route(value = "categoria",layout = MainLayout.class)
@RouteAlias(value = "categorias",layout = MainLayout.class)
public class CategoriaView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Categorias Productos";

    private CategoriaGrid grid;
    private CategoriaForm form;
    private TextField filter;

    private CategoriaCrudLogic viewLogic;
    private Button btnNewCategoria;

    @Autowired
    private CategoriaDataProvider dataProvider;


    //public CategoriaView(@Autowired IcategoriaProductoService iService) {
    public CategoriaView() {
            viewLogic = new CategoriaCrudLogic(this);

            setSizeFull();
            HorizontalLayout topLayout = createTopBar();

            dataProvider = CategoriaDataProvider.getInstance();//.ofCollection(iProductoService.findAll(CurrentUser.get().getEmpresa()));

            grid = new CategoriaGrid();
            grid.setDataProvider(dataProvider);
            grid.setItems(dataProvider.findAll());
            grid.asSingleSelect().addValueChangeListener(
                    event -> viewLogic.rowSelected(event.getValue()));

            form = new CategoriaForm(viewLogic);

            H3 title = new H3(this.VIEW_NAME);
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
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filtro por Nombre, Descripción de Categoria a Buscar");
        filter.addValueChangeListener(event -> {
            grid.setItems(dataProvider.setFilter(event.getValue()));
            }
        );
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        btnNewCategoria = new Button("Nueva");
        btnNewCategoria.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNewCategoria.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnNewCategoria.addClickListener(click -> viewLogic.newCategoria());
        // CTRL+N will create a new window which is unavoidable
        btnNewCategoria.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(btnNewCategoria);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }


    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewCategoriaEnabled(boolean enabled) {
        btnNewCategoria.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(CategoriaProducto row) {
        grid.getSelectionModel().select(row);
    }

    public CategoriaProducto getSelectedRow() {
        return grid.getSelectedRow();
    }

    public boolean saveCategoria(CategoriaProducto categoria) {
        return dataProvider.save(categoria);
    }

    public CategoriaProducto findById(String categoriaId) {
        return dataProvider.findById(categoriaId);
    }

    public boolean deleteCategoria(CategoriaProducto categoria) {
        return dataProvider.delete(categoria);
    }

    public void editCategoria(CategoriaProducto categoria) {
        showForm(categoria != null);
        form.editCategoria(categoria);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

    public void refresh(){
        grid.setItems(dataProvider.findAll());
    }

    public void refresh(CategoriaProducto categoria){
        grid.refresh(categoria);
    }

}
