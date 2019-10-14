package com.gigti.xfinance.ui.crud.Empresa;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Comparator;

public class EmpresaGrid extends PaginatedGrid<Empresa> {

    public EmpresaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(Empresa::getNombreEmpresa)
                .setHeader("Nombre")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(Empresa::getTipoIde)
                .setHeader("Tipo Iden")
                .setFlexGrow(5)
                .setSortable(true);

        addColumn(Empresa::getIdentificacion)
                .setHeader("Identificación")
                .setFlexGrow(10);

        addColumn(Empresa::getDireccion)
                .setHeader("Dirección")
                .setFlexGrow(5);

        addColumn(Empresa::getTelefono)
                .setHeader("Telefono")
                .setFlexGrow(5);

        addColumn(Empresa::getFechaActivacion)
                .setHeader("Fecha Activo")
                .setFlexGrow(8)
                .setSortable(true);

        addColumn(Empresa::getFechaDesactivacion)
                .setHeader("Fecha Inactivo")
                .setFlexGrow(8)
                .setSortable(true);

        String activoTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<CategoriaProducto>of(activoTemplate)
                .withProperty("activoS", Empresa::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Empresa::getActivoS))
                .setSortable(true)
                .setFlexGrow(5);

        setPageSize(Constantes.PAGE_SIZE);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(Empresa empresa) {
        getDataCommunicator().refresh(empresa);
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }

}
