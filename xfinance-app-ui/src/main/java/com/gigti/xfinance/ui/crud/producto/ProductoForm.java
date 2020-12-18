/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.TipoMedida;
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
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.vaadin.flow.shared.Registration;

import java.math.BigDecimal;
import java.util.List;

/**
 * A form for editing a single product.
 */
public class ProductoForm extends Dialog {

    private final H2 titleForm;
    private final BigDecimalField tfPrecioCosto;
    private final BigDecimalField tfPrecioVenta;
    private final Checkbox chkControlarStock;
    private final Button btnDelete;
    private final ComboBox<Impuesto> cbImpuesto;
    private final TextField tfProdCodigoB;
    private final TextField tfProdDescripcion;
    private final Checkbox chkActivo;
    private final ComboBox<CategoriaProducto> cbCategorias;
    private final Checkbox chkManageInitialStock;
    private final Checkbox chkInventarioFinal;
    private final FormLayout contentSub;
    private final TextField   tfProdNombre;
    private final ComboBox<TipoMedida> cbTipoMedida;
    private Button btnSave;
    private final Binder<Producto> binder;
    private final BigDecimalField tfCantidadInicial;

    public ProductoForm(List<CategoriaProducto> listCategoria, List<TipoMedida> listaTipoMedida, List<Impuesto> listImpuestos) {

        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());

        contentSub = new FormLayout();
        contentSub.setClassName("formLayout");
        contentSub.setResponsiveSteps(MyResponsiveStep.getMyList());

        H4 subTitleData = new H4("Inventario Inicial");
        subTitleData.addClassName("subTitleView");

        titleForm = new H2("");
        titleForm.addClassName("titleView");


        tfProdNombre = new TextField("Nombre Producto");
        tfProdNombre.setRequired(true);
        tfProdNombre.focus();

        tfProdCodigoB = new TextField("Codigo de barras");

        tfProdDescripcion = new TextField("Descripción");

        cbTipoMedida = new ComboBox<>();
        cbTipoMedida.setItems(listaTipoMedida);
        cbTipoMedida.setLabel("Tipo Medida");
        cbTipoMedida.setRequired(true);
        cbTipoMedida.setAllowCustomValue(false);
        cbTipoMedida.setAutoOpen(true);
        cbTipoMedida.setClearButtonVisible(true);
        cbTipoMedida.setItemLabelGenerator(TipoMedida::getNombre);
        cbTipoMedida.getElement().setAttribute("title", "Campo para determinar que medida usa el Producto");
        //Tooltips.getCurrent().setTooltip(cbTipoMedida, "Campo para determinar que medida usa el Producto");

        chkActivo = new Checkbox("Activo");

        cbCategorias = new ComboBox<>();
        cbCategorias.setLabel("Categoria");
        cbCategorias.setItems(listCategoria);
        cbCategorias.setRequired(true);
        cbCategorias.setAllowCustomValue(false);
        cbCategorias.setAutoOpen(true);
        cbCategorias.setClearButtonVisible(true);
        cbCategorias.setItemLabelGenerator(CategoriaProducto::getNombre);
        cbCategorias.getElement().setAttribute("title", "Puedes Agrupar en Grupos los Productos\nLas categorias se pueden crear");
        //Tooltips.getCurrent().setTooltip(cbCategorias, "Puedes Agrupar en Grupos los Productos\nLas categorias se pueden crear");

        cbImpuesto = new ComboBox<>();
        cbImpuesto.setLabel("Impuesto");
        cbImpuesto.setItems(listImpuestos);
        cbImpuesto.setRequired(true);
        cbImpuesto.setAllowCustomValue(false);
        cbImpuesto.setAutoOpen(true);
        cbImpuesto.setClearButtonVisible(true);
        cbImpuesto.setItemLabelGenerator(Impuesto::getNombre);

        chkManageInitialStock = new Checkbox("Editar Inventario Inicial");
        chkManageInitialStock.getElement().setAttribute("title","Habilitar Agregar Inventario Inicial");

