/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.DecimalFormat;
import java.util.Comparator;

public class ProductoGrid extends Grid<Producto> {

    public ProductoGrid() {
        setSizeFull();

        addColumn(Producto::getNombreProducto)
                .setHeader("Nombre")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(Producto::getCodigoBarra)
                .setHeader("Codigo Barras")
                .setFlexGrow(20)
                .setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availability]]\"></iron-icon> [[item.availability]]";
        addColumn(TemplateRenderer.<Producto>of(availabilityTemplate)
                .withProperty("activo", Producto::getActivo))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(this::formatActivo))
                .setSortable(true)
                .setFlexGrow(5);



        addColumn(this::formatStock)
                .setHeader("Cantidad")
                .setFlexGrow(3);

        addColumn(this::formatPrice)
                .setHeader("Cantidad")
                .setFlexGrow(3);

        // Show all categories the product is in, separated by commas
        addColumn(this::formatCategories)
                .setHeader("Category")
                .setFlexGrow(12);
    }

    public Producto getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Producto product) {
        getDataCommunicator().refresh(product);
    }

    private String formatCategories(Producto producto) {
//        if (producto.getCategoria() == null || producto.getCategoria().getNombre().isEmpty()) {
//            return "";
//        }
//
//        return producto.getCategoria().getNombre();
        return "";
    }

    private String formatActivo(Producto producto) {
        if (producto.getActivo()) {
            return "Activo";
        }else{
            return "Inactivo";
        }
    }

    private String formatPrice(Producto producto){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);


        return "$ 0.0";
    }

    private String formatStock(Producto producto){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);


        return "0";
    }

}
