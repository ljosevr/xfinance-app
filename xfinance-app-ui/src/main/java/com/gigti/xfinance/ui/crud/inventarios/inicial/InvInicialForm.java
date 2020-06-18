/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.inicial;

import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.math.BigDecimal;
import java.util.List;

/**
 * A form for editing a single Usuario Admin.
 */
public class InvInicialForm extends FormLayout {

    private final NumberField tfCantidad;
    private Button btnSave;
    private Binder<InventarioInicial> binder;

    public InvInicialForm(List<Impuesto> listImpuestos) {
        binder = new BeanValidationBinder<>(InventarioInicial.class);
        this.addClassName("form-inv");

        H2 title = new H2("Crear Inventario Inicial");
        title.addClassName("titleView");
        this.add(title,3);

        TextField tfProducto = new TextField("Producto");
        tfProducto.setRequired(true);
        tfProducto.setReadOnly(true);
        tfProducto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfCantidad = new NumberField("Cantidad");
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
        tfPrecioCosto.setPrefixComponent(new Span("$"));
        tfPrecioCosto.setAutoselect(true);
        tfPrecioCosto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        NumberField tfPrecioVenta = new NumberField("Precio de Venta");
        tfPrecioVenta.setPrefixComponent(new Span("$"));
        tfPrecioVenta.setAutoselect(true);
        tfPrecioVenta.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        ComboBox<Impuesto> cbImpuesto = new ComboBox<>();
        cbImpuesto.setLabel("Impuesto");
        cbImpuesto.setItems(listImpuestos);
        cbImpuesto.setRequired(true);
        cbImpuesto.setItemLabelGenerator(Impuesto::getNombre);

        binder.forField(tfProducto)
                .bind(inv -> inv.getProducto().getNombreProducto()
                , (inv, data) -> inv.setProducto(binder.getBean().getProducto()));
        binder.forField(tfCantidad).asRequired("Digite Cantidad")
                .bind(inv -> inv.getCantidad().doubleValue()
                        , (inv, data) -> inv.setCantidad(BigDecimal.valueOf(data)));
        binder.forField(tfPrecioCosto).asRequired("Digite Precio de Costo")
                .bind(inv -> inv.getPrecioCosto().doubleValue()
                , (inv, data) -> {
                    inv.setPrecioCosto(BigDecimal.valueOf(data));
                });
        binder.forField(tfPrecioVenta).asRequired("Digite Precio de Venta")
                .bind(inv -> inv.getPrecioVenta().doubleValue()
                , (inv, data) -> {
                    inv.setPrecioVenta(BigDecimal.valueOf(data));
                });

        binder.forField(chkInfinite).bind("infinite");
        binder.forField(cbImpuesto)
                .asRequired("Seleccione un Impuesto")
                .bind(InventarioInicial::getImpuesto, InventarioInicial::setImpuesto);

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

        this.add(tfProducto, tfCantidad, chkInfinite, tfPrecioCosto, tfPrecioVenta, cbImpuesto, actionsLayout);
    }

    public void setInventario(InventarioInicial inventarioInicial) {
        binder.setBean(inventarioInicial);
        tfCantidad.focus();
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        } else {
            NotificacionesUtil.showError("Validar Inventario: "+binder.validate().getValidationErrors());
        }
    }

    // Events
    public static abstract class InventarioFormEvent extends ComponentEvent<InvInicialForm> {
        private InventarioInicial inventario;

        protected InventarioFormEvent(InvInicialForm source, InventarioInicial inventario) {
            super(source, false);
            this.inventario = inventario;
        }

        public InventarioInicial getInventario() {
            return inventario;
        }
    }

    public static class SaveEvent extends InventarioFormEvent {
        SaveEvent(InvInicialForm source, InventarioInicial inventario) {
            super(source, inventario);
        }
    }

    public static class CloseEvent extends InventarioFormEvent {
        CloseEvent(InvInicialForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