        tfCantidadInicial = new BigDecimalField("Cantidad Inicial");
        tfCantidadInicial.focus();
        tfCantidadInicial.setAutoselect(true);
        tfCantidadInicial.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        chkControlarStock = new Checkbox("Controlar Stock(Inventario)");
        chkControlarStock.setValue(false);
        chkControlarStock.setRequiredIndicatorVisible(true);
        chkControlarStock.getElement().setAttribute("title","Si esta marcado, vas a manejar Inventario con este Producto\nSi no esta marcado no vas a manejar Inventario");

        tfPrecioCosto = new BigDecimalField("Precio Costo Inicial");
        tfPrecioCosto.setPrefixComponent(new Span("$"));
        tfPrecioCosto.setAutoselect(true);
        tfPrecioCosto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfPrecioVenta = new BigDecimalField("Precio Venta Inicial");
        tfPrecioVenta.setPrefixComponent(new Span("$"));
        tfPrecioVenta.setAutoselect(true);
        tfPrecioVenta.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        chkInventarioFinal = new Checkbox("Inventario Definitivo");
        chkInventarioFinal.setValue(false);
        chkInventarioFinal.setRequiredIndicatorVisible(true);
        chkInventarioFinal.getElement().setAttribute("title","Si esta marcado, el inventario Inicial No se podrá cambiar");

        chkInventarioFinal.addValueChangeListener(event -> {
           if(chkInventarioFinal.getValue()){
               NotificacionesUtil.showWarn("Al Estar Marcado Inventario Definitivo, El inventario Inicial No se podrá Cambiar más");
           }
        });
        chkControlarStock.addValueChangeListener(event -> {
            if(event.getValue()){
                tfCantidadInicial.setReadOnly(false);
                tfCantidadInicial.focus();
            } else {
                tfCantidadInicial.setReadOnly(true);
                tfCantidadInicial.setValue(BigDecimal.ZERO);
                tfPrecioCosto.focus();
            }
        });

        chkManageInitialStock.addValueChangeListener(event -> {
            if(event.getValue()) {
                contentSub.setVisible(true);
                chkControlarStock.setValue(true);
                tfCantidadInicial.focus();
            } else {
                contentSub.setVisible(false);
                btnSave.focus();
            }
        });

        //TODO READBEAN AND WRITEBEAN

