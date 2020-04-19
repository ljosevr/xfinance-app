/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.compra;

import com.gigti.xfinance.backend.data.CompraItem;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.GridVariant;
import org.vaadin.klaudeta.PaginatedGrid;

import java.math.BigDecimal;

public class CompraItemGrid extends PaginatedGrid<CompraItem> {

    public CompraItemGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(ci -> ci.getProducto().getNombreProducto())
                .setHeader("Producto")
                .setKey("name");
                //.setFlexGrow(10);

        addColumn(CompraItem::getCantidad)
                .setHeader("Cantidad")
                .setKey("cantidad");
                //.setFlexGrow(10);

        addColumn(ci -> ci.getPrecioTotalCosto() != null ? AllUtils.numberFormat(ci.getPrecioTotalCosto()) : BigDecimal.ZERO)
                .setHeader("P. Costo Total")
                .setKey("costoT");
                //.setFlexGrow(8);

        addColumn(ci -> ci.getPrecioTotalCosto() != null ? AllUtils.numberFormat(ci.getPrecioTotalCosto().divide(ci.getCantidad())) : BigDecimal.ZERO)
                .setHeader("P. Costo Un")
                .setKey("costoU");

        addColumn(ci -> ci.getPrecioVenta() != null ? AllUtils.numberFormat(ci.getPrecioVenta()) : BigDecimal.ZERO)
                .setHeader("P. Venta Un")
                .setKey("ventaU");

        setPageSize(Constantes.PAGE_SIZE_10);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}