/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.inicial;

import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.GridVariant;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public class InvInicialGrid extends PaginatedGrid<InventarioInicial> {

    public InvInicialGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(producto -> producto.getProducto().getNombreProducto())
                .setHeader("Nombre Producto")
                .setSortable(true);

        addColumn(producto -> AllUtils.formatUnidadMedida(producto.getProducto().getTipoMedida().name()))
                .setHeader("Medida")
                .setFlexGrow(2)
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(InventarioInicial::getCantidad)
                .setHeader("Cantidad")
                .setKey("cantidad");

        addColumn(inv -> inv.isInfinite() ? "SI" : "NO")
            .setHeader("Infinito")
            .setKey("infinite")
        .setSortable(true);

        addColumn(inventario -> {
            final DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setCurrency(Currency.getInstance(Locale.US));
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(0);

            return "$ "+decimalFormat.format(inventario.getPrecioCosto());
        }).setHeader("Precio Costo")
        .setKey("pcosto");

        addColumn(inventario -> {
            final DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setCurrency(Currency.getInstance(Locale.US));
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(0);

            return "$ "+decimalFormat.format(inventario.getPrecioVenta());
        }).setHeader("Precio Venta")
        .setKey("pventa");

        addColumn(inv -> StringUtils.isBlank(inv.getId()) ? "NO" : "SI")
                .setHeader("Definitivo")
                .setSortable(true);

        setPageSize(Constantes.PAGE_SIZE_10);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(InventarioInicial product) {
        getDataCommunicator().refresh(product);
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
