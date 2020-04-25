package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.ui.util.AllUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class PventaGrid extends Grid<PventaDTO> {

    public PventaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);

        addColumn(PventaDTO::getItem)
                .setFlexGrow(1)
                .setVisible(true);

        addColumn(PventaDTO::getNombreProducto)
                .setHeader("Producto")
                .setFlexGrow(15)
                .setSortable(true)
                .setTextAlign(ColumnTextAlign.START);

        addColumn(pv -> AllUtils.formatUnidadMedida(pv.getUnidadMedida()))
                .setHeader("Medida")
                .setFlexGrow(2)
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(PventaDTO::getCantidadVenta)
                .setHeader("Cantidad")
                .setFlexGrow(2)
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(this::formatImpuesto)
                .setHeader("Impuesto")
                .setFlexGrow(3)
                .setTextAlign(ColumnTextAlign.END);

        addColumn(this::formatPrice)
                .setHeader("SubTotal")
                .setFlexGrow(5)
                .setTextAlign(ColumnTextAlign.END);

        getColumns().forEach(column -> column.setAutoWidth(true));

    }

    public void refresh(PventaDTO pventaDTO) {
        getDataCommunicator().refresh(pventaDTO);
    }

    private String formatPrice(PventaDTO pventaDTO){
        return AllUtils.numberFormat(pventaDTO.getPrecioVentaActual().multiply(pventaDTO.getCantidadVenta()));
    }

    private String formatImpuesto(PventaDTO pventaDTO) {

        return AllUtils.numberFormat(AllUtils.percentage(pventaDTO.getPrecioVentaActual(), pventaDTO.getImpuestoValor()));
    }

}
