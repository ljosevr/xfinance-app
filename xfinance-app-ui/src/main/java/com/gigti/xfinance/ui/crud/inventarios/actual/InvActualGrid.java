/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.actual;

import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.GridVariant;
import org.vaadin.klaudeta.PaginatedGrid;

public class InvActualGrid extends PaginatedGrid<InventarioActual> {

    public InvActualGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(invActual -> invActual.getProducto().getNombreProducto())
                .setHeader("Nombre Producto")
                .setFlexGrow(15)
                .setSortable(true);

        addColumn(producto -> AllUtils.formatUnidadMedida(producto.getProducto().getTipoMedida().name()))
                .setHeader("Medida")
                .setFlexGrow(3)
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(InventarioActual::getCantidad)
                .setFlexGrow(5)
                .setHeader("Cantidad");

        addColumn(inv -> inv.isInfinite() ? "SI" : "NO")
                .setHeader("Infinito")
                .setFlexGrow(5)
                .setSortable(true);

        addColumn(inv -> AllUtils.formatDate(inv.getFechaActualizacion()))
                .setHeader("Actualización")
                .setFlexGrow(7)
                .setTextAlign(ColumnTextAlign.CENTER);

        setPageSize(Constantes.PAGE_SIZE_10);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
