/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Comparator;

public class UsuarioGrid extends PaginatedGrid<Usuario> {

    public UsuarioGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(Usuario::getNombreUsuario)
                .setHeader("Usuario")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(usuario -> {
            Persona persona = usuario.getPersona();
            return persona == null ? "-" : persona.getTipoIde().getNombre();
        }).setHeader("Tipo Ide").setSortable(true);

        addColumn(usuario -> {
            Persona persona = usuario.getPersona();
            return persona == null ? "-" : persona.getIdentificacion();
        }).setHeader("Identificación").setSortable(true);

        addColumn(usuario -> {
            Persona persona = usuario.getPersona();
            return persona == null ? "-" : persona.getPrimerNombre();
        }).setHeader("Primer Nombre").setSortable(true);

        addColumn(usuario -> {
            Persona persona = usuario.getPersona();
            return persona == null ? "-" : persona.getPrimerApellido();
        }).setHeader("Primer Apellido").setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Usuario>of(availabilityTemplate)
                .withProperty("activoS", Usuario::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Usuario::getActivoS))
                .setSortable(true);

        addColumn(usuario -> {
            Persona persona = usuario.getPersona();
            return persona == null ? "-" : persona.getTelefono();
        }).setHeader("Telefono").setSortable(false);

        addColumn(usuario -> {
            Persona persona = usuario.getPersona();
            return persona == null ? "-" : persona.getEmail();
        }).setHeader("Email").setSortable(true);

        // Show all categories the product is in, separated by commas
        addColumn(this::formatRol)
                .setHeader("Rol")
                .setSortable(true);

        setPageSize(Constantes.PAGE_SIZE);
        setPaginatorSize(Constantes.PAGINATOR_SIZE);
        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(Usuario usuario) {
        getDataCommunicator().refresh(usuario);
    }

    private String formatRol(Usuario usuario) {
        if (usuario.getRol() == null || usuario.getRol().getNombre().isEmpty()) {
            return "";
        }
        return usuario.getRol().getNombre();
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
