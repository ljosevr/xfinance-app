/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventario;

import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * A form for editing a single Usuario Admin.
 */
public class InventarioInicialForm extends Dialog {

    private static final Logger logger = Logger.getLogger(InventarioInicialForm.class.getName());
    private Button btnSave;

    private Binder<InventarioInicial> binder;
    public InventarioInicialForm() {
        binder = new BeanValidationBinder<>(InventarioInicial.class);

        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        H4 title = new H4("Crear Inventario Inicial");
        content.add(title,3);

        TextField tfProducto = new TextField("Producto");
        tfProducto.setRequired(true);
        tfProducto.setEnabled(false);
        tfProducto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        NumberField tfCantidad = new NumberField("Cantidad");
        //tfCantidad.setRequired(true);
        tfCantidad.focus();
        tfCantidad.setAutoselect(true);
        tfCantidad.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        Checkbox chkInfinite = new Checkbox("Stock Infinito");
        chkInfinite.setValue(false);
        chkInfinite.setRequiredIndicatorVisible(true);

        chkInfinite.addValueChangeListener(event -> {
           if(event.getValue()){
               tfCantidad.setValue(0d);
               tfCantidad.setEnabled(false);
           } else {
               tfCantidad.setEnabled(true);
           }
        });

        NumberField tfPrecioCosto = new NumberField("Precio de Costo");
        //tfPrecioCosto.setRequired(true);
        tfPrecioCosto.setPrefixComponent(new Span("$"));
        tfPrecioCosto.setAutoselect(true);
        tfPrecioCosto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        NumberField tfPrecioVenta = new NumberField("Precio de Venta");
        //tfPrecioVenta.setRequired(false);
        tfPrecioVenta.setPrefixComponent(new Span("$"));
        tfPrecioVenta.setAutoselect(true);
        tfPrecioVenta.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        binder.forField(tfProducto)
                .bind(inv -> {
                    return inv.getProducto().getNombreProducto();
                }, (inv, data) -> inv.setProducto(binder.getBean().getProducto()));
        binder.forField(tfCantidad).asRequired("Digite Cantidad")
                .bind(inv -> {
                    return inv.getCantidad().doubleValue();
                }, (inv, data) -> inv.setCantidad(BigDecimal.valueOf(data)));
        binder.forField(tfPrecioCosto).asRequired("Digite Precio de Costo")
                .bind(inv -> {
                    return inv.getPrecioCosto().doubleValue();
                }, (inv, data) -> {
                    inv.setPrecioCosto(BigDecimal.valueOf(data));
                });
        binder.forField(tfPrecioVenta).asRequired("Digite Precio de Venta")
                .bind(inv -> {
                    return inv.getPrecioVenta().doubleValue();
                }, (inv, data) -> {
                    inv.setPrecioVenta(BigDecimal.valueOf(data));
                });

        binder.forField(chkInfinite).bind("infinite");

        //binderPersona.bindInstanceFields(this);

         binder.addStatusChangeListener(event -> {
             btnSave.setEnabled(binder.isValid());
        });

        btnSave = new Button("Guardar");
        btnSave.setWidth("100%");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.setWidth("100%");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnClose);

        content.add(tfProducto, tfCantidad, chkInfinite, tfPrecioCosto, tfPrecioVenta, actionsLayout);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setInventario(InventarioInicial inventarioInicial) {
        binder.setBean(inventarioInicial);
    }

    private void validateAndSave() {

        if (binder.validate().isOk()) {
            this.close();
            NotificacionesUtil.openConfirmationDialog("Al Guardar No se podrá Modificar \n ¿Desea Confirmar?", true, false);

            NotificacionesUtil.getSiButton().addClickListener(event -> {
                Dialog dialog = NotificacionesUtil.getDialog();
                dialog.close();
                fireEvent(new SaveEvent(this, binder.getBean()));
            });
            NotificacionesUtil.getNoButton().addClickListener(event -> {
                NotificacionesUtil.getDialog().close();
                this.open();
            });
        } else {
            Notification.show("Validar Inventario: "+binder.validate().getValidationErrors(),3000, Notification.Position.TOP_CENTER);
        }



    }

    // Events
    public static abstract class InventarioFormEvent extends ComponentEvent<InventarioInicialForm> {
        private InventarioInicial inventario;

        protected InventarioFormEvent(InventarioInicialForm source, InventarioInicial inventario) {
            super(source, false);
            this.inventario = inventario;
        }

        public InventarioInicial getInventario() {
            return inventario;
        }
    }

    public static class SaveEvent extends InventarioFormEvent {
        SaveEvent(InventarioInicialForm source, InventarioInicial inventario) {
            super(source, inventario);
        }
    }

    public static class CloseEvent extends InventarioFormEvent {
        CloseEvent(InventarioInicialForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
