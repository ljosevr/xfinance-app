/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.TipoMedidaEnum;
import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Producto;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * A form for editing a single product.
 */
public class ProductoForm extends Dialog {
    private FormLayout content;

    Logger logger = LoggerFactory.getLogger(ProductoForm.class);
    private NumberField tfProdStock;
    private TextField tfPrecioVenta;
    private TextField tfPrecioCosto;
    private ComboBox<TipoMedidaEnum> cbTipoMedida;
    private ComboBox<CategoriaProducto>  cbCategorias;
    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private ProductoCrudLogic viewLogic;
    private Binder<Producto> binder;
    private Producto currentProduct;

    private static class PriceConverter extends StringToBigDecimalConverter {

        public PriceConverter() {
            super(BigDecimal.ZERO, "No se puede convertir el valor a Número.");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // Always display currency with two decimals
            NumberFormat format = super.getFormat(locale);
            if (format instanceof DecimalFormat) {
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);
            }
            return format;
        }
    }

    private static class StockCountConverter extends StringToIntegerConverter {

        public StockCountConverter() {
            super(0, "No se puede convertir el valor a " + Integer.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // Do not use a thousands separator, as HTML5 input type
            // number expects a fixed wire/DOM number format regardless
            // of how the browser presents it to the user (which could
            // depend on the browser locale).
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(true);
            format.setParseIntegerOnly(false);
            format.setGroupingUsed(false);
            return format;
        }
    }

    public ProductoForm(ProductoCrudLogic productoCrudLogic, List<CategoriaProducto> listCategoria, List<TipoMedidaEnum> listaTipoMedida) {
        content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Producto");
        content.add(title,3);

        viewLogic = productoCrudLogic;

        TextField tfProdNombre = new TextField("Nombre Producto");
        tfProdNombre.setRequired(true);
        tfProdNombre.focus();

        TextField tfProdCodigoB = new TextField("Codigo de barras");
        tfProdCodigoB.setRequired(false);

        TextField tfProdDescripcion = new TextField("Descripción");

        cbTipoMedida = new ComboBox<>();
        cbTipoMedida.setItems(listaTipoMedida);
        cbTipoMedida.setLabel("Tipo Medida");
        cbTipoMedida.setRequired(true);

        Checkbox chkActivo = new Checkbox("Activo");
        chkActivo.setValue(true);

        cbCategorias = new ComboBox<>();
        cbCategorias.setLabel("Categoria");
        cbCategorias.setItems(listCategoria);
        cbCategorias.setRequired(true);

        tfPrecioCosto = new TextField("Precio Costo");
        tfPrecioCosto.setPrefixComponent(new Span("$"));
        tfPrecioCosto.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfPrecioCosto.setValueChangeMode(ValueChangeMode.EAGER);
        tfPrecioCosto.setRequired(true);
        tfPrecioCosto.addFocusListener(focus -> {
            if(focus.isFromClient()){
                if(tfPrecioCosto.isEmpty() || tfPrecioCosto.getValue().equals("0,00")) {
                    tfPrecioCosto.clear();
                } else if(tfPrecioCosto.isEmpty()) {
                    tfPrecioCosto.setValue("0");
                }
            }
        });

        tfPrecioVenta = new TextField("Precio Venta");
        tfPrecioVenta.setPrefixComponent(new Span("$"));
        tfPrecioVenta.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfPrecioVenta.setValueChangeMode(ValueChangeMode.EAGER);
        tfPrecioVenta.setRequired(true);
        tfPrecioVenta.addFocusListener(focus -> {
            if(focus.isFromClient()){
                if(tfPrecioVenta.isEmpty() || tfPrecioVenta.getValue().equals("0,00")){
                    tfPrecioVenta.clear();
                } else if(tfPrecioVenta.isEmpty()) {
                    tfPrecioVenta.setValue("0");
                }
            }
        });

        tfProdStock = new NumberField("Cantidad Inicial");
        tfProdStock.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfProdStock.setValueChangeMode(ValueChangeMode.EAGER);
        tfProdStock.setRequiredIndicatorVisible(true);
        tfProdStock.addFocusListener(focus -> {
            if(focus.isFromClient()){
                if(tfProdStock.isEmpty() || tfProdStock.getValue() <= 0d){
                    tfProdStock.clear();
                }
            }
        });

        binder = new BeanValidationBinder<>(Producto.class);
        binder.forField(tfPrecioCosto).asRequired("Digite el Precio de Costo").withConverter(new PriceConverter()).bind(Producto::getPrecioCostoActual, Producto::setPrecioCostoActual);
        binder.forField(tfPrecioVenta).asRequired("Digite el Precio de Venta").withConverter(new PriceConverter()).bind(Producto::getPrecioVentaActual, Producto::setPrecioVentaActual);
        binder.forField(tfProdStock).asRequired("Digite Cantidad").bind(Producto::getStockActual, Producto::setStockActual);
        binder.forField(tfProdNombre).asRequired("Digite Nombre").bind(Producto::getNombreProducto, Producto::setNombreProducto);
        binder.forField(tfProdDescripcion).bind(Producto::getDescripcion, Producto::setDescripcion);
        binder.forField(tfProdCodigoB).asRequired("Digite el Codigo de Barras").bind(Producto::getCodigoBarra, Producto::setCodigoBarra);
        binder.forField(chkActivo).bind(Producto::isActivo, Producto::setActivo);
        binder.forField(cbCategorias).asRequired("Seleccione una Categoria").bind(Producto::getCategoria, Producto::setCategoria);
        binder.forField(cbTipoMedida).asRequired("Seleccione una Unidad de Medida").bind(Producto::getTipoMedida, Producto::setTipoMedida);

        binder.bindInstanceFields(this);

        // enable/disable btnSave button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
        });

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProducto(currentProduct);
            } else {
                Notification.show("Validar Datos del formulario",3000, Notification.Position.MIDDLE);
            }
        });
        btnSave.addClickShortcut(Key.ENTER);

        btnDiscard = new Button("Descartar");
        btnDiscard.addClickListener(
                event -> viewLogic.editProducto(currentProduct));

        Button btnCancel = new Button("Cancelar");
        btnCancel.addClickListener(event -> viewLogic.cancelProducto());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProducto())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProducto(currentProduct);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave,btnDiscard);
        //HorizontalLayout actionsLayout2 = new HorizontalLayout();
        actionsLayout.add(btnDelete,btnCancel);

        content.add(tfProdNombre,tfProdCodigoB,tfProdDescripcion,cbTipoMedida,cbCategorias,tfProdStock,tfPrecioCosto,tfPrecioVenta,chkActivo,actionsLayout);
        content.setColspan(actionsLayout,2);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    public void setCategories(List<CategoriaProducto> categories) {
        cbCategorias.setItems(categories);
    }

    public void editProducto(Producto producto) {
        if (producto == null) {
            producto = new Producto();
            producto.setActivo(true);
            btnDelete.setEnabled(false);
            tfProdStock.setEnabled(true);
        } else if(StringUtils.isBlank(producto.getId())){
            producto.setActivo(true);
            btnDelete.setEnabled(false);
            tfProdStock.setEnabled(true);
        } else {
            btnDelete.setEnabled(true);
            tfProdStock.setEnabled(false);
        }
        currentProduct = producto;
        binder.readBean(producto);
        cbCategorias.setValue(producto.getCategoria());
        cbTipoMedida.setValue(producto.getTipoMedida());
    }
}
