package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.data.enums.TipoBusquedaEnum;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.VentaService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.AllUtils;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.math.BigDecimal;
import java.util.*;

@Route(value = Constantes.VIEW_R_VENTA, layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_VENTA, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_PVENTA +" | "+ Constantes.VIEW_MAIN)
public class PventaView extends VerticalLayout {

    private boolean isModified;
    private final PventaGrid grid;
    private BigDecimalField nfCantidad, nfSubTotal, nfImpuestos;
    private TextField tfNombreProducto;
    private H2 lblTotal;
    private H3 lblImpuesto;
    private Map<String, PventaDTO> mapItemsventa;
    private PventaDTO pventaDTO;
    private final VerticalLayout dataLayout;
    private final VentaService ventaService;
    private TextField filter;
    private Button btnDelete;
    private Button btnSave;
    private ComboBox<TipoBusquedaEnum> cbTipoBusqueda;
    private final Empresa empresa;
    private Grid<PventaDTO> gridPro;
    private VerticalLayout gridProLayout;

    public PventaView(VentaService ventaService) {
        this.ventaService = ventaService;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getPersona().getEmpresa() : null;
        addClassName("PventaView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        dataLayout = new VerticalLayout();
        dataLayout.addClassName("rightLayout");

        H1 title = new H1(Constantes.VIEW_REGISTRAR.toUpperCase());
        title.addClassName("titleView2");
        this.add(title);

        configureTopBar();
        configureDataLayout();
        configureSearchGrid();

        grid = new PventaGrid();
        grid.addClassName("grid");

        Div divLayout = new Div();
        divLayout.addClassName("content");
        divLayout.setSizeFull();

        divLayout.add(grid, gridProLayout, dataLayout);

        grid.addItemClickListener(event -> {
            nfCantidad.setValue(event.getItem().getCantidadVenta());
            nfSubTotal.setValue(event.getItem().getCantidadVenta().multiply(event.getItem().getPrecioVentaActual()));
            nfImpuestos.setValue(AllUtils.percentage(event.getItem().getPrecioVentaActual(), event.getItem().getImpuestoValor()));
            pventaDTO = event.getItem();
            nfCantidad.focus();
            isModified = true;
            btnDelete.setEnabled(true);
            nfCantidad.setReadOnly(false);
        });

        add(divLayout);
    }

    private void search() {

        if(cbTipoBusqueda.getValue().equals(TipoBusquedaEnum.CODIGO)) {
            //Busqueda por Codigo de Barras
            selectedItem(ventaService.findByBarCode(filter.getValue(), empresa));
        } else {
            //Busqueda por Nombre
            List<PventaDTO> listResult = ventaService.findByName(filter.getValue(), empresa, 0, 10);
            gridPro.setItems(listResult);
            if(!listResult.isEmpty()) {
                //Ocultar Tabla principal
                gridProLayout.setVisible(true);
                //MOstrar tabla de busqueda
                grid.setVisible(false);
            }

        }
    }

    private void configureTopBar() {

        VerticalLayout topBarLayout = new VerticalLayout();
        topBarLayout.setDefaultHorizontalComponentAlignment(Alignment.START);

        FormLayout topFormLayout = new FormLayout();
        topFormLayout.addClassName("searchBar");
        //topBarLayout.addClassName("form");
        topFormLayout.setResponsiveSteps(MyResponsiveStep.getMyList());

        filter = new TextField();
        filter.setPlaceholder("Buscar por Código de Barras");
        filter.setPrefixComponent(new Icon(VaadinIcon.BARCODE));
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.addFocusShortcut(Key.F3);
        filter.setMaxWidth("70%");
        filter.addKeyPressListener(Key.ENTER, evt -> search());

        cbTipoBusqueda = new ComboBox<>();
        cbTipoBusqueda.setItems(TipoBusquedaEnum.values());
        cbTipoBusqueda.setValue(TipoBusquedaEnum.CODIGO);
        cbTipoBusqueda.setAllowCustomValue(false);
        cbTipoBusqueda.setMaxWidth("160px");
        cbTipoBusqueda.addValueChangeListener(evt -> {
           if(evt.getValue().name().equalsIgnoreCase(TipoBusquedaEnum.CODIGO.name())){
               filter.setPrefixComponent(new Icon(VaadinIcon.BARCODE));
               filter.setPlaceholder("Buscar Por Código de Barras");
               filter.focus();
           }
           if(evt.getValue().name().equalsIgnoreCase(TipoBusquedaEnum.NOMBRE.name())){
               filter.setPrefixComponent(new Icon(VaadinIcon.BOOK));
               filter.setPlaceholder("Buscar Por Nombre del producto");
               filter.focus();
           }
        });

        Button btnSearch = new Button("", new Icon(VaadinIcon.SEARCH));
        btnSearch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSearch.setVisible(true);
        btnSearch.addClickShortcut(Key.F4);
        btnSearch.addClickListener(click -> search());
        btnSearch.setMaxWidth("60px");

//        HorizontalLayout hLayout = new HorizontalLayout(filter, cbTipoBusqueda, btnSearch);
//        hLayout.setSizeFull();
//        hLayout.expand(filter);
        //topFormLayout.add(hLayout);
        topFormLayout.add(filter, cbTipoBusqueda, btnSearch);


        filter.focus();

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(listener -> saveSell(mapItemsventa.values()));
        btnSave.setEnabled(false);

        btnDelete = new Button("Borrar Item");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(listener -> deleteItemFromGrid());
        btnDelete.setEnabled(false);

        Button btnCancel = new Button("Cancelar Factura");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnCancel.addClickListener(listener -> clearAll());

        topBarLayout.add(topFormLayout);
        topBarLayout.add(new HorizontalLayout(btnSave, btnDelete, btnCancel));
        this.add(topBarLayout);
    }

    private void configureSearchGrid() {

        gridProLayout = new VerticalLayout();
        gridProLayout.addClassName("gridPro");
        gridProLayout.setVisible(false);

        gridPro = new Grid<>(PventaDTO.class);
        gridPro.setColumns("nombreProducto", "unidadMedida");

        Button btnSelect = new Button("Seleccionar");
        btnSelect.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        btnSelect.addClickListener(evt -> {
            if(gridPro.getSelectionModel().getFirstSelectedItem().isPresent()){
                selectedItem(gridPro.getSelectionModel().getFirstSelectedItem().get());
            } else {
                NotificacionesUtil.showError("Debe seleccionar 1 Item de la Tabla");
            }
        });

        gridPro.addItemDoubleClickListener(evt -> selectedItem(evt.getItem()));
        gridProLayout.add(gridPro, btnSelect);

    }

    private void configureDataLayout() {

        nfCantidad = new BigDecimalField("Cantidad");
        nfCantidad.setValue(BigDecimal.valueOf(0.0));
        nfCantidad.setAutoselect(true);
        nfCantidad.setReadOnly(true);
        nfCantidad.setValueChangeMode(ValueChangeMode.EAGER);
        nfCantidad.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER, TextFieldVariant.LUMO_SMALL);
        nfCantidad.addKeyPressListener(Key.ENTER, listener -> addItem(nfCantidad.getValue()));

        nfSubTotal = new BigDecimalField("SubTotal");
        nfSubTotal.setReadOnly(true);
        nfSubTotal.setValue(BigDecimal.valueOf(0.0));
        nfSubTotal.setPrefixComponent(new Span("$"));
        nfSubTotal.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT, TextFieldVariant.LUMO_SMALL);

