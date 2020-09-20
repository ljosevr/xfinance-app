package com.gigti.xfinance.ui.crud.reportes.ventas;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.shared.Registration;

import java.time.ZoneId;
import java.util.*;

@PreserveOnRefresh
public class VentaDetailForm extends Dialog {

    private final H2 titleForm;
    private final TextField tfFactura;
    private final DatePicker dpFecha;
    private final BigDecimalField bdFDescuento;
    private final BigDecimalField bdfVentaTotal;
    private final VentaItemGrid itemsGrid;
    private final BigDecimalField bdfCostoTotal;

    public VentaDetailForm() {
        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        titleForm = new H2("");
        titleForm.addClassName("titleView");
        this.add(titleForm);

        tfFactura = new TextField("# Factura");
        tfFactura.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfFactura.setReadOnly(true);

        dpFecha = new DatePicker("Fecha");
        dpFecha.setReadOnly(true);

        bdFDescuento = new BigDecimalField("Descuento");
        bdFDescuento.setReadOnly(true);

        bdfCostoTotal = new BigDecimalField("Costo Total");
        bdfCostoTotal.setReadOnly(true);

        bdfVentaTotal = new BigDecimalField("Venta Total");
        bdfVentaTotal.setReadOnly(true);

        VerticalLayout vlGrid = new VerticalLayout();
        itemsGrid = new VentaItemGrid();
        itemsGrid.setSizeFull();
        vlGrid.add(itemsGrid);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new VentaDetailForm.CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnClose);

        content.add(titleForm, tfFactura, dpFecha, bdFDescuento, bdfCostoTotal, bdfVentaTotal, vlGrid, actionsLayout);

        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(vlGrid, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);

    }

    public void setVenta(Venta venta, String title) {

        titleForm.setText(title);

        if(venta != null && venta.getItems() != null) {
            List<VentaItem> listaItems = venta.getItems();
            listaItems.forEach(item ->
                    item.setPrecioTotalCosto(item.getPrecioCosto().multiply(item.getCantidad())));
            itemsGrid.setItems(listaItems);
            tfFactura.setValue(venta.getNumeroFactura());
            dpFecha.setValue(venta.getFechaCreacion().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());

            bdFDescuento.setValue(venta.getDescuentoFactura());
            bdfCostoTotal.setValue(venta.getTotalCosto());
            bdfVentaTotal.setValue(venta.getTotalVenta());

        }
    }

    // Events
    public static abstract class VentaFormEvent extends ComponentEvent<VentaDetailForm> {
        private Venta venta;

        VentaFormEvent(VentaDetailForm source, Venta venta) {
            super(source, false);
            this.venta = venta;
        }

        public Venta getVenta() {
            return venta;
        }
    }

    public static class CloseEvent extends VentaFormEvent {
        CloseEvent(VentaDetailForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
