package com.gigti.xfinance.ui.crud.compra;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.backend.services.ProveedorService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.components.MyButton;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.shared.Registration;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@PreserveOnRefresh
public class CompraDetailForm extends VerticalLayout {

    private final H2 titleForm;
    private final Empresa empresa;
    private TextField tfFactura;
    private Binder<Compra> binder;
    private CompraItemGrid itemsGrid;
    private final ProductoService productoService;
    private ProveedorService proveedorService;
    private List<CompraItem> listaItems;
//    private Producto selectedProd;
    private BigDecimalField tfVenta;
    private BigDecimalField tfCantidad;
    private BigDecimalField tfCostoU;
    private BigDecimalField tfCostoTotal;
    private ComboBox<Producto> cbProductos;
    private ProductoValorVenta productoValorVenta;
    private Button btnAgregar;
    private Button btnUpdate;
    private Button btnSave;
    private Button btnDelete;
    private Button btnClose;
    private Editor<CompraItem> itemGridEditor;
    private DataProvider<Proveedor, String> dataProviderProveedores;


    public CompraDetailForm(ProductoService productoService, ProveedorService proveedorService) {
        this.productoService = productoService;
        this.proveedorService = proveedorService;
        empresa = CurrentUser.get() != null ? Objects.requireNonNull(CurrentUser.get()).getPersona().getEmpresa() : null;
        listaItems = new ArrayList<>();

        this.addClassName("CompraDetailFormView");
        this.setDefaultHorizontalComponentAlignment(Alignment.START);

        titleForm = new H2("");
        titleForm.addClassName("titleView");
        this.add(titleForm);

        configureFormData();
        configureFormItems();
        configureActionButtons();
    }

    private void configureFormData() {

        H4 subTitleData = new H4("Datos Factura");
        subTitleData.addClassName("subTitleView");

        FormLayout formDataLayout = new FormLayout();
        formDataLayout.setClassName("formLayout");
        formDataLayout.setResponsiveSteps(MyResponsiveStep.getMyList());
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

        ComboBox<Proveedor> cbProveedores = new ComboBox<>("Proveedor");
        cbProveedores.getElement().setAttribute("theme", "small");
        cbProveedores.setItemLabelGenerator(Proveedor::getNombre);
        cbProveedores.setPlaceholder("Buscar por Nombre o Nit");

        ComboBox.ItemFilter<Proveedor> filter = (prov, filterString) ->
                prov.getNombre().toLowerCase()
                        .contains(filterString.toLowerCase());

        cbProveedores.setItems(filter, proveedorService.find("", empresa, null));

        cbProveedores.setAllowCustomValue(true);
        configureProvider();
        cbProveedores.setDataProvider(dataProviderProveedores);
        cbProveedores.setClearButtonVisible(true);
        cbProveedores.setRenderer(TemplateRenderer.<Proveedor>of(
                "<div>[[item.nombre]]<br><small>[[item.identificacion]]</small></div>")
                .withProperty("nombre", prov -> prov.getNombre().toUpperCase())
                .withProperty("identificacion", prov -> prov.getIdentificacion().toLowerCase()));


        BigDecimalField tfTotalFactura = new BigDecimalField("Total Factura");
        tfTotalFactura.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tfTotalFactura.setReadOnly(true);
        tfTotalFactura.setValue(BigDecimal.valueOf(0));

        binder = new BeanValidationBinder<>(Compra.class);
        binder.forField(tfFactura).asRequired("Digite # Factura").bind(Compra::getNumeroFactura, Compra::setNumeroFactura);
        binder.forField(dpFechaCompra).asRequired("Digite Fecha de Factura").bind(Compra::getFechaCompraLD, Compra::setFechaCompraLD);
        binder.forField(cbProveedores).asRequired("Seleccione Proveedor").bind(Compra::getProveedor, Compra::setProveedor);
        binder.addStatusChangeListener(event -> enableButtonSave());

        formDataLayout.add(tfFactura, dpFechaCompra, cbProveedores);

        this.add(subTitleData, formDataLayout);
    }

