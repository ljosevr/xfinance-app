package com.gigti.xfinance.ui.util;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.backend.services.VentaService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchProductByNameComponent extends VerticalLayout {

    private Button btnClose;
    private TextField filter;
    private Grid<PventaDTO> grid;
    private Grid<Producto> gridPro;
    private VentaService ventaService;
    private Empresa empresa;
    private List<PventaDTO> listData;
    private List<Producto> listDataPro;
    private ProductoService productoService;

    public SearchProductByNameComponent(VentaService ventaService, String labelFilter, String placeHolderFilter) {
        this.ventaService = ventaService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.addFocusShortcut(Key.F3);
        filter.addValueChangeListener(event -> updateGrid());
        filter.addKeyPressListener(Key.ENTER, listener ->  grid.focus());
        filter.focus();

        btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        grid = new Grid<>(PventaDTO.class);
        grid.setColumns("nombreProducto");

        this.add(new HorizontalLayout(filter, btnClose), grid);
    }

    public SearchProductByNameComponent(ProductoService productoService, String labelFilter, String placeHolderFilter) {
        this.productoService = productoService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.addFocusShortcut(Key.F3);
        filter.addValueChangeListener(event -> updateGridPro());
        filter.addKeyPressListener(Key.ENTER, listener ->  gridPro.focus());
        filter.focus();

        btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);



        this.add(new HorizontalLayout(filter, btnClose), gridPro);
    }


    private void updateGrid() {
        if(StringUtils.isNotBlank(filter.getValue())) {
            listData = ventaService.findByName(filter.getValue(), empresa, 0, 20);
            grid.setItems(listData);
        }
    }

    private void updateGridPro() {
        if(StringUtils.isNotBlank(filter.getValue())) {
            listDataPro = productoService.findAll(filter.getValue(), empresa, 0, 20);
            gridPro.setItems(listDataPro);
        }
    }

    public Grid<PventaDTO> getGrid() {
        return grid;
    }

    public Grid<Producto> getGridPro() {
        return gridPro;
    }

    public TextField getFilter() {
        return filter;
    }

    public List<PventaDTO> getListData() {
        return listData;
    }

    public List<Producto> getListDataPro() {
        return listDataPro;
    }

    public Button getBtnClose(){
        return btnClose;
    }

    public void clear(){
        filter.clear();
        grid.setItems(new ArrayList<>());
    }

}
