/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.Comparator;

public class ProductoGrid extends Grid<Producto> {

    public ProductoGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        addColumn(Producto::getNombreProducto)
                .setHeader("Nombre")
                .setSortable(true);

        addColumn(producto -> producto.getTipoMedida().getSimbolo())
                .setHeader("Medida")
                .setSortable(true);

        addColumn(Producto::getCodigoBarra)
                .setHeader("Codigo Barras")
                .setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Producto>of(availabilityTemplate)
                .withProperty("activoS", Producto::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Producto::getActivoS))
                .setSortable(true);

        addColumn(this::formatCategories)
                .setHeader("Categoria")
                .setSortable(true);

        addColumn(producto -> producto.getImpuesto().getNombre())
                .setHeader("Impuesto")
                .setSortable(true);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private String formatCategories(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getNombre().isEmpty()) {
            return "";
        }

        return producto.getCategoria().getNombre();
    }

}
