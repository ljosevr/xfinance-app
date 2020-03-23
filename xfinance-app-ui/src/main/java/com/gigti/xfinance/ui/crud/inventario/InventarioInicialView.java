/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventario;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.InventarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.TopBarComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

@Route(value = Constantes.VIEW_R_INVENTARIO_INICIAL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_INVENTARIO_INICIAL +" | "+ Constantes.VIEW_MAIN)
public class InventarioInicialView extends VerticalLayout {

    private Empresa empresa;
    private InventarioGrid grid;
    private TextField filter;
    private InventarioService inventarioService;
    private InventarioInicialForm form;

    public InventarioInicialView(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        addClassName("InventarioView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout topLayout = createTopBar();
        configureGrid();

        form = new InventarioInicialForm();
        form.addListener(InventarioInicialForm.SaveEvent.class, this::saveInventario);
        form.addListener(InventarioInicialForm.CloseEvent.class, e -> closeEditor());

        H3 title = new H3(Constantes.VIEW_INVENTARIO);
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

    private void closeEditor() {
        form.setInventario(null);
        grid.deselectAll();
        showForm(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        grid = new InventarioGrid();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(evt -> editInventario(evt.getValue()));

        //Editor

//        Binder<ProductoInventario> binder = new Binder<>(ProductoInventario.class);
//        Editor<ProductoInventario> editor = grid.getEditor();
//        editor.setBinder(binder);
//
//        NumberField cantidadField = new NumberField("Cantidad");
//        cantidadField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
//        //binder.bind(cantidadField, "quantity");
//
//        grid.getColumnByKey("cantidad").setEditorComponent(item -> {
//            cantidadField.setValue(item.getQuantity().doubleValue());
//            return cantidadField;
//        });
//
//
//        NumberField precioCostoField = new NumberField("Precio Costo");
//        precioCostoField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
//        precioCostoField.setPrefixComponent(new Span("$"));
//        //binder.bind(precioCostoField, "precioCosto");
//        grid.getColumnByKey("pcosto").setEditorComponent(item -> {
//            precioCostoField.setValue(item.getPrecioCosto().doubleValue());
//            return precioCostoField;
//        });
//
//        NumberField precioVentaField = new NumberField("Precio Venta");
//        precioVentaField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
//        precioVentaField.setPrefixComponent(new Span("$"));
//        //binder.bind(precioVentaField, "precioVenta");
//        grid.getColumnByKey("pventa").setEditorComponent(item -> {
//            precioVentaField.setValue(item.getPrecioVenta().doubleValue());
//            return precioVentaField;
//        });
//
//        grid.addItemDoubleClickListener(event -> {
//            grid.getEditor().editItem(event.getItem());
//            cantidadField.focus();
//        });
//
//        binder.addValueChangeListener(event -> {
//            grid.getEditor().refresh();
//        });
//
//        grid.getEditor().addCloseListener(event -> {
//            if (binder.getBean() != null) {
//
//            }
//        });

    }

    private void editInventario(InventarioInicial inventario) {
        if (inventario == null) {
            closeEditor();
        } else {
            if(StringUtils.isBlank(inventario.getId())) {
                form.setInventario(inventario);
                form.setVisible(true);
                showForm(true);
                addClassName("editing");
            } else {
                Notification.show("Este Inventario No se puede modificar, ya fue actualizado");
            }
        }
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filtro Nombre");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> updateList());
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        filter.focus();

        return new TopBarComponent(filter);
    }

    private void updateList() {
        grid.setItems(inventarioService.findAll(filter.getValue(), empresa, grid.getPage(), grid.getPageSize()));
    }

    private void saveInventario(InventarioInicialForm.SaveEvent evt) {
        InventarioInicial inventario = evt.getInventario();

        Response response = inventarioService.saveInventarioInicial(inventario, CurrentUser.get());
        Notification.show(response.getMessage());
        if(response.isSuccess()) {
            updateList();
            closeEditor();
        }
    }

    public void showForm(boolean show) {
        if(show){
            if(!form.isOpened())
                form.open();
        }else{
            if(form.isOpened())
                form.close();
            filter.focus();
        }
    }

}
