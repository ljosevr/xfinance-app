package com.gigti.xfinance.ui.crud.cliente;

import com.gigti.xfinance.backend.data.Cliente;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class ClienteGrid extends Grid<Cliente> {

    public ClienteGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(c -> c.getPersona().getTipoIde())
                .setHeader("Tipo Identificacón")
                .setSortable(true);

        addColumn(c -> c.getPersona().getIdentificacion())
                .setHeader("Identificación")
                .setSortable(true);

        addColumn(c -> c.getPersona().getPrimerNombre())
                .setHeader("Nombre")
                .setSortable(true);

        addColumn(c -> c.getPersona().getPrimerApellido())
                .setHeader("Apellido")
                .setSortable(true);

        addColumn(Cliente::getEmail)
                .setHeader("Email");


        getColumns().forEach(column -> column.setAutoWidth(true));
        this.setColumnReorderingAllowed(true);
    }
}
