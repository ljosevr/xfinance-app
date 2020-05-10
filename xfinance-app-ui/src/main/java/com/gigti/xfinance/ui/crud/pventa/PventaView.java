package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.VentaService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.AllUtils;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.gigti.xfinance.ui.util.SearchFilterComponent;
import com.gigti.xfinance.ui.util.SearchProductByNameComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.math.BigDecimal;
import java.util.*;

import static com.gigti.xfinance.ui.util.NotificacionesUtil.getSiButton;

@Route(value = Constantes.VIEW_R_VENTA, layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_VENTA, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_PVENTA +" | "+ Constantes.VIEW_MAIN)
public class PventaView extends VerticalLayout {

    private boolean isModified;
    private PventaGrid grid;
    private NumberField nfCantidad, nfSubTotal, nfImpuestos;
    private TextField tfNombreProducto;
    private H2 lblTotal;

    private Map<String, PventaDTO> mapItemsventa;
    private PventaDTO pventaDTO;
    private BigDecimal totalFactura;
    private VerticalLayout dataLayout;
    private SearchProductByNameComponent searchLayout;
    private VentaService ventaService;
    private TextField filter;
    private Button btnDelete;
    private Button btnSave;
    private Page page;

    public PventaView(VentaService ventaService) {
        this.ventaService = ventaService;
        Empresa empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
        addClassName("PventaView");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.START);

        dataLayout = new VerticalLayout();
        dataLayout.addClassName("rightLayout");

        H3 title = new H3(Constantes.VIEW_PVENTA);
        title.addClassName("titleView2");
        this.add(title);

        configureTopBar();
        configureDataLayout();
        confgureSearchLayout();

        grid = new PventaGrid();
        grid.addClassName("grid");
        Div divLayout = new Div();
        divLayout.addClassName("content");
        divLayout.setSizeFull();

        divLayout.add(grid, dataLayout, searchLayout);

        grid.addItemClickListener(event -> {
            nfCantidad.setValue(event.getItem().getCantidadVenta().doubleValue());
            nfSubTotal.setValue(event.getItem().getCantidadVenta().multiply(event.getItem().getPrecioVentaActual()).doubleValue());
            nfImpuestos.setValue(AllUtils.percentage(event.getItem().getPrecioVentaActual(), event.getItem().getImpuestoValor()).doubleValue());
            pventaDTO = event.getItem();
            nfCantidad.focus();
            isModified = true;
            btnDelete.setEnabled(true);
            nfCantidad.setReadOnly(false);
        });

        add(divLayout);
        showSearch(false);
    }

    private void showSearch(boolean show) {
        searchLayout.setVisible(show);
        if(show) {
            searchLayout.clear();
            searchLayout.getFilter().focus();
        }
        dataLayout.setVisible(!show);
    }

    private void configureTopBar() {

        SearchFilterComponent topBarLayout = new SearchFilterComponent("Nuevo", "", "Filtro por Codigo de Barras del Producto", true, false);
        filter = topBarLayout.getFilter();
        filter.focus();
        topBarLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
        topBarLayout.getBtnSearch().addClickListener(click -> showSearch(true));
        this.add(topBarLayout);

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

        this.add(new HorizontalLayout(btnSave, btnDelete, btnCancel));
    }

    private void confgureSearchLayout() {
        searchLayout = new SearchProductByNameComponent(ventaService, "", "Buscar por Nombre del Producto");
        searchLayout.addClassName("rightLayout");

        searchLayout.getFilter().addKeyPressListener(Key.ENTER, listener -> {
            if(!searchLayout.getListData().isEmpty()){
                PventaDTO item = searchLayout.getListData().get(0);
                NotificacionesUtil.openConfirmationDialog("Desea Seleccionar el Producto: "+item.getNombreProducto(), true, false);
                NotificacionesUtil.getSiButton().addClickListener(event -> {
                    if(NotificacionesUtil.getDialog().isOpened())
                        NotificacionesUtil.getDialog().close();
                    selectedItem(item);
                });
                NotificacionesUtil.getNoButton().addClickListener(event -> {
                    if(NotificacionesUtil.getDialog().isOpened())
                        NotificacionesUtil.getDialog().close();
                    searchLayout.getFilter().focus();
                });
            }
        });
        searchLayout.getFilter().addKeyPressListener(Key.ESCAPE, listener -> showSearch(false));
        searchLayout.getBtnClose().addClickListener(listner -> showSearch(false));

        configMobile();
    }

    private void configMobile() {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            if(details.getWindowInnerWidth() < 600) {
                searchLayout.getGrid().addItemClickListener(click -> {
                    pventaDTO = click.getItem();
                    selectedItem(pventaDTO);
                });
            } else {
                searchLayout.getGrid().addItemDoubleClickListener(click -> {
                    pventaDTO = click.getItem();
                    selectedItem(pventaDTO);
                });
            }
        });
    }

    private void configureDataLayout() {

        nfCantidad = new NumberField("Cantidad");
        nfCantidad.setValue(0.0);
        nfCantidad.setAutoselect(true);
        nfCantidad.setReadOnly(true);
        nfCantidad.setValueChangeMode(ValueChangeMode.EAGER);
        nfCantidad.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER, TextFieldVariant.LUMO_SMALL);
        nfCantidad.addKeyPressListener(Key.ENTER, listener -> addItem(nfCantidad.getValue()));

        nfSubTotal = new NumberField("SubTotal");
        nfSubTotal.setReadOnly(true);
        nfSubTotal.setValue(0.0);
        nfSubTotal.setPrefixComponent(new Span("$"));
        nfSubTotal.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT, TextFieldVariant.LUMO_SMALL);

        nfImpuestos = new NumberField("Impuestos");
        nfImpuestos.setReadOnly(true);
        nfImpuestos.setValue(0.0);
        nfImpuestos.setPrefixComponent(new Span("$"));
        nfImpuestos.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT, TextFieldVariant.LUMO_SMALL);