        nfImpuestos = new BigDecimalField("Impuestos");
        nfImpuestos.setReadOnly(true);
        nfImpuestos.setValue(BigDecimal.valueOf(0.0));
        nfImpuestos.setPrefixComponent(new Span("$"));
        nfImpuestos.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT, TextFieldVariant.LUMO_SMALL);

        Button btnAgregar = new Button("Agregar");
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        btnAgregar.addClickListener(evt -> addItem(nfCantidad.getValue()));
//        nfDescuentos = new NumberField("Descuento");
//        nfDescuentos.setReadOnly(true);
//        nfDescuentos.setValue(0.0);
//        nfDescuentos.setPrefixComponent(new Span("$"));
//        nfDescuentos.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT, TextFieldVariant.LUMO_SMALL);

        lblTotal = new H2();
        lblTotal.setText("TOTAL: " + BigDecimal.ZERO);

        lblImpuesto = new H3();
        lblImpuesto.setText("Impuestos: " + BigDecimal.ZERO);

        tfNombreProducto = new TextField("Producto");
        tfNombreProducto.setReadOnly(true);
        tfNombreProducto.setValue("");
        tfNombreProducto.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);

        HorizontalLayout hlayout = new HorizontalLayout(nfImpuestos, btnAgregar);
        hlayout.setDefaultVerticalComponentAlignment(Alignment.END);

        isModified = false;
        dataLayout.add(tfNombreProducto,
                new HorizontalLayout(nfCantidad, nfSubTotal),
                hlayout,
                lblImpuesto, lblTotal);
    }

    private void selectedItem(PventaDTO pventaDTO) {
        if (pventaDTO != null) {
            if (mapItemsventa == null) mapItemsventa = new HashMap<>();
            nfCantidad.setReadOnly(false);
            nfCantidad.setValue(BigDecimal.valueOf(1.0));
            nfSubTotal.setValue(pventaDTO.getPrecioVentaActual());
            nfImpuestos.setValue(AllUtils.percentage(pventaDTO.getPrecioVentaActual(), pventaDTO.getImpuestoValor()));
            nfCantidad.focus();
            tfNombreProducto.setValue(pventaDTO.getNombreProducto());
            this.pventaDTO = pventaDTO;
            gridProLayout.setVisible(false);
            grid.setVisible(true);
        }
    }


    private void saveSell(Collection<PventaDTO> items) {
        Venta venta = ventaService.registrarVenta(CurrentUser.get(), new ArrayList<>(items));
        if (venta == null) {
            NotificacionesUtil.showError("Error al generar factura");
        } else {
            NotificacionesUtil.showSuccess("Factura Generada: " + venta.getNumeroFactura());
            clearAll();
        }
    }

    private void deleteItemFromGrid() {
        Set<PventaDTO> selected = grid.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            NotificacionesUtil.showWarn("Debes seleccionar un Item de la Factura");
        } else {
            mapItemsventa.remove(pventaDTO.getIdProducto());
            putInGrid(mapItemsventa);
            clearFormData();
            btnDelete.setEnabled(false);
            grid.getSelectionModel().deselectAll();
            calculteTotalFactura();
            activeBtnSave();
            filter.focus();
        }
    }

    /**
     * Metodo para agregar un Item a la tabla
     * @param value
     */
    private void addItem(BigDecimal value) {
        if (pventaDTO != null) {
            if (value.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal accumulatedQ = accumulatedQuantity(pventaDTO);
                if ((value.add(accumulatedQ)).compareTo(pventaDTO.getInStock()) <= 0 || pventaDTO.isInfinite()) {
                    if (isModified) {
                        isModified = false;
                        if (pventaDTO == null) {
                            NotificacionesUtil.showWarn("para modificar debe seleccionar un registro de la lista");
                        } else {
                            pventaDTO.setCantidadVenta(value);
                            mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                            putInGrid(mapItemsventa);
                            btnSave.setEnabled(true);
                        }
                    } else {
                        PventaDTO pv = mapItemsventa.get(pventaDTO.getIdProducto());
                        if (pv != null) {
                            //Existe
                            if (mapItemsventa.remove(pv.getIdProducto(), pv)) {
                                pv.setCantidadVenta(accumulatedQ.add(value));
                                mapItemsventa.put(pv.getIdProducto(), pv);
                            } else {
                                NotificacionesUtil.showError("Error: No se pudo Asociar Articulo");
                            }
                        } else {
                            //Nuevo
                            pventaDTO.setCantidadVenta(value);
                            mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                        }
                        putInGrid(mapItemsventa);
                    }
                    calculteTotalFactura();

                    clearFormData();
                    filter.focus();
                    pventaDTO = null;
                    activeBtnSave();
                } else {
                    BigDecimal acumulado = accumulatedQuantity(pventaDTO);
                    BigDecimal temp = pventaDTO.getInStock().subtract(acumulado);
                    if (temp.compareTo(BigDecimal.ZERO) > 0) {
                        String acumuladoStr = "";
                        if(acumulado.compareTo(BigDecimal.ZERO) > 0) {
                            acumuladoStr = "Ya se agregaron "+acumulado + " en la venta \n";
                        }
                        NotificacionesUtil.showError("Solo existen " + pventaDTO.getInStock() + " en Stock, " +
                                "No puedes vender más de esa cantidad.\n" +
                                acumuladoStr +
                                "Debe solo agregar " + temp + " a la Compra actual.\n" +
                                "Por favor contacta al Administrador");
                    } else {
                        NotificacionesUtil.showWarn("No existen inventario para este producto, No se puede vender. \nSi tiene 1 o más en " +
                                "mano contacta al administrador");
                        clearFormData();
                        filter.focus();
                    }
                }
            } else {
                nfCantidad.focus();
            }
        } else {
            filter.focus();
        }
    }

    private void activeBtnSave() {
        btnSave.setEnabled(!mapItemsventa.isEmpty());
    }

    /**
     * quantityAccumulated is method that return cantidad de venta acumulada en la factura
     *
     * @param pventa Objeto ProductoVentaDTO
     * @return retorna cantidad acumulada
     */
    private BigDecimal accumulatedQuantity(PventaDTO pventa) {
        BigDecimal result = BigDecimal.ZERO;
        for(PventaDTO pventa1 : mapItemsventa.values()){
            if(pventa1.getIdProducto().equals(pventa.getIdProducto())) {
                result = result.add(pventa1.getCantidadVenta());
            }
        }

        return result;
    }

    private void putInGrid(Map<String, PventaDTO> mapItemsventa) {
        grid.setItems(mapItemsventa.values());
    }

    private void calculteTotalFactura() {

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal subImpuesto = BigDecimal.ZERO;

        for(PventaDTO pv : mapItemsventa.values()) {
            subtotal = subtotal.add(pv.getSubTotal());
            subImpuesto = subImpuesto.add(pv.getSubImpuesto());
        }

        lblTotal.setText(String.format("TOTAL: %s", AllUtils.numberFormat(subtotal)));
        lblImpuesto.setText(String.format("Impuestos: %s", AllUtils.numberFormat(subImpuesto)));
    }

    public PventaGrid getGrid() {
        return grid;
    }

    public void clearAll() {
        grid.setItems(new ArrayList<>());
        clearFormData();
        mapItemsventa = null;
        lblTotal.setText(String.format("TOTAL: %s", AllUtils.numberFormat(BigDecimal.ZERO)));
        lblImpuesto.setText(String.format("Impuestos: %s", AllUtils.numberFormat(BigDecimal.ZERO)));
        filter.setValue("");
        cbTipoBusqueda.setValue(TipoBusquedaEnum.CODIGO);
        filter.focus();
    }

    private void clearFormData(){
        nfCantidad.setValue(BigDecimal.valueOf(0.0));
        nfSubTotal.setValue(BigDecimal.valueOf(0.0));
        tfNombreProducto.setValue("");
        nfImpuestos.setValue(BigDecimal.valueOf(0.0));
        nfCantidad.setReadOnly(true);
    }
}
