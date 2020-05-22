/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Comparator;

public class ProductoGrid extends PaginatedGrid<Producto> {

    public ProductoGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(Producto::getNombreProducto)
                .setHeader("Nombre")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(producto -> AllUtils.formatUnidadMedida(producto.getTipoMedida().name()))
                .setHeader("Medida")
                .setSortable(true)
                .setFlexGrow(5);

        addColumn(Producto::getCodigoBarra)
                .setHeader("Codigo Barras")
                .setFlexGrow(15)
                .setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Producto>of(availabilityTemplate)
                .withProperty("activoS", Producto::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Producto::getActivoS))
                .setSortable(true)
                .setFlexGrow(3);

//        addColumn(item -> {
//            Icon icon;
//            if(item.isActivo()){
//                icon = VaadinIcon.CHECK_CIRCLE.create();
//                icon.setColor("green");
//            } else {
//                icon = VaadinIcon.CLOSE_CIRCLE.create();
//                icon.setColor("red");
//            }
//            return icon;
//        })      .setHeader("Activo")
//                .setComparator(Comparator.comparing(Producto::isActivo))
//                .setSortable(true)
//                .setFlexGrow(5);

        // Show all categories the product is in, separated by commas
        addColumn(this::formatCategories)
                .setHeader("Categoria")
                .setSortable(true)
                .setFlexGrow(8);

        addColumn(producto -> producto.getImpuesto().getNombre())
                .setHeader("Impuesto")
                .setSortable(true);

        setPageSize(Constantes.PAGE_SIZE_10);
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

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
