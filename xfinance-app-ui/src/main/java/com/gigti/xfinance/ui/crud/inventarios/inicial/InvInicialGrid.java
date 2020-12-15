/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.inicial;

import com.gigti.xfinance.backend.data.InventarioInicial;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public class InvInicialGrid extends Grid<InventarioInicial> {

    public InvInicialGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        addColumn(producto -> producto.getProducto().getNombreProducto())
                .setHeader("Nombre Producto")
                .setSortable(true);

        addColumn(producto -> producto.getProducto().getTipoMedida().getSimbolo())
                .setHeader("Medida")
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(InventarioInicial::getCantidad)
                .setHeader("Cantidad")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setKey("cantidad");

        addColumn(inv -> inv.isManageStock() ? "SI" : "NO")
            .setHeader("Controla Stock")
            .setKey("infinite")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setSortable(true);

        addColumn(inventario -> {
            final DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setCurrency(Currency.getInstance(Locale.US));
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(0);

            return "$ "+decimalFormat.format(inventario.getPrecioCosto() != null ? inventario.getPrecioCosto() : BigDecimal.ZERO);
        }).setHeader("Precio Costo")
                .setTextAlign(ColumnTextAlign.END)
                .setKey("pcosto");

        addColumn(inventario -> {
            final DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setCurrency(Currency.getInstance(Locale.US));
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(0);

            return "$ "+decimalFormat.format(inventario.getPrecioVenta());
        }).setHeader("Precio Venta")
                .setTextAlign(ColumnTextAlign.END)
                .setKey("pventa");

        addColumn(inv -> inv.isDefinitivo() ? "SI" : "NO")
                .setHeader("Definitivo")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setSortable(true);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

}
