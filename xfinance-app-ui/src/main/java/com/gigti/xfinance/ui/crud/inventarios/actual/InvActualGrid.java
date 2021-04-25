/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.actual;

import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.others.AllUtils;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class InvActualGrid extends Grid<InventarioActual> {

    public InvActualGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);

        addColumn(invActual -> invActual.getProducto().getNombreProducto())
                .setHeader("Nombre Producto")
                .setSortable(true);

        addColumn(producto -> producto.getProducto().getTipoMedida().getSimbolo())
                .setHeader("Medida")
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(InventarioActual::getCantidad)
                .setHeader("Cantidad");

        addColumn(inv -> inv.isManageStock() ? "SI" : "NO")
                .setHeader("Controla Stock")
                .setSortable(true);

//        addColumn(new LocalDateRenderer<>(InventarioActualCosto::getFechaActualizacion,
//                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
//                .setHeader("Actualización")
//                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(inv -> AllUtils.formatDate(inv.getFechaActualizacion()))
                .setHeader("Actualización")
                .setTextAlign(ColumnTextAlign.CENTER);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }
}
