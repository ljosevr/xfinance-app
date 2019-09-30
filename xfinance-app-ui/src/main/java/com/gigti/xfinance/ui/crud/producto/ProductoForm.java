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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
public class ProductoForm extends Div {

    private CheckboxGroup<CategoriaProducto> chkGroupCategorias;

    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

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

    public ProductoForm(ProductoCrudLogic productoCrudLogic, List<CategoriaProducto> listCategoria) {
        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = productoCrudLogic;

        TextField tfProdNombre = new TextField("Nombre Producto");
        tfProdNombre.setWidth("100%");
        tfProdNombre.setRequired(true);
        tfProdNombre.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(tfProdNombre);

        TextField tfProdCodigoB = new TextField("Codigo de barras");
        tfProdCodigoB.setWidth("100%");
        tfProdCodigoB.setRequired(true);
        tfProdCodigoB.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(tfProdCodigoB);

        TextField tfProdDescripcion = new TextField("Descripción");
        tfProdDescripcion.setWidth("100%");
        //tfProdDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(tfProdDescripcion);

        Checkbox chkActivo = new Checkbox("Activo");
        chkActivo.setValue(true);
        content.add(chkActivo);

        chkGroupCategorias = new CheckboxGroup<>();
        chkGroupCategorias.setLabel("Categoria Producto");
        chkGroupCategorias.setItems(listCategoria);
        //chkGroupCategorias.setRenderer(new TextRenderer<>(CategoriaProducto::getNombre));
        chkGroupCategorias.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        chkGroupCategorias.setRequired(true);
        //TODO corregir las categorias
        chkGroupCategorias.addValueChangeListener(event ->{
            //TODO MEJORAR
            //if(){
                for(Object c : event.getValue().toArray()){
                    CategoriaProducto cp = (CategoriaProducto) c;
                    if(!cp.isActivo()){
                        cp.setActivo(false);
                    }
                }
            for(Object c : event.getOldValue().toArray()){
                CategoriaProducto cp = (CategoriaProducto) c;
                if(cp.isActivo()){
                    cp.setActivo(false);
                }
            }
            //}
            //if(event.getValue() != null){
                //event.getOldValue().clear();
            //}
            Notification.show(event.getHasValue().toString());
        });
        //rBGroupcategory.setId("nombre");
        //rBGroupcategory.setVisible(true);
        content.add(chkGroupCategorias);

        TextField tfProdPrecio = new TextField("Precio");
        tfProdPrecio.setSuffixComponent(new Span("€"));
        tfProdPrecio.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfProdPrecio.setValueChangeMode(ValueChangeMode.EAGER);

        TextField tfProdStock = new TextField("Stock(cantidad)");
        tfProdStock.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfProdStock.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout horizontalLayout = new HorizontalLayout(tfProdPrecio,
                tfProdStock);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, tfProdPrecio, tfProdStock);
        content.add(horizontalLayout);

        binder = new BeanValidationBinder<>(Producto.class);
        binder.forField(tfProdPrecio).withConverter(new PriceConverter()).bind(Producto::getPrecioVentaActual, Producto::setPrecioVentaActual);
        binder.forField(tfProdStock).withConverter(new StockCountConverter()).bind(Producto::getStockActual, Producto::setStockActual);
        binder.forField(tfProdNombre).bind(Producto::getNombreProducto, Producto::setNombreProducto);
        binder.forField(tfProdDescripcion).bind(Producto::getDescripcion, Producto::setDescripcion);
        binder.forField(tfProdCodigoB).bind(Producto::getCodigoBarra, Producto::setCodigoBarra);
        binder.forField(chkActivo).bind(Producto::isActivo, Producto::setActivo);
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
        btnSave.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

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

        content.add(btnSave, btnDiscard, btnDelete, btnCancel);
    }

    public void setCategories(List<CategoriaProducto> categories) {
        chkGroupCategorias.setItems(categories);
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
