package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class CategoriaGrid extends Grid<CategoriaProducto> {

    public CategoriaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

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
