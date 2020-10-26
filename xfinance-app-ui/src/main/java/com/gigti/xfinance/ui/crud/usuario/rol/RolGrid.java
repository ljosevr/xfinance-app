package com.gigti.xfinance.ui.crud.usuario.rol;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Rol;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.Comparator;

public class RolGrid extends Grid<Rol> {

    public RolGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        addColumn(Rol::getNombre)
                .setHeader("Nombre")
                .setSortable(true);

        addColumn(Rol::getDescripcion)
                .setHeader("Descripción")
                .setSortable(true);

        final String activoTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Rol>of(activoTemplate)
                .withProperty("activoS", Rol::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Rol::getActivoS))
                .setSortable(true);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }
}