//        nfDescuentos = new NumberField("Descuento");
//        nfDescuentos.setReadOnly(true);
//        nfDescuentos.setValue(0.0);
//        nfDescuentos.setPrefixComponent(new Span("$"));
//        nfDescuentos.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT, TextFieldVariant.LUMO_SMALL);

        totalFactura = BigDecimal.ZERO;

        lblTotal = new H2();
        lblTotal.setText("TOTAL: " + totalFactura);

        tfNombreProducto = new TextField("Producto");
        tfNombreProducto.setReadOnly(true);
        tfNombreProducto.setValue("");
        tfNombreProducto.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);

        isModified = false;
        dataLayout.add(tfNombreProducto, new HorizontalLayout(nfCantidad, nfSubTotal), new HorizontalLayout(nfImpuestos), lblTotal);
    }

    private void selectedItem(PventaDTO pventaDTO) {
        if (pventaDTO != null) {
            showSearch(false);
            if (mapItemsventa == null) mapItemsventa = new HashMap<>();
            nfCantidad.setReadOnly(false);
            nfCantidad.setValue(1.0);
            nfSubTotal.setValue(pventaDTO.getPrecioVentaActual().doubleValue());
            nfImpuestos.setValue(AllUtils.percentage(pventaDTO.getPrecioVentaActual(), pventaDTO.getImpuestoValor()).doubleValue());
            nfCantidad.focus();
            tfNombreProducto.setValue(pventaDTO.getNombreProducto());
            this.pventaDTO = pventaDTO;
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
    private void addItem(double value) {
        if (pventaDTO != null) {
            if (value > 0) {
                if ((quantityAccumalted(pventaDTO) + value) <= pventaDTO.getInStock().doubleValue() || pventaDTO.isInfinite()) {
                    int item = mapItemsventa.size();
                    if (isModified) {
                        isModified = false;
                        if (pventaDTO == null) {
                            NotificacionesUtil.showWarn("para modificar debe seleccionar un registro de la lista");
                        } else {
                            pventaDTO.setCantidadVenta(BigDecimal.valueOf(value));
                            mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                            putInGrid(mapItemsventa);
                            btnSave.setEnabled(true);
                        }
                    } else {
                        PventaDTO pv = mapItemsventa.get(pventaDTO.getIdProducto());
                        if (pv != null) {
                            //Existe
                            pv.setCantidadVenta(pv.getCantidadVenta().add(BigDecimal.valueOf(value)));
                            if (!mapItemsventa.replace(pv.getIdProducto(), pventaDTO, pv)) {
                                NotificacionesUtil.showError("Error: No se pudo Asociar Articulo");
                            }
                        } else {
                            //Nuevo
                            item++;
                            pventaDTO.setItem(item);
                            pventaDTO.setCantidadVenta(BigDecimal.valueOf(value));
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
                    double temp = pventaDTO.getInStock().doubleValue() - quantityAccumalted(pventaDTO);
                    if (temp > 0) {
                        NotificacionesUtil.openConfirmationDialog("Solo existen " + pventaDTO.getInStock() + " en Stock, No puedes vender más de esa cantidad.\n" +
                                "¿Deseas Agregar " + temp + " a la Compra actual?", true, false);

                        Objects.requireNonNull(NotificacionesUtil.getDialog()).addDialogCloseActionListener(
                                listener -> filter.focus());

                        getSiButton().focus();

                        getSiButton().addClickListener(event -> {
                            if (NotificacionesUtil.getDialog().isOpened())
                                NotificacionesUtil.getDialog().close();
                            addItem(temp);
                        });

                        NotificacionesUtil.getNoButton().addClickListener(event -> {
                            if (NotificacionesUtil.getDialog().isOpened())
                                NotificacionesUtil.getDialog().close();
                        });
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
        if (mapItemsventa.isEmpty()) {
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
        }
    }

    /**
     * quantityAccumulated is method that return cantidad de venta acumulada en la factura
     *
     * @param pventa Objeto ProductoVentaDTO
     * @return retorna cantidad acumulada
     */
    private double quantityAccumalted(PventaDTO pventa) {
        return mapItemsventa
                .values()
                .stream()
                .filter(v -> v.getIdProducto().equals(pventa.getIdProducto()))
                .mapToDouble(venta -> venta.getCantidadVenta().doubleValue())
                .sum();
    }

    private void putInGrid(Map<String, PventaDTO> mapItemsventa) {
        grid.setItems(mapItemsventa.values());
    }

    private void calculteTotalFactura() {
        double subtotal = mapItemsventa.values()
                .stream()
                .mapToDouble(v -> v.getSubTotal().doubleValue())
                .sum();

        lblTotal.setText("TOTAL: " + AllUtils.numberFormat(BigDecimal.valueOf(subtotal)));
    }

    public PventaGrid getGrid() {
        return grid;
    }

    public void clearAll() {
        grid.setItems(new ArrayList<>());
        clearFormData();
        mapItemsventa = null;
        totalFactura = BigDecimal.ZERO;
        lblTotal.setText("TOTAL: " + totalFactura);
        filter.focus();
    }

    private void clearFormData(){
        nfCantidad.setValue(0.0);
        nfSubTotal.setValue(0.0);
        tfNombreProducto.setValue("");
        nfImpuestos.setValue(0.0);
        nfCantidad.setReadOnly(true);
    }
}
