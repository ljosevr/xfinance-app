package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.authentication.LoginScreen;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "categoria",layout = MainLayout.class)
@RouteAlias(value = "categorias",layout = MainLayout.class)
@RouteAlias(value = "cate",layout = MainLayout.class)
//@ParentLayout(value = MainLayout.class)
public class CategoriaView extends HorizontalLayout implements HasUrlParameter<String>/*, BeforeEnterObserver*/ {

    public static final String VIEW_NAME = "categoria";
    public static final String VIEW_TITLE = "Categorias Productos";

    private CategoriaGrid grid;
    private CategoriaForm form;
    private TextField filter;

    private CategoriaCrudLogic viewLogic;
    private Button btnNewCategoria;
    private Empresa empresa;

    private CategoriaDataProvider dataProvider;

    public CategoriaView(@Autowired IcategoriaProductoService iService) {
//        if(CurrentUser.get() != null) {
            viewLogic = new CategoriaCrudLogic(iService,this);

            setSizeFull();
            HorizontalLayout topLayout = createTopBar();

            //empresa = CurrentUser.get().getEmpresa();
            dataProvider = CategoriaDataProvider.getInstance(iService);//.ofCollection(iProductoService.findAll(CurrentUser.get().getEmpresa()));

            grid = new CategoriaGrid();
            grid.setDataProvider(dataProvider);
            grid.setItems(dataProvider.findAll());
            grid.asSingleSelect().addValueChangeListener(
                    event -> viewLogic.rowSelected(event.getValue()));

            form = new CategoriaForm(viewLogic);
            //form.setCategories(dataProvider.findAll());

            H3 title = new H3(this.VIEW_TITLE);
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
//        } else {
//            UI.getCurrent().navigate(LoginScreen.class);
//        }
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filtro Nombre, Categoria");
        // Apply the filter to grid's data provider. TextField value is never null
        filter.addValueChangeListener(event -> {
            //dataProvider.setFilterByValue(CategoriaProducto::getNombre, event.getValue());
            dataProvider.setFilter(event.getValue());
            refresh();
            //System.out.println(CategoriaProducto::getNombre);
            }
        );
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        btnNewCategoria = new Button("Categoria Nueva");
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

    public boolean deleteCategoria(CategoriaProducto categoria) {
        return dataProvider.delete(categoria);
    }

    public void editCategoria(CategoriaProducto categoria) {
        showForm(categoria != null);
        form.editCategoria(categoria);
    }

    public void showForm(boolean show) {
        form.setVisible(show);

        /* FIXME The following line should be uncommented when the CheckboxGroup
         * issue is resolved. The category CheckboxGroup throws an
         * IllegalArgumentException when the form is disabled.
         */
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
