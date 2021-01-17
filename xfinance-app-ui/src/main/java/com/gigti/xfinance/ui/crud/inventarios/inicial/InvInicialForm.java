/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.inventarios.inicial;

import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
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
public class InvInicialForm extends Dialog {

    private final BigDecimalField tfCantidad;
    private final H2 titleForm;
    private final ComboBox<Impuesto> cbImpuesto;
    private Button btnSave;
    private final Binder<InventarioInicial> binder;
    private final List<Impuesto> listImpuestos;
    private final Checkbox chkDefinitivo;
    private final Checkbox chkManageStock;
    private final BigDecimalField tfPrecioCosto;

    public InvInicialForm(List<Impuesto> listImpuestos) {
        this.listImpuestos = listImpuestos;

        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyListForm3Columns());

        titleForm = new H2("");
        titleForm.addClassName("titleView");

        TextField tfProducto = new TextField("Producto");
        tfProducto.setRequired(true);
        tfProducto.setReadOnly(true);
        tfProducto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfCantidad = new BigDecimalField("Cantidad");
        tfCantidad.focus();
        tfCantidad.setAutoselect(true);
        tfCantidad.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        chkManageStock = new Checkbox("Controlar Stock(Inventario)");
        chkManageStock.setRequiredIndicatorVisible(true);

        tfPrecioCosto = new BigDecimalField("Precio de Costo");
        tfPrecioCosto.setPrefixComponent(new Span("$"));
        tfPrecioCosto.setAutoselect(true);
        tfPrecioCosto.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfPrecioCosto.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));

        BigDecimalField tfPrecioVenta = new BigDecimalField("Precio de Venta");
        tfPrecioVenta.setPrefixComponent(new Span("$"));
        tfPrecioVenta.setAutoselect(true);
        tfPrecioVenta.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfPrecioVenta.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));

        chkManageStock.addValueChangeListener(event -> {
            if(event.getValue()){
                tfCantidad.setEnabled(true);
                tfCantidad.focus();

            } else {

                tfCantidad.setValue(BigDecimal.ZERO);
                tfCantidad.setEnabled(false);
                tfPrecioCosto.focus();
            }
        });

        cbImpuesto = new ComboBox<>();
        cbImpuesto.setLabel("Impuesto");
        cbImpuesto.setItems(listImpuestos);
        cbImpuesto.setRequired(true);
        cbImpuesto.setItemLabelGenerator(Impuesto::getNombre);

        chkDefinitivo = new Checkbox("Es Definitivo");
        chkDefinitivo.setValue(false);
        chkDefinitivo.setRequiredIndicatorVisible(true);

        binder = new BeanValidationBinder<>(InventarioInicial.class);
        binder.forField(tfProducto)
                .bind(inv -> inv.getProducto().getNombreProducto()
                , (inv, data) -> inv.setProducto(binder.getBean().getProducto()));

        binder.forField(tfCantidad).asRequired("Digite Cantidad").bind(InventarioInicial::getCantidad, InventarioInicial::setCantidad);

        binder.forField(tfPrecioCosto).asRequired("Digite Precio de Costo")
                .bind(InventarioInicial::getPrecioCosto
                , InventarioInicial::setPrecioCosto);
        binder.forField(tfPrecioVenta).asRequired("Digite Precio de Venta")
                .bind(InventarioInicial::getPrecioVenta
                , InventarioInicial::setPrecioVenta);

        binder.forField(chkManageStock).bind(InventarioInicial::isManageStock,InventarioInicial::setManageStock);
        binder.forField(cbImpuesto)
                .asRequired("Seleccione un Impuesto")
                .bind(InventarioInicial::getImpuesto, InventarioInicial::setImpuesto);
        binder.forField(chkDefinitivo).bind(InventarioInicial::isDefinitivo,InventarioInicial::setDefinitivo);

        binder.addStatusChangeListener(event -> btnSave.setEnabled(binder.isValid()));

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

        content.add(titleForm, tfProducto, chkManageStock, tfCantidad, tfPrecioCosto,
                tfPrecioVenta, cbImpuesto, chkDefinitivo, actionsLayout);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setInventario(InventarioInicial inventarioInicial, String title, String type)  {
        binder.setBean(inventarioInicial);
        if(type.equals(ICrudView.OPTION_ADD)) {
            chkManageStock.setVisible(true);
            if(!listImpuestos.isEmpty()){
                cbImpuesto.setValue(listImpuestos.get(0));
            }
        }
        if(type.equals(ICrudView.OPTION_EDIT)) {
            if(inventarioInicial != null) {
                if(inventarioInicial.isManageStock()) {
                    chkManageStock.setValue(true);
                    tfCantidad.setEnabled(true);
                    tfCantidad.focus();
                } else {
                    chkManageStock.setValue(false);
                    tfCantidad.setEnabled(false);
                    tfPrecioCosto.focus();
                }
            } else {
                chkManageStock.setValue(false);
                chkDefinitivo.setValue(false);
            }
        }

        titleForm.setText(title);
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
        private final InventarioInicial inventario;

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
