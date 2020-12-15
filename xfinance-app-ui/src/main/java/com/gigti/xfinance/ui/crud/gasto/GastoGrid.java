package com.gigti.xfinance.ui.crud.gasto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Gasto;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.math.BigDecimal;
import java.util.Comparator;

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
