/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Producto;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

/**
 * A form for editing a single product.
 */
public class ProductoForm extends Div {

    private VerticalLayout content;

    private TextField productName;
    private TextField codigobarra;
    private TextField descripcion;
    private Checkbox activo;

    private TextField price;
    private TextField stockCount;

    //private Select<Availability> availability;
    private CheckboxGroup<CategoriaProducto> category;

    private Button save;
    private Button discard;
    private Button cancel;
    private Button delete;

    private ProductoCrudLogic viewLogic;
    private Binder<Producto> binder;
    private Producto currentProduct;

    private static class PriceConverter extends StringToBigDecimalConverter {

        public PriceConverter() {
            super(BigDecimal.ZERO, "Cannot convert value to a number.");
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
            super(0, "Could not convert value to " + Integer.class.getName()
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

    public ProductoForm(ProductoCrudLogic productoCrudLogic) {
        setClassName("standard-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = productoCrudLogic;

        productName = new TextField("Nombre Producto");
        productName.setWidth("100%");
        productName.setRequired(true);
        productName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(productName);

        codigobarra = new TextField("Codigo de barras");
        codigobarra.setWidth("100%");
        codigobarra.setRequired(true);
        codigobarra.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(codigobarra);

        descripcion = new TextField("Descripción");
        descripcion.setWidth("100%");
        //descripcion.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(descripcion);

        activo = new Checkbox();
        activo.setLabel("Categorias");
        activo.setValue(true);
        content.add(activo);

        category = new CheckboxGroup<>();
        category.setLabel("Categoria Producto");
        category.setId("category");
        category.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        content.add(category);

        price = new TextField("Price");
        price.setSuffixComponent(new Span("€"));
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setValueChangeMode(ValueChangeMode.EAGER);

        stockCount = new TextField("In stock");
        stockCount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout horizontalLayout = new HorizontalLayout(price,
                stockCount);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, price, stockCount);
        content.add(horizontalLayout);

//        availability = new Select<>();
//        availability.setLabel("Availability");
//        availability.setWidth("100%");
//        availability.setItems(Availability.values());
//        content.add(availability);



        binder = new BeanValidationBinder<>(Producto.class);
        //binder.forField(price).withConverter(new PriceConverter()).bind("price");
        //binder.forField(stockCount).withConverter(new StockCountConverter()).bind("stockCount");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Guardar");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProducto(currentProduct);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Descartar Cambios");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editProducto(currentProduct));

        cancel = new Button("Cancelar");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelProducto());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProducto())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Eliminar");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProducto(currentProduct);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void setCategories(Collection<CategoriaProducto> categories) {
        category.setItems(categories);
    }

    public void editProducto(Producto producto) {
        if (producto == null) {
            producto = new Producto();
        }
        //delete.setVisible(!producto.isNewProduct());
        currentProduct = producto;
        binder.readBean(producto);
    }
}
