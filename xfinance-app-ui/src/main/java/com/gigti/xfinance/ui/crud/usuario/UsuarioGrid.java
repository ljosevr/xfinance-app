/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

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

        addColumn(this::getTipoIde)
                .setHeader("Tipo Ide")
                .setFlexGrow(5)
                .setSortable(true);

        addColumn(this::getIdentificacion)
                .setHeader("Identificacion")
                .setFlexGrow(11)
                .setSortable(true);


        addColumn(this::getPrimerNombre)
                .setHeader("Primer Nombre")
                .setFlexGrow(10)
                .setSortable(true);


        addColumn(this::getPrimerApellido)
                .setHeader("Primer Apellido")
                .setFlexGrow(10)
                .setSortable(true);

        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.activoS]]\"></iron-icon> [[item.activoS]]";
        addColumn(TemplateRenderer.<Usuario>of(availabilityTemplate)
                .withProperty("activoS", Usuario::getActivoS))
                .setHeader("Activo")
                .setComparator(Comparator.comparing(Usuario::getActivoS))
                .setSortable(true)
                .setFlexGrow(5);

        addColumn(this::getTelefono)
                .setHeader("Cantidad")
                .setSortable(true)
                .setFlexGrow(3);

        addColumn(this::getEmail)
                .setHeader("Email")
                .setFlexGrow(10);

        // Show all categories the product is in, separated by commas
        addColumn(this::formatRol)
                .setHeader("Rol")
                .setSortable(true)
                .setFlexGrow(5);

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

    private String getTipoIde(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getTipoIde().getNombre();
        }
    }

    private String getIdentificacion(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getIdentificacion();
        }
    }

    private String getPrimerNombre(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getPrimerNombre();
        }
    }

    private String getSegundoNombre(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getSegundoNombre();
        }
    }

    private String getPrimerApellido(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getPrimerApellido();
        }
    }

    private String getSegundoApellido(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getSegundoApellido();
        }
    }

    private String getDireccion(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getDireccion();
        }
    }

    private String getTelefono(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getTelefono();
        }
    }

    private String getEmail(Usuario usuario){
        if (usuario.getPersona() == null) {
            return "";
        } else {
            return usuario.getPersona().getEmail();
        }
    }

    @Override
    public int getPage() {
        return super.getPage()-1;
    }
}