        binder = new Binder<>(Producto.class);
        binder.forField(tfProdNombre).asRequired("Digite Nombre").bind(Producto::getNombreProducto, Producto::setNombreProducto);
        binder.forField(tfProdDescripcion).bind(Producto::getDescripcion, Producto::setDescripcion);
        binder.forField(tfProdCodigoB).bind(Producto::getCodigoBarra, Producto::setCodigoBarra);
        binder.forField(chkActivo).bind(Producto::isActivo, Producto::setActivo);
        binder.forField(cbCategorias).asRequired("Seleccione una Categoria").bind(Producto::getCategoria, Producto::setCategoria);
        binder.forField(cbTipoMedida).asRequired("Seleccione una Unidad de Medida").bind(Producto::getTipoMedida, Producto::setTipoMedida);
        binder.forField(cbImpuesto).asRequired("Seleccione un Impuesto").bind(Producto::getImpuesto, Producto::setImpuesto);
        binder.forField(tfCantidadInicial).withValidator(v -> {
            if(chkManageInitialStock.getValue() && chkControlarStock.getValue()) {
                return tfCantidadInicial.getOptionalValue().isPresent();
            } else {
                return true;
            }
        },"Digita una Cantidad de Stock Inicial").bind(Producto::getCantidadInicial, Producto::setCantidadInicial);
        binder.forField(tfPrecioCosto).withValidator(v -> {
            if(chkManageInitialStock.getValue()) {
                return tfPrecioCosto.getOptionalValue().isPresent();
            } else {
                return true;
            }
        }, "Digita precio de Costo Inicial").bind(Producto::getPrecioCosto, Producto::setPrecioCosto);
        binder.forField(tfPrecioVenta).withValidator(v -> {
            if(chkManageInitialStock.getValue()) {
                return tfPrecioVenta.getOptionalValue().isPresent();
            } else {
                return true;
            }
        },"Digita precio de Venta Inicial").bind(Producto::getPrecioVenta, Producto::setPrecioVenta);
        binder.forField(chkControlarStock).bind(Producto::isManageStock, Producto::setManageStock);
        binder.forField(chkManageInitialStock).bind(Producto::isManageInitialStock, Producto::setManageInitialStock);
        binder.forField(chkInventarioFinal).bind(Producto::isInventarioDefinitivo, Producto::setInventarioDefinitivo);

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete,btnClose);

        contentSub.add(subTitleData, chkControlarStock,
                tfCantidadInicial, tfPrecioCosto, tfPrecioVenta, chkInventarioFinal);
        content.setColspan(subTitleData, contentSub.getResponsiveSteps().size()+1);

        content.add(titleForm, tfProdNombre,tfProdCodigoB,
                tfProdDescripcion, cbTipoMedida,
                cbCategorias, cbImpuesto, chkActivo, chkManageInitialStock, contentSub, actionsLayout);

        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        content.setColspan(contentSub, content.getResponsiveSteps().size()+1);
        content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);

    }

    public void setProducto(Producto producto, String title, String type) {
        binder.setBean(producto);
        setReadOnlyByDelete(type.equals(ICrudView.OPTION_DELETE));
        if(type.equals(ICrudView.OPTION_EDIT) && (producto != null && producto.isInventarioDefinitivo())) {
           tfCantidadInicial.setReadOnly(true);
           tfPrecioCosto.setReadOnly(true);
           tfPrecioVenta.setReadOnly(true);
           chkControlarStock.setReadOnly(true);
           chkManageInitialStock.setVisible(false);
           chkInventarioFinal.setReadOnly(true);
        }
        if(type.equals(ICrudView.OPTION_ADD)) {
            chkManageInitialStock.setVisible(true);
        }
        contentSub.setVisible((ICrudView.OPTION_ADD.equals(type) || producto != null) && producto.isManageInitialStock());
        btnDelete.setEnabled(!ICrudView.OPTION_ADD.equals(type));
        titleForm.setText(title);
        tfProdNombre.focus();
    }

    private void setReadOnlyByDelete(boolean readOnly) {
        btnSave.setVisible(!readOnly);
        btnDelete.setText(readOnly ? "Sí, Eliminar" : "Eliminar");
        tfPrecioVenta.setReadOnly(readOnly);
        tfProdNombre.setReadOnly(readOnly);
        tfProdCodigoB.setReadOnly(readOnly);
        tfProdDescripcion.setReadOnly(readOnly);
        cbCategorias.setReadOnly(readOnly);
        cbImpuesto.setReadOnly(readOnly);
        cbTipoMedida.setReadOnly(readOnly);
        chkActivo.setReadOnly(readOnly);
        tfCantidadInicial.setReadOnly(readOnly);
        tfPrecioCosto.setReadOnly(readOnly);
        tfPrecioVenta.setReadOnly(readOnly);
        chkControlarStock.setReadOnly(readOnly);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class ProductoFormEvent extends ComponentEvent<ProductoForm> {
        private final Producto producto;

        ProductoFormEvent(ProductoForm source, Producto producto) {
            super(source, false);
            this.producto = producto;
        }

        public Producto getProducto() {
            return producto;
        }
    }

    public static class SaveEvent extends ProductoFormEvent {
        SaveEvent(ProductoForm source, Producto producto) {
            super(source, producto);
        }
    }

    public static class DeleteEvent extends ProductoFormEvent {
        DeleteEvent(ProductoForm source, Producto producto) {
            super(source, producto);
        }
    }

    public static class CloseEvent extends ProductoFormEvent {
        CloseEvent(ProductoForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
