package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PventaGrid extends Grid<PventaDTO> {

    private BigDecimal total;
    private BigDecimal subTotal;

    public PventaGrid() {
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        addColumn(PventaDTO::getNombreProducto)
                .setHeader("Producto")
                .setFlexGrow(20)
                .setSortable(true);

        addColumn(PventaDTO::getCantidadVenta)
                .setHeader("Cantidad")
                .setFlexGrow(3);

        addColumn(this::formatPrice)
                .setHeader("SubTotal")
                .setFlexGrow(5)
                .setFooter("TOTAL: " + total);

        getColumns().forEach(column -> column.setAutoWidth(true));
    }

    public void refresh(PventaDTO pventaDTO) {
        getDataCommunicator().refresh(pventaDTO);
    }

    private String formatPrice(PventaDTO pventaDTO){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        subTotal = pventaDTO.getPrecioVentaActual().multiply(BigDecimal.valueOf(pventaDTO.getCantidadVenta()));

        return "$ "+pventaDTO.getPrecioVentaActual().multiply(BigDecimal.valueOf(pventaDTO.getCantidadVenta()));
    }

    private String getTotal(PventaDTO pventaDTO){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        //this.

        return "$ "+pventaDTO.getPrecioVentaActual().multiply(BigDecimal.valueOf(pventaDTO.getCantidadVenta()));
    }
}
