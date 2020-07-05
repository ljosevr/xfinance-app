package com.gigti.xfinance.ui.crud.proveedor;

import com.gigti.xfinance.backend.data.Proveedor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.Comparator;

public class ProveedorGrid extends Grid<Proveedor> {

    public ProveedorGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(Proveedor::getTipoIde)
                .setHeader("Tipo Identificacón")
                .setSortable(true);

        addColumn(Proveedor::getIdentificacion)
                .setHeader("Identificación")
                .setSortable(true);

        addColumn(Proveedor::getNombre)
                .setHeader("Nombre")
                .setSortable(true);

        addColumn(Proveedor::getEmail)
                .setHeader("Email");

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Proveedor>of(availabilityTemplate)
                .withProperty("activoS", Proveedor::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Proveedor::getActivoS))
                .setSortable(true);

        getColumns().forEach(column -> column.setAutoWidth(true));
        this.setColumnReorderingAllowed(true);
    }
}
