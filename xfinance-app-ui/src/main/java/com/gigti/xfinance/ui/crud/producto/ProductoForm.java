/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.Constantes;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * A form for editing a single product.
 */
public class ProductoForm extends FormLayout {
    private RadioButtonGroup<CategoriaProducto> rbGroupCategorias;

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
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }
    }

    public ProductoForm(ProductoCrudLogic productoCrudLogic, List<CategoriaProducto> listCategoria) {
        this.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Producto");
        this.add(title,3);

        viewLogic = productoCrudLogic;

        TextField tfProdNombre = new TextField("Nombre Producto");
        tfProdNombre.setRequired(true);
        tfProdNombre.setValueChangeMode(ValueChangeMode.EAGER);
        tfProdNombre.focus();

        TextField tfProdCodigoB = new TextField("Codigo de barras");
        tfProdCodigoB.setRequired(true);
        tfProdCodigoB.setValueChangeMode(ValueChangeMode.EAGER);

        TextField tfProdDescripcion = new TextField("Descripción");
        tfProdDescripcion.setWidth("100%");

        Checkbox chkActivo = new Checkbox("Activo");
        chkActivo.setValue(true);

        rbGroupCategorias = new RadioButtonGroup<>();
        rbGroupCategorias.setLabel("Categoria Producto");
        rbGroupCategorias.setItems(listCategoria);
        rbGroupCategorias.setRenderer(new TextRenderer<>(CategoriaProducto::getNombre));
        rbGroupCategorias.setRequired(true);
        rbGroupCategorias.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        rbGroupCategorias.setRequiredIndicatorVisible(true);

        TextField tfPrecioCosto = new TextField("Precio Costo");
        tfPrecioCosto.setPrefixComponent(new Span("$"));
        tfPrecioCosto.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfPrecioCosto.setValueChangeMode(ValueChangeMode.EAGER);

        TextField tfPrecioVenta = new TextField("Precio Venta");
        tfPrecioVenta.setPrefixComponent(new Span("$"));
        tfPrecioVenta.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfPrecioVenta.setValueChangeMode(ValueChangeMode.EAGER);

        TextField tfProdStock = new TextField("Stock(cantidad)");
        tfProdStock.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfProdStock.setValueChangeMode(ValueChangeMode.EAGER);

        binder = new BeanValidationBinder<>(Producto.class);
        binder.forField(tfPrecioCosto).withConverter(new PriceConverter()).bind(Producto::getPrecioCostoActual, Producto::setPrecioCostoActual);
        binder.forField(tfPrecioVenta).withConverter(new PriceConverter()).bind(Producto::getPrecioVentaActual, Producto::setPrecioVentaActual);
        binder.forField(tfProdStock).withConverter(new StockCountConverter()).bind(Producto::getStockActual, Producto::setStockActual);
        binder.forField(tfProdNombre).bind(Producto::getNombreProducto, Producto::setNombreProducto);
        binder.forField(tfProdDescripcion).bind(Producto::getDescripcion, Producto::setDescripcion);
        binder.forField(tfProdCodigoB).bind(Producto::getCodigoBarra, Producto::setCodigoBarra);
        binder.forField(chkActivo).bind(Producto::isActivo, Producto::setActivo);
        binder.forField(rbGroupCategorias).asRequired("Debe Seleccionar Categoria").bind(Producto::getCategoria, Producto::setCategoria);
        binder.bindInstanceFields(this);

        // enable/disable btnSave button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
        });

        btnSave = new Button("Guardar");
        btnSave.setWidth("100%");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProducto(currentProduct);
            }
        });
        btnSave.addClickShortcut(Key.ENTER);

        btnDiscard = new Button("Descartar Cambios");
        btnDiscard.setWidth("100%");
        btnDiscard.addClickListener(
                event -> viewLogic.editProducto(currentProduct));

        Button btnCancel = new Button("Cancelar");
        btnCancel.setWidth("100%");
        btnCancel.addClickListener(event -> viewLogic.cancelProducto());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProducto())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        btnDelete.setWidth("100%");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProducto(currentProduct);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave,btnDiscard);
        HorizontalLayout actionsLayout2 = new HorizontalLayout();
        actionsLayout.add(btnDelete,btnCancel);

        this.add(tfProdNombre,tfProdCodigoB,tfProdDescripcion,chkActivo,rbGroupCategorias,tfPrecioCosto,tfPrecioVenta,tfProdStock,actionsLayout,actionsLayout2);
        this.setColspan(rbGroupCategorias,2);
    }

    public void setCategories(List<CategoriaProducto> categories) {
        rbGroupCategorias.setItems(categories);
        rbGroupCategorias.setRenderer(new TextRenderer<>(CategoriaProducto::getNombre));
    }

    public void editProducto(Producto producto) {
        if (producto == null) {
            producto = new Producto();
            producto.setActivo(true);
            btnDelete.setEnabled(false);
        } else if(StringUtils.isBlank(producto.getId())){
            producto.setActivo(true);
            btnDelete.setEnabled(false);
        } else {
            btnDelete.setEnabled(true);
        }
        currentProduct = producto;
        binder.readBean(producto);
    }
}
