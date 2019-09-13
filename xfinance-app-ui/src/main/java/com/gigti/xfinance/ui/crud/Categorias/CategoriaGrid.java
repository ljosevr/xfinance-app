package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Producto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.DecimalFormat;
import java.util.Comparator;

public class CategoriaGrid extends Grid<CategoriaProducto> {

    public CategoriaGrid() {
        setSizeFull();

        addColumn(CategoriaProducto::getNombre)
                .setHeader("Nombre")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(CategoriaProducto::getDescripcion)
                .setHeader("Descripción")
                .setFlexGrow(30)
                .setSortable(true);

    }

    public CategoriaProducto getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(CategoriaProducto categoria) {
        getDataCommunicator().refresh(categoria);
    }
}
