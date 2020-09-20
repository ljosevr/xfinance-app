/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.reportes.ventas;

import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.math.BigDecimal;

public class VentaGrid extends Grid<Venta> {

    public VentaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        addColumn(Venta::getNumeroFactura)
                .setHeader("#Numero");

        //TODO Cliente
//        addColumn(Venta::getDescuentoFactura)
//                .setHeader("Descuento");

        addColumn(venta -> AllUtils.formatDate(venta.getFechaCreacion()))
                .setHeader("Fecha");

        addColumn(venta -> venta.getItems().size())
                .setHeader("# Items");

        addColumn(venta -> venta.getDescuentoFactura() != null ? AllUtils.numberFormat(venta.getDescuentoFactura()) : BigDecimal.ZERO)
                .setHeader("Descuento");

        addColumn(venta -> venta.getTotalCosto() != null ? AllUtils.numberFormat(venta.getTotalCosto()) : BigDecimal.ZERO)
                .setHeader("Total Costo");

        addColumn(venta -> venta.getTotalVenta() != null ? AllUtils.numberFormat(venta.getTotalVenta()) : BigDecimal.ZERO)
                .setHeader("Total Venta");


        getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
