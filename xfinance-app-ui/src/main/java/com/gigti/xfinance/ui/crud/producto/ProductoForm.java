/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.enums.TipoMedidaEnum;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.List;

/**
 * A form for editing a single product.
 */
public class ProductoForm extends FormLayout {

    private final H4 titleForm;
    private TextField tfProdNombre;
    private Button btnSave;
    private Binder<Producto> binder;

    public ProductoForm(List<CategoriaProducto> listCategoria, List<TipoMedidaEnum> listaTipoMedida, List<Impuesto> listImpuestos) {
        this.addClassName("form");
        //this.getElement().setAttribute("max-width", "450px");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));

        titleForm = new H4("");
        titleForm.addClassName("subTitleView");
        this.add(titleForm,3);

        tfProdNombre = new TextField("Nombre Producto");
        tfProdNombre.setRequired(true);
        tfProdNombre.focus();

        TextField tfProdCodigoB = new TextField("Codigo de barras");
        //tfProdCodigoB.setRequired(false);

        TextField tfProdDescripcion = new TextField("Descripción");

        ComboBox<TipoMedidaEnum> cbTipoMedida = new ComboBox<>();
        cbTipoMedida.setItems(listaTipoMedida);
        cbTipoMedida.setLabel("Tipo Medida");
        cbTipoMedida.setRequired(true);
        Tooltips.getCurrent().setTooltip(cbTipoMedida, "Campo para determinar que medida usa el Producto");

        Checkbox chkActivo = new Checkbox("Activo");

        ComboBox<CategoriaProducto> cbCategorias = new ComboBox<>();
        cbCategorias.setLabel("Categoria");
        cbCategorias.setItems(listCategoria);
        cbCategorias.setRequired(true);
        cbCategorias.setItemLabelGenerator(CategoriaProducto::getNombre);
        Tooltips.getCurrent().setTooltip(cbCategorias, "Puedes Agrupar en Grupos los Productos\nLas categorias se pueden crear");

        ComboBox<Impuesto> cbImpuesto = new ComboBox<>();
        cbImpuesto.setLabel("Impuesto");
        cbImpuesto.setItems(listImpuestos);
        cbImpuesto.setRequired(true);
        cbImpuesto.setItemLabelGenerator(Impuesto::getNombre);

        binder = new BeanValidationBinder<>(Producto.class);
        binder.forField(tfProdNombre).asRequired("Digite Nombre").bind(Producto::getNombreProducto, Producto::setNombreProducto);
        binder.forField(tfProdDescripcion).bind(Producto::getDescripcion, Producto::setDescripcion);
        binder.forField(tfProdCodigoB).bind(Producto::getCodigoBarra, Producto::setCodigoBarra);
        binder.forField(chkActivo).bind(Producto::isActivo, Producto::setActivo);
        binder.forField(cbCategorias).asRequired("Seleccione una Categoria").bind(Producto::getCategoria, Producto::setCategoria);
        binder.forField(cbTipoMedida).asRequired("Seleccione una Unidad de Medida").bind(Producto::getTipoMedida, Producto::setTipoMedida);
        binder.forField(cbImpuesto).asRequired("Seleccione un Impuesto").bind(Producto::getImpuesto, Producto::setImpuesto);

        binder.addStatusChangeListener(event -> btnSave.setEnabled(binder.isValid()));

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete,btnClose);

        this.add(tfProdNombre,tfProdCodigoB,
                tfProdDescripcion, cbTipoMedida,
                cbCategorias, cbImpuesto,
                chkActivo,actionsLayout);

    }

    public void setProducto(Producto producto, String title) {
        binder.setBean(producto);
        titleForm.setText(title);
        tfProdNombre.focus();
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        } else {
            NotificacionesUtil.showError("Validar Producto: "+binder.validate().getValidationErrors());
        }
    }

    // Events
    public static abstract class ProductoFormEvent extends ComponentEvent<ProductoForm> {
        private Producto producto;

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
