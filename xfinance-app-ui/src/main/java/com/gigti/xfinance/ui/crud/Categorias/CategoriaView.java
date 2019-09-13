package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "categoria",layout = MainLayout.class)
@RouteAlias(value = "categorias",layout = MainLayout.class)
@RouteAlias(value = "cate",layout = MainLayout.class)
public class CategoriaView extends VerticalLayout {

    public static final String VIEW_NAME = "categoria";
    public static final String VIEW_TITLE = "Categorias Productos";
    private Grid<CategoriaProducto> grid;
    private IcategoriaProductoService icategoriaProductoService;
    private FormLayout formDetails;
    private Binder<CategoriaProducto> binder;
    private TextField tfNombre;
    private TextField tfDesc;

    public CategoriaView(@Autowired IcategoriaProductoService iService) {
        grid = new Grid<>(CategoriaProducto.class);
        formDetails = new FormLayout();
        formDetails.setVisible(false);
        this.icategoriaProductoService = iService;

        structureView();
        refreshGrid();
    }

    private void refreshGrid() {
        List lista = icategoriaProductoService.findAll(CurrentUser.get().getEmpresa());
        grid.setItems(lista);
    }

    private void structureView(){

        //TITULO
        H3 title = new H3(VIEW_TITLE);
        title.setClassName("titleView");
        setSpacing(false);
        setMargin(true);
        //setSizeUndefined();

        //BAR FUNCTION
        HorizontalLayout hlBar = new HorizontalLayout();

        Button btnAdd = new Button("");
        btnAdd.setIcon(new Icon(VaadinIcon.PLUS));
        btnAdd.addClickListener(click -> addCategoria());

        Button btnUpdate = new Button("");
        btnUpdate.setIcon(new Icon(VaadinIcon.EDIT));
        Button btnDelete = new Button("");
        btnDelete.setIcon(new Icon(VaadinIcon.TRASH));

        Button btnRefresh = new Button("");
        btnRefresh.setIcon(new Icon(VaadinIcon.REFRESH));
        btnRefresh.addClickListener(click -> refreshGrid());

        //LISTENERS

        hlBar.add(btnAdd,btnUpdate,btnDelete,btnRefresh);
        hlBar.setSpacing(true);
        hlBar.setMargin(true);
        hlBar.setAlignItems(Alignment.CENTER);

        //FORM LAYOUT

        tfNombre = new TextField();
        tfNombre.setPlaceholder("nombre");
        tfNombre.setRequiredIndicatorVisible(true);
        formDetails.addFormItem(tfNombre, "Nombre");

        tfDesc = new TextField();
        tfDesc.setPlaceholder("Descripción");
        tfDesc.setRequiredIndicatorVisible(true);
        formDetails.addFormItem(tfDesc, "Descripción");

        Button btnSave = new Button("Guardar");
        btnSave.addClickListener(click -> addCategoria());
        //formDetails.add(btnSave);

        Button btnCancel = new Button("Cancelar");
        btnCancel.addClickListener(click -> formDetails.setVisible(false));
        //formDetails.add(btnCancel);

        // Button bar
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(btnSave, btnCancel);

        binder = new Binder<>(CategoriaProducto.class);
        binder.forField(tfNombre);
        binder.forField(tfDesc);

        btnSave.addClickListener(event -> {
            if (binder.validate().isOk()) {
                addCategoria();
            }
        });

        formDetails.add(actions);



        //GRID
        grid.setColumns("nombre", "descripcion");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addItemClickListener(
                event -> {
                    formDetails.setVisible(true);
                    tfNombre.setValue(event.getItem().getNombre());
                    tfDesc.setValue(String.valueOf(event.getItem().getDescripcion()));
                });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);


        HorizontalLayout hlGridForm = new HorizontalLayout();
        hlGridForm.setSpacing(true);
        hlGridForm.setMargin(true);
        hlGridForm.setAlignItems(Alignment.START);
        hlGridForm.add(grid, formDetails);

        add(title, hlBar , hlGridForm);
    }

    private void addCategoria() {
        CategoriaProducto categoria = icategoriaProductoService.guardarCategoria(new CategoriaProducto(tfNombre.getValue(),tfDesc.getValue(),CurrentUser.get().getEmpresa()));
        if(categoria != null){
            Notification.show("OK");
            refreshGrid();
        }
    }

}
