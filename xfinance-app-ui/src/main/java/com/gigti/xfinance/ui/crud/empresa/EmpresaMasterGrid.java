package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class EmpresaMasterGrid extends PaginatedGrid<EmpresaDTO> {

    public EmpresaMasterGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(EmpresaDTO::getNombreEmpresa)
                .setHeader("Nombre")
                .setSortable(true);

        addColumn(EmpresaDTO::getCodigoEmpresa)
                .setHeader("Código");

        addColumn(empresa -> {
            return empresa.getTipoIde() + " "+empresa.getIdentificacion();
        }).setHeader("Identificación");

        addColumn(EmpresaDTO::getDireccion)
                .setHeader("Dirección");

        addColumn(EmpresaDTO::getTelefono)
                .setHeader("Telefono");

        addColumn(this::formatDateActivation)
                .setHeader("Fecha Activo")
                .setSortable(true);

        addColumn(this::formatDateDesactivation)
                .setHeader("Fecha Inactivo")
                .setSortable(true);

        String activoTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<EmpresaDTO>of(activoTemplate)
                .withProperty("activoS", EmpresaDTO::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(EmpresaDTO::getActivoS))
                .setSortable(true);

        setPageSize(Constantes.PAGE_SIZE);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(EmpresaDTO empresa) {
        getDataCommunicator().refresh(empresa);
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }

    private String formatDateActivation(EmpresaDTO empresa){
        DateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        return empresa.getFechaActivacion() != null ? df.format(empresa.getFechaActivacion()) : "";
    }

    private String formatDateDesactivation(EmpresaDTO empresa){
        DateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        return empresa.getFechaDesactivacion() != null ? df.format(empresa.getFechaDesactivacion()) : "";
    }
}
