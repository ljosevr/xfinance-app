package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import org.vaadin.klaudeta.PaginatedGrid;

public class CategoriaGrid extends PaginatedGrid<CategoriaProducto> {

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

        setPageSize(Constantes.PAGE_SIZE);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
    }

    public CategoriaProducto getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(CategoriaProducto categoria) {
        getDataCommunicator().refresh(categoria);
    }

}
