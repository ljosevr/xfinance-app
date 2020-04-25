package com.gigti.xfinance.ui.crud.compra;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.shared.Registration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CompraDetailForm extends FormLayout {

    private H4 titleForm;
    private Empresa empresa;
    private TextField tfFactura;
    private FormLayout formDataLayout;
    private Button btnSave;
    private Button btnNext;
    private Binder<Compra> binder;
    private CompraItemGrid itemsGrid;
    private ProductoService productoService;
    private HorizontalLayout actionsLayout;
    private List<CompraItem> listaItems = new ArrayList<>();
    private Producto selectedProd;
    private CompraItem selectedItemGrid;
    private Button btnAgregar;
    private Button btnQuitar;
    private BigDecimalField tfVenta;
    private BigDecimalField tfCantidad;
    private TextField tfProducto;
    private BigDecimalField tfCosto;
    private ComboBox<Producto> cbProductos;
    private ProductoValor productoValor;
    private Button btnUpdate;

    public CompraDetailForm(ProductoService productoService) {
        this.productoService = productoService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
        listaItems = new ArrayList<>();

        titleForm = new H4("");
        titleForm.addClassName("subTitleView");
        this.add(titleForm,3);

        this.setClassName("formLayout");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1)/*,
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3)*/);

        configureFormData();
        configureFormItems();
    }

    private void configureFormData() {

        formDataLayout = new FormLayout();
        formDataLayout.setClassName("formLayout");
        formDataLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 3),
                new FormLayout.ResponsiveStep("40em", 5));
        tfFactura = new TextField("# Factura");
        tfFactura.setRequired(true);
        tfFactura.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfFactura.setClearButtonVisible(true);
        tfFactura.focus();

        DatePicker dpFechaCompra = new DatePicker();
        dpFechaCompra.setLabel("Fecha Compra");
        dpFechaCompra.setRequired(true);
        dpFechaCompra.setClearButtonVisible(true);
        dpFechaCompra.getElement().setAttribute("theme", "align-center");
        dpFechaCompra.getElement().setAttribute("theme", "small");

        TextField tfProveedor = new TextField("Nombre Proveedor");
        tfProveedor.setRequired(true);
        tfProveedor.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfProveedor.setClearButtonVisible(true);

        TextField tfTelefonoProveedor = new TextField("Telefono Proveedor");
        tfTelefonoProveedor.setRequired(false);
        tfTelefonoProveedor.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfTelefonoProveedor.setClearButtonVisible(true);

        TextField tfDireccionProveedor = new TextField("Dirección Proveedor");
        tfDireccionProveedor.setRequired(false);
        tfDireccionProveedor.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfDireccionProveedor.setClearButtonVisible(true);

        BigDecimalField tfTotalFactura = new BigDecimalField("Total Factura");
        tfTotalFactura.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfTotalFactura.setReadOnly(true);
        tfTotalFactura.setValue(BigDecimal.valueOf(0));

        binder = new BeanValidationBinder<>(Compra.class);
        binder.forField(tfFactura).asRequired("Digite # Factura").bind(Compra::getNumeroFactura, Compra::setNumeroFactura);
        binder.forField(dpFechaCompra).asRequired("Digite Fecha de Factura").bind(Compra::getFechaCompraLD, Compra::setFechaCompraLD);
        binder.forField(tfProveedor).bind(Compra::getProveedor, Compra::setProveedor);
        binder.forField(tfTelefonoProveedor).bind(Compra::getTelefonoProveedor, Compra::setTelefonoProveedor);
        binder.forField(tfDireccionProveedor).bind(Compra::getDireccionProveedor, Compra::setDireccionProveedor);

        binder.addStatusChangeListener(event -> btnSave.setEnabled(binder.isValid() && (listaItems != null && listaItems.size() > 0)));

        formDataLayout.add(tfFactura, dpFechaCompra, tfProveedor,
                tfTelefonoProveedor, tfDireccionProveedor);

        this.add(formDataLayout, 3);
    }

    private void configureFormItems() {
        VerticalLayout itemLayout = new VerticalLayout();
        itemLayout.setSpacing(false);
        itemLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);

        H4 subTitle = new H4("Items Compra");
        subTitle.addClassName("subTitleView");

        configureItemGrid();

        cbProductos = new ComboBox<>();
        cbProductos.getElement().setAttribute("theme", "small");
        cbProductos.setItemLabelGenerator(Producto::getNombreProducto);
        cbProductos.setPlaceholder("Buscar Producto");

        ComboBox.ItemFilter<Producto> filter = (prod, filterString) ->
                prod.getNombreProducto().toLowerCase()
                        .contains(filterString.toLowerCase());

        cbProductos.setItems(filter, productoService.findAll(empresa));
        selectedProd = new Producto();

        cbProductos.addValueChangeListener(evt -> {
            selectedProd = evt.getValue();
            if(selectedProd != null) {
                Response result = productoService.getPriceVenta(selectedProd);
                if(result.isSuccess()) {
                    productoValor = (ProductoValor) result.getObject();
                    tfProducto.setValue(selectedProd.getNombreProducto());
                    tfCantidad.setValue(BigDecimal.valueOf(0));
                    tfCosto.setValue(BigDecimal.valueOf(0));
                    tfVenta.setValue(productoValor.getPrecioVenta());
                    btnAgregar.setEnabled(true);
                    tfCantidad.focus();
                } else {
                    NotificacionesUtil.showError(result.getMessage());
                }
            } else {
                clearData();
            }
        });

        cbProductos.setAllowCustomValue(true);
        cbProductos.addCustomValueSetListener(evt -> {
            cbProductos.setItems(productoService.findAll(evt.getDetail(), empresa, 0, 30));
        });
        cbProductos.setClearButtonVisible(true);
        cbProductos.setRenderer(TemplateRenderer.<Producto>of(
                "<div>[[item.nombre]]<br><small>[[item.tipMedida]]</small></div>")
                .withProperty("nombre", prod -> prod.getNombreProducto().toUpperCase())
                .withProperty("tipMedida", prod -> prod.getTipoMedida().toString().toLowerCase()));

        FormLayout formItemLayout = new FormLayout();
        formItemLayout.setClassName("formLayout");
        formItemLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 3),
                new FormLayout.ResponsiveStep("40em", 5));

        tfProducto = new TextField("Producto");
        tfProducto.setReadOnly(true);
        tfProducto.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        tfCantidad = new BigDecimalField("Cantidad");
        tfCantidad.setReadOnly(false);
        tfCantidad.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfCantidad.setAutoselect(true);

        tfCosto = new BigDecimalField("P. Costo");
        tfCosto.setReadOnly(false);
        tfCosto.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfCosto.setPrefixComponent(new Span("$"));
        tfCosto.setAutoselect(true);

        tfVenta = new BigDecimalField("P. Venta");
        tfVenta.setReadOnly(false);
        tfVenta.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfVenta.setPrefixComponent(new Span("$"));
        tfVenta.setAutoselect(true);

        btnAgregar = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAgregar.setEnabled(false);
        btnAgregar.addClickListener(evt -> {
            if(selectedProd != null) {
                CompraItem item = new CompraItem();
                item.setProducto(selectedProd);
                item.setCantidad(tfCantidad.getValue());
                item.setPrecioTotalCosto(tfCosto.getValue());
                item.setPrecioVenta(tfVenta.getValue());
                listaItems.add(item);
                itemsGrid.setItems(listaItems);
                clearData();
            }
        });

        btnQuitar = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        btnQuitar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnQuitar.setEnabled(false);
        btnQuitar.addClickListener(evt -> {
            listaItems.remove(selectedItemGrid);
            itemsGrid.setItems(listaItems);
            btnQuitar.setEnabled(false);
        });

        btnUpdate = new Button(new Icon(VaadinIcon.CHECK_CIRCLE));
        btnUpdate.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnUpdate.setEnabled(false);
        btnUpdate.addClickListener(evt -> {
            if(selectedProd != null) {
                CompraItem item = new CompraItem();
                item.setProducto(selectedProd);
                item.setCantidad(tfCantidad.getValue());
                item.setPrecioTotalCosto(tfCosto.getValue());
                item.setPrecioVenta(tfVenta.getValue());
                if(listaItems.removeIf(i -> i.getProducto().getId().equals(selectedProd.getId()))) {
                    listaItems.add(item);
                    itemsGrid.setItems(listaItems);
                } else {
                    NotificacionesUtil.showError("No fue posible actualizar Item en la tabla");
                }

                clearData();
            }
        });

        formItemLayout.add(cbProductos);
        formItemLayout.setColspan(cbProductos, 5);
        formItemLayout.add(tfProducto, tfCantidad, tfCosto,tfVenta, new HorizontalLayout(btnAgregar, btnQuitar, btnUpdate));

        VerticalLayout gridLayout = new VerticalLayout(itemsGrid);
        gridLayout.addClassName("grid");

        //Actions CENTER
        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        //btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        Button btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));

        actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);
        actionsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        itemLayout.add(subTitle, formItemLayout, gridLayout, actionsLayout);

        itemLayout.setMargin(false);
        itemLayout.setPadding(false);

        this.add(itemLayout, 2);
    }

    private void clearData() {
        selectedProd = null;
        cbProductos.setValue(null);
        tfProducto.setValue("");
        tfCantidad.setValue(BigDecimal.valueOf(0));
        tfCosto.setValue(BigDecimal.valueOf(0));
        tfVenta.setValue(BigDecimal.valueOf(0));
        btnAgregar.setEnabled(false);
        btnQuitar.setEnabled(false);
        btnUpdate.setEnabled(false);
        productoValor = null;
        cbProductos.focus();
    }

    private void configureItemGrid() {
        itemsGrid = new CompraItemGrid();
        itemsGrid.setItems(listaItems);
        itemsGrid.asSingleSelect().addValueChangeListener(evt -> {
            selectedItemGrid = evt.getValue();
            if(selectedItemGrid != null) {
                btnQuitar.setEnabled(evt.getValue() != null);
                btnUpdate.setEnabled(evt.getValue() != null);
                tfProducto.setValue(selectedItemGrid.getProducto().getNombreProducto());
                tfCantidad.setValue(selectedItemGrid.getCantidad());
                tfCosto.setValue(selectedItemGrid.getPrecioTotalCosto());
                tfVenta.setValue(selectedItemGrid.getPrecioVenta());
                selectedProd = selectedItemGrid.getProducto();
                tfCantidad.focus();
            }
        });
    }

    public void setCompra(Compra compra, String title) {
        binder.setBean(compra);
        titleForm.setText(title);

        if(compra != null && compra.getItems() != null) {
            listaItems =  compra.getItems();
            itemsGrid.setItems(listaItems);
        }

        tfFactura.focus();
    }

    private void validateAndSave() {
        if (binder.validate().isOk() && listaItems.size() > 0) {
            Compra compra = binder.getBean();
            compra.setItems(listaItems);
            fireEvent(new SaveEvent(this, compra));
        } else {
            NotificacionesUtil.showError("Validar Compra: "+binder.validate().getValidationErrors());
        }
    }

    // Events
    public static abstract class CompraFormEvent extends ComponentEvent<CompraDetailForm> {
        private Compra compra;

        CompraFormEvent(CompraDetailForm source, Compra compra) {
            super(source, false);
            this.compra = compra;
        }

        public Compra getCompra() {
            return compra;
        }
    }

    public static class SaveEvent extends CompraFormEvent {
        SaveEvent(CompraDetailForm source, Compra compra) {
            super(source, compra);
        }
    }

    public static class DeleteEvent extends CompraFormEvent {
        DeleteEvent(CompraDetailForm source, Compra compra) {
            super(source, compra);
        }
    }

    public static class CloseEvent extends CompraFormEvent {
        CloseEvent(CompraDetailForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
