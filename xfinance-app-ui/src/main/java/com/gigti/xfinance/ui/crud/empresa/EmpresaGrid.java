package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.net.http.HttpRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;

public class EmpresaGrid extends PaginatedGrid<EmpresaDTO> {

    public EmpresaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(EmpresaDTO::getNombreEmpresa)
                .setHeader("Nombre")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(EmpresaDTO::getTipoIde)
                .setHeader("Tipo Iden")
                .setFlexGrow(5)
                .setSortable(true);

        addColumn(EmpresaDTO::getIdentificacion)
                .setHeader("Identificación")
                .setFlexGrow(10);

        addColumn(EmpresaDTO::getDireccion)
                .setHeader("Dirección")
                .setFlexGrow(5);

        addColumn(EmpresaDTO::getTelefono)
                .setHeader("Telefono")
                .setFlexGrow(5);

        addColumn(this::formatDateActivation)
                .setHeader("Fecha Activo")
                .setFlexGrow(8)
                .setSortable(true);

        addColumn(this::formatDateDesactivation)
                .setHeader("Fecha Inactivo")
                .setFlexGrow(8)
                .setSortable(true);

        String activoTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<EmpresaDTO>of(activoTemplate)
                .withProperty("activoS", EmpresaDTO::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(EmpresaDTO::getActivoS))
                .setSortable(true)
                .setFlexGrow(5);

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