    public void configureProvider() {
        dataProviderProveedores = DataProvider.fromFilteringCallbacks(
                        query -> {
                            String filter = query.getFilter().orElse(null);
                            return proveedorService.find(filter, empresa, new OffsetBasedPageRequest(query)).stream();
                        },
                        query -> {
                            String filter = query.getFilter().orElse(null);
                            return (int) proveedorService.countSearch(filter, empresa);
                        }
        );
    }

    private void enableButtonSave() {
        btnSave.setEnabled(binder.isValid() && (listaItems != null && listaItems.size() > 0));
    }

    private void configureFormItems() {
        H4 subTitleItems = new H4("Seleccionar Productos");
        subTitleItems.addClassName("subTitleView");

        configureItemGrid();

        cbProductos = new ComboBox<>();
        cbProductos.getElement().setAttribute("theme", "small");
        cbProductos.setItemLabelGenerator(Producto::getNombreProducto);
        cbProductos.setPlaceholder("Buscar Producto");
        cbProductos.getElement().setAttribute("align-self:","flex-start");
        //cbProductos.setMinWidth("350px");

        ComboBox.ItemFilter<Producto> filter = (prod, filterString) ->
                prod.getNombreProducto().toLowerCase()
                        .contains(filterString.toLowerCase());

        cbProductos.setItems(filter, productoService.findAllByEmpresaAndNotInfinite(empresa));
//        selectedProd = new Producto();

//        cbProductos.addValueChangeListener(evt -> {
//            if(evt.getValue() != null) {
//                btnAgregar.setEnabled(true);
//            } else {
//                clearData();
//            }
//        });

        cbProductos.setAllowCustomValue(true);
        cbProductos.addCustomValueSetListener(evt -> {
            cbProductos.setItems(productoService.findAll(evt.getDetail(), empresa, 0, 30));
        });
        cbProductos.setClearButtonVisible(true);
        cbProductos.setRenderer(TemplateRenderer.<Producto>of(
                "<div>[[item.nombre]]<br><small>[[item.tipMedida]]</small></div>")
                .withProperty("nombre", prod -> prod.getNombreProducto().toUpperCase())
                .withProperty("tipMedida", prod -> prod.getTipoMedida().getSimbolo().toLowerCase()));

        btnAgregar = MyButton.MyButton("Adicionar", new Icon(VaadinIcon.PLUS_CIRCLE),
                "Agregar Producto a la compra", ButtonVariant.LUMO_PRIMARY, true, true);
        btnAgregar.addClickListener(evt -> {
            Producto selectedProd = cbProductos.getValue();
            if(selectedProd != null) {
                Response result = productoService.getPriceVenta(selectedProd);
                if(result.isSuccess()) {
                    productoValorVenta = (ProductoValorVenta) result.getObject();

                    CompraItem item = new CompraItem();
                    item.setProducto(selectedProd);
                    item.setCantidad(BigDecimal.valueOf(1).setScale(2));
                    item.setPrecioTotalCosto(BigDecimal.valueOf(0).setScale(2));
                    item.setPrecioCosto(BigDecimal.ZERO.setScale(2));
                    item.setPrecioVenta(productoValorVenta.getValorVenta().setScale(2));
                    item.setItem(listaItems.size()+1);
                    listaItems.add(item);
                    itemsGrid.setItems(listaItems);
                    itemGridEditor.editItem(item);
                    enableButtonSave();
                    clearData();
                    tfCantidad.focus();
                } else {
                    NotificacionesUtil.showError(result.getMessage());
                }
            }
        });

        FormLayout formProductos = new FormLayout();
        formProductos.setResponsiveSteps(MyResponsiveStep.getMyList());
        formProductos.setClassName("formLayout");
        formProductos.add(cbProductos, btnAgregar);


        VerticalLayout gridLayout = new VerticalLayout(itemsGrid);
        gridLayout.addClassName("grid");
        gridLayout.setSizeFull();
        formProductos.add(gridLayout);

        this.add(subTitleItems, formProductos);
        //this.add(gridLayout);
    }

    private void configureActionButtons() {

        //Actions CENTER
        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> validateAndDelete());
        btnDelete.setEnabled(false);

        btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new CloseEvent(this)));

        this.add(new HorizontalLayout(btnSave, btnDelete, btnClose));
    }

    private void validateAndDelete() {
        //TODO Make Eliminar
        //NotificacionesUtil.openConfirmationDialog("Al Eliminar Compra, Afectará Saldos y de Productos. \n ¿Desea Continuar?", true, false);
        NotificacionesUtil.showError("Acción Pendiente de Construir");
        NotificacionesUtil.getSiButton().addClickListener(event -> {
            if(NotificacionesUtil.getDialog().isOpened())
                NotificacionesUtil.getDialog().close();
            fireEvent(new DeleteEvent(this, binder.getBean()));
        });
        NotificacionesUtil.getNoButton().addClickListener(event -> {
            if(NotificacionesUtil.getDialog().isOpened())
                NotificacionesUtil.getDialog().close();
            tfFactura.focus();
        });
    }

    private void clearData() {
        cbProductos.setValue(null);
        btnAgregar.setEnabled(false);
        productoValorVenta = null;
        cbProductos.focus();
    }

    private void configureItemGrid() {
        itemsGrid = new CompraItemGrid();
        itemsGrid.setItems(listaItems);
        itemsGrid.addItemDoubleClickListener(evt -> {
            itemGridEditor.editItem(evt.getItem());
            tfCantidad.focus();
        });

        Grid.Column<CompraItem> cantidadColumn = itemsGrid.getColumnByKey("cantidad");
        Grid.Column<CompraItem> costoTColumn = itemsGrid.getColumnByKey("costoT");
        Grid.Column<CompraItem> costoUColumn = itemsGrid.getColumnByKey("costoU");
        Grid.Column<CompraItem> ventaColumn = itemsGrid.getColumnByKey("ventaU");

        tfCantidad = new BigDecimalField();
        tfCantidad.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);
        tfCantidad.setAutoselect(true);
        tfCantidad.addKeyPressListener(Key.ENTER, keyPressEvent -> tfCostoTotal.focus());
        tfCantidad.addValueChangeListener(evt -> calculatedPriceCost());

        tfCostoTotal = new BigDecimalField();
        tfCostoTotal.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfCostoTotal.setPrefixComponent(new Span("$"));
        tfCostoTotal.setAutoselect(true);
        tfCostoTotal.addKeyPressListener(Key.ENTER, keyPressEvent -> tfVenta.focus());
        tfCostoTotal.addValueChangeListener(evt -> calculatedPriceCost());

        tfCostoU = new BigDecimalField();
        tfCostoU.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfCostoU.setPrefixComponent(new Span("$"));
        tfCostoU.setReadOnly(true);

        tfVenta = new BigDecimalField();
        tfVenta.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        tfVenta.setPrefixComponent(new Span("$"));
        tfVenta.setAutoselect(true);
        tfVenta.addKeyPressListener(Key.ENTER, keyPressEvent -> btnUpdate.focus());

        Binder<CompraItem> itemBinder = new BeanValidationBinder<>(CompraItem.class);
        itemGridEditor = itemsGrid.getEditor();
        itemGridEditor.setBinder(itemBinder);
        itemGridEditor.setBuffered(true);

        itemBinder.forField(tfCantidad).asRequired("Obligatorio").bind(CompraItem::getCantidad, CompraItem::setCantidad);
        itemBinder.forField(tfCostoTotal).asRequired("Obligatorio").bind(CompraItem::getPrecioTotalCosto, CompraItem::setPrecioTotalCosto);
        itemBinder.forField(tfCostoU)
                .bind(CompraItem::getPrecioCosto, CompraItem::setPrecioCosto);
        itemBinder.forField(tfVenta)
                .withValidator(venta -> venta.compareTo(tfCostoU.getValue()) >= 0, "Precio de Venta MENOR o IGUAL que Precio de Costo")
                .asRequired("Obligatorio").bind(CompraItem::getPrecioVenta, CompraItem::setPrecioVenta);
        cantidadColumn.setEditorComponent(tfCantidad);
        costoTColumn.setEditorComponent(tfCostoTotal);
        ventaColumn.setEditorComponent(tfVenta);
        costoUColumn.setEditorComponent(tfCostoU);

        itemBinder.addValueChangeListener(event -> itemsGrid.getEditor().refresh());

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<CompraItem> editorColumn = itemsGrid
                .addComponentColumn(item -> {
                    Button edit = MyButton.MyButton("", new Icon(VaadinIcon.EDIT), "Editar",
                            ButtonVariant.LUMO_SUCCESS, false, true);
                    edit.addClickListener(e -> {
                        itemGridEditor.editItem(item);
                        tfCantidad.focus();
                    });
                    edit.setEnabled(!itemGridEditor.isOpen());
                    editButtons.add(edit);

                    Button delete = MyButton.MyButton("", new Icon(VaadinIcon.FILE_REMOVE), "Eliminar",
                            ButtonVariant.LUMO_ERROR, false, true);
                    delete.addClickListener(e -> {
                        if(listaItems.remove(item)) {
                            itemsGrid.setItems(listaItems);
                            enableButtonSave();
                            clearData();
                        } else {
                            NotificacionesUtil.showError("No Fue posible Eliminar Item de la tabla");
                        }
                    });
                    delete.setEnabled(!itemGridEditor.isOpen());
                    editButtons.add(delete);

                    return new HorizontalLayout(edit, delete);
                });

        itemGridEditor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!itemGridEditor.isOpen())));
        itemGridEditor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!itemGridEditor.isOpen())));

        btnUpdate = MyButton.MyButton("", new Icon(VaadinIcon.CHECK_CIRCLE),
                "Actualizar Producto en tabla", ButtonVariant.LUMO_SUCCESS, false, true);
        btnUpdate.addClickListener(evt -> itemGridEditor.save());

        Button btnCancel = MyButton.MyButton("", new Icon(VaadinIcon.CLOSE_CIRCLE),
                "Cancelar",ButtonVariant.LUMO_ERROR, false, true);
        btnCancel.addClickListener(e -> itemGridEditor.cancel());

        itemsGrid.getElement().addEventListener("keyup", event -> itemGridEditor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(btnUpdate, btnCancel);
        editorColumn.setEditorComponent(buttons);

        itemGridEditor.addSaveListener(evt -> {
            if(itemBinder.validate().isOk()) {
                CompraItem item = evt.getItem();
                if (listaItems.removeIf(i -> i.getProducto().getId().equals(item.getProducto().getId()))) {
                    listaItems.add(item);
                    itemsGrid.getEditor().cancel();
                    itemsGrid.getDataProvider().refreshAll();
                    clearData();
                } else {
                    NotificacionesUtil.showError("No fue posible actualizar Item en la tabla");
                }
            }
        });

        //itemGridEditor.addCancelListener(evt -> itemGridEditor.closeEditor());
    }

    public void setCompra(Compra compra, String title) {
        binder.setBean(compra);
        titleForm.setText(title);
        btnDelete.setEnabled(false);
        listaItems = new ArrayList<>();

        if(compra != null && compra.getItems() != null) {
            listaItems = compra.getItems();
            listaItems.forEach(item ->
                    item.setPrecioTotalCosto(item.getPrecioCosto().multiply(item.getCantidad())));
            btnDelete.setEnabled(true);
        }
        itemsGrid.setItems(listaItems);
        tfFactura.focus();
    }

    private void validateAndSave() {
        if(itemsGrid.getEditor().isOpen()) {
            itemsGrid.getEditor().save();
        }
        if (binder.validate().isOk() && listaItems.size() > 0) {
            Compra compra = binder.getBean();
            compra.setItems(listaItems);
            fireEvent(new SaveEvent(this, compra));
        } else {
            NotificacionesUtil.showError("Validar Compra: "+binder.validate().getValidationErrors());
        }
    }

    private void calculatedPriceCost() {
        if(tfCostoTotal.getOptionalValue().isPresent() &&
                tfCantidad.getOptionalValue().isPresent() &&
                tfCostoTotal.getValue().compareTo(BigDecimal.ZERO) > 0 &&
                tfCantidad.getValue().compareTo(BigDecimal.ZERO) > 0) {

            tfCostoU.setValue(tfCostoTotal.getValue().divide(tfCantidad.getValue(), 2, RoundingMode.HALF_UP));
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
