/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.compra;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.others.AllUtils;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.math.BigDecimal;

public class CompraGrid extends Grid<Compra> {

    public CompraGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        addColumn(Compra::getNumeroFactura)
                .setHeader("#Numero de Factura");

        addColumn(compra -> compra.getProveedor().getNombre())
                .setHeader("Proveedor");

        addColumn(compra -> AllUtils.formatDate(compra.getFechaCompra()))
                .setHeader("Fecha Compra");

        addColumn(compra -> AllUtils.formatDate(compra.getFechaCreacion()))
                .setHeader("Fecha Creación");

        addColumn(compra -> AllUtils.numberFormat(compra.getTotalFactura() != null ? compra.getTotalFactura() : BigDecimal.ZERO))
                .setHeader("Total Factura");

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
