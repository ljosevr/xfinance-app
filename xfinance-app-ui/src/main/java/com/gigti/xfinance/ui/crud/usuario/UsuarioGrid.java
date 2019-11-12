/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.text.DecimalFormat;
import java.util.Comparator;

public class UsuarioGrid extends PaginatedGrid<Usuario> {

    /*public UsuarioGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(Usuario::getNombreUsuario)
                .setHeader("Usuario")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(Usuario::getPer)
                .setHeader("Codigo Barras")
                .setFlexGrow(20)
                .setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Producto>of(availabilityTemplate)
                .withProperty("activoS", Producto::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Producto::getActivoS))
                .setSortable(true)
                .setFlexGrow(5);

        addColumn(this::formatStock)
                .setHeader("Cantidad")
                .setSortable(true)
                .setFlexGrow(3);

        addColumn(this::formatCostPrice)
                .setHeader("P. Costo")
                .setFlexGrow(5);

        addColumn(this::formatSellPrice)
                .setHeader("P. Venta")
                .setFlexGrow(5);

        // Show all categories the product is in, separated by commas
        addColumn(this::formatCategories)
                .setHeader("Categoria")
                .setSortable(true)
                .setFlexGrow(12);

        setPageSize(Constantes.PAGE_SIZE);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(Producto product) {
        getDataCommunicator().refresh(product);
    }

    private String formatCategories(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getNombre().isEmpty()) {
            return "";
        }

        return producto.getCategoria().getNombre();
    }

    private String formatCostPrice(Producto producto){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        return "$ "+decimalFormat.format(producto.getPrecioCostoActual());
    }

    private String formatSellPrice(Producto producto){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        return "$ "+decimalFormat.format(producto.getPrecioVentaActual());
    }

    private String formatStock(Producto producto){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        return decimalFormat.format(producto.getStockActual());
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }*/
}
