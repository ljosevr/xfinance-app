/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.compra;

import java.math.BigDecimal;

import com.gigti.xfinance.backend.data.CompraItem;
import com.gigti.xfinance.backend.others.AllUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class CompraItemGrid extends Grid<CompraItem> {

    public CompraItemGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT);

        addColumn(ci -> ci.getProducto().getNombreProducto())
                .setHeader("Producto")
                .setKey("name");
                //.setFlexGrow(10);

        addColumn(ci -> ci.getProducto().getTipoMedida().getSimbolo())
                .setHeader("Medida")
                .setKey("med");

        addColumn(CompraItem::getCantidad)
                .setHeader("Cantidad")
                .setKey("cantidad").setTextAlign(ColumnTextAlign.CENTER);
                //.setFlexGrow(10);

        addColumn(ci -> ci.getPrecioTotalCosto() != null ? AllUtils.numberFormat(ci.getPrecioTotalCosto()) : BigDecimal.ZERO)
                .setHeader("P. Costo Total")
                .setKey("costoT")
                .setTextAlign(ColumnTextAlign.END);
                //.setFlexGrow(8);

        addColumn(ci -> ci.getPrecioTotalCosto() != null ? AllUtils.numberFormat(ci.getPrecioCosto()) : BigDecimal.ZERO)
                .setHeader("P. Costo Un")
                .setKey("costoU")
                .setTextAlign(ColumnTextAlign.END);

        addColumn(ci -> ci.getPrecioVenta() != null ? AllUtils.numberFormat(ci.getPrecioVenta()) : BigDecimal.ZERO)
                .setHeader("P. Venta Un")
                .setKey("ventaU").setTextAlign(ColumnTextAlign.END);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }


}
