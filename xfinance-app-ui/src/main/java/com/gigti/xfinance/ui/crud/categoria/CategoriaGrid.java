package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Comparator;

public class CategoriaGrid extends PaginatedGrid<CategoriaProducto> {

    public CategoriaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);

        addColumn(CategoriaProducto::getNombre)
                .setHeader("Nombre")
                .setSortable(true);

        addColumn(CategoriaProducto::getDescripcion)
                .setHeader("Descripción")
                .setSortable(true);

        final String activoTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<CategoriaProducto>of(activoTemplate)
                .withProperty("activoS", CategoriaProducto::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(CategoriaProducto::getActivoS))
                .setSortable(true);

        addColumn(categoria -> {
            Empresa empresa = categoria.getEmpresa();
            return empresa == null ? "-" : empresa.getNombreEmpresa();
        }).setHeader("Empresa");

        setPageSize(Constantes.PAGE_SIZE_10);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(CategoriaProducto categoria) {
        getDataCommunicator().refresh(categoria);
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }

}
