package com.gigti.xfinance.ui.crud.gasto;

import com.gigti.xfinance.backend.data.Gasto;
import com.gigti.xfinance.backend.others.AllUtils;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.math.BigDecimal;

public class GastoGrid extends Grid<Gasto> {

    public GastoGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        addColumn(Gasto::getDescripcion)
                .setHeader("Gasto")
                .setSortable(false);

        addColumn(gasto -> AllUtils.formatDate(gasto.getFecha()))
                .setHeader("Fecha")
                .setSortable(true);

        addColumn(gasto -> AllUtils.numberFormat(gasto.getValor() != null ? gasto.getValor() : BigDecimal.ZERO))
                .setHeader("Valor");

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
