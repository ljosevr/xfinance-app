package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;

public class PventaGrid extends Grid<PventaDTO> {

    private BigDecimal total;
    private BigDecimal subTotal;

    public PventaGrid() {
        //setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);

        addColumn(PventaDTO::getItem)
                .setFlexGrow(1)
                .setVisible(true);

        addColumn(PventaDTO::getNombreProducto)
                .setHeader("Producto")
                .setFlexGrow(10)
                .setSortable(true)
                .setTextAlign(ColumnTextAlign.START);

        addColumn(PventaDTO::getCantidadVenta)
                .setHeader("Cantidad")
                .setFlexGrow(3)
                .setTextAlign(ColumnTextAlign.CENTER);

        addColumn(PventaDTO::getUnidadMedida)
                .setHeader("Medida")
                .setFlexGrow(3)
                .setTextAlign(ColumnTextAlign.CENTER);

        total = BigDecimal.ZERO;

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
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setCurrency(Currency.getInstance(Locale.US));
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(0);

        subTotal = pventaDTO.getPrecioVentaActual().multiply(BigDecimal.valueOf(pventaDTO.getCantidadVenta()));

        return "$ "+decimalFormat.format(subTotal);
    }

}
