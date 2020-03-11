/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Comparator;

public class UsuarioGrid extends PaginatedGrid<UsuarioDTO> {

    public UsuarioGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(UsuarioDTO::getNombreUsuario)
                .setHeader("Usuario")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(UsuarioDTO::getTipoIde)
                .setHeader("Tipo Ide")
                .setSortable(true);

        addColumn(UsuarioDTO::getIdentificacion)
                .setHeader("Identificación")
                .setSortable(true);

        addColumn(UsuarioDTO::getPrimerNombre)
                .setHeader("Primer Nombre")
                .setSortable(true);

        addColumn(UsuarioDTO::getPrimerApellido)
                .setHeader("Primer Apellido")
                .setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<UsuarioDTO>of(availabilityTemplate)
                .withProperty("activoS", UsuarioDTO::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(UsuarioDTO::getActivoS))
                .setSortable(true);

        addColumn(UsuarioDTO::getTelefono)
                .setHeader("Telefono")
                .setSortable(false);

        addColumn(UsuarioDTO::getEmail)
                .setHeader("Email")
                .setSortable(false);

        // Show all categories the product is in, separated by commas
        addColumn(usuario ->
            usuario.getRol().getNombre()
        ).setHeader("Rol").setSortable(true);

        setPageSize(Constantes.PAGE_SIZE_10);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(UsuarioDTO usuario) {
        getDataCommunicator().refresh(usuario);
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
