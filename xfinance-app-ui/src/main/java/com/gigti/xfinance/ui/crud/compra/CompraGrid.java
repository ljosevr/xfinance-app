/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.compra;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.GridVariant;
import org.vaadin.klaudeta.PaginatedGrid;

public class CompraGrid extends PaginatedGrid<Compra> {

    public CompraGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(Compra::getNumeroFactura)
                .setHeader("#Numero de Factura")
                .setFlexGrow(10);

        addColumn(compra -> compra.getProveedor().getNombre())
                .setHeader("Proveedor")
                .setFlexGrow(10);

        addColumn(compra -> AllUtils.formatDate(compra.getFechaCompra()))
                .setHeader("Fecha Compra")
                .setFlexGrow(8);

        addColumn(compra -> AllUtils.formatDate(compra.getFechaCreacion()))
                .setHeader("Fecha Creación")
                .setFlexGrow(8);

        addColumn(compra -> AllUtils.numberFormat(compra.getTotalFactura()))
                .setHeader("Total Factura")
                .setFlexGrow(8);

        setPageSize(Constantes.PAGE_SIZE_10);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(Compra compra) {
        getDataCommunicator().refresh(compra);
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
