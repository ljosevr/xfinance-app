/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.reportes.ventas;

import com.gigti.xfinance.backend.data.CompraItem;
import com.gigti.xfinance.backend.data.VentaItem;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import org.vaadin.klaudeta.PaginatedGrid;

import java.math.BigDecimal;

public class VentaItemGrid extends PaginatedGrid<VentaItem> {

    public VentaItemGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT);

        addColumn(ci -> ci.getProducto().getNombreProducto())
                .setHeader("Producto")
                .setKey("name");

        addColumn(ci -> ci.getProducto().getTipoMedida().getSimbolo())
                .setHeader("Medida")
                .setKey("med");

        addColumn(VentaItem::getCantidad)
                .setHeader("Cantidad")
                .setKey("cantidad").setTextAlign(ColumnTextAlign.CENTER);
                //.setFlexGrow(10);

        addColumn(ci -> ci.getPrecioVenta() != null ? AllUtils.numberFormat(ci.getPrecioVenta()) : BigDecimal.ZERO)
                .setHeader("P. Venta Un")
                .setKey("ventaU").setTextAlign(ColumnTextAlign.END);

        addColumn(ci -> ci.getPrecioVenta() != null ? AllUtils.numberFormat(ci.getPrecioVenta().multiply(ci.getCantidad())) : BigDecimal.ZERO)
                .setHeader("P. Venta Total")
                .setKey("ventaT").setTextAlign(ColumnTextAlign.END);

        this.setColumnReorderingAllowed(true);
        this.setPageSize(Constantes.PAGE_SIZE_10);
        this.setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
