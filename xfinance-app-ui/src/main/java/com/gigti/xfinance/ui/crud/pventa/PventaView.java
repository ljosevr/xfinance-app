package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.IventaService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.math.BigDecimal;
import java.util.*;

import static com.gigti.xfinance.ui.util.NotificacionesUtil.getConfirmButton;

@Route(value = Constantes.VIEW_R_VENTA,layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_VENTA,layout = MainLayout.class)
public class PventaView extends HorizontalLayout {

    private boolean isModified;
    private PventaGrid grid;
    private ComboBox<PventaDTO> filter;
    private NumberField nfCantidad;
    private NumberField nfSubTotal;
    private H2 lblTotal;
    private PventaLogic viewLogic;
    private List<PventaDTO> lista;

    private Map<String,PventaDTO> mapItemsventa;
    private PventaDTO pventaDTO;
    private BigDecimal totalFactura;
    private FormLayout formActions;

    //@Autowired
    public PventaView(IventaService iventaService) {
        formActions = new FormLayout();
        formActions.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 4));

        viewLogic = new PventaLogic(iventaService,this);
        lista = viewLogic.findAll();
        createTopBar();

        grid = new PventaGrid();

        H3 title = new H3(Constantes.VIEW_PVENTA);
        title.setClassName("titleView");

        //private Set<PventaDTO> listaItemsVenta;
        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(formActions);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, formActions);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        grid.addItemClickListener(event ->{
            filter.setValue(event.getItem());
            filter.setEnabled(false);
            nfCantidad.setValue(event.getItem().getCantidadVenta());
            nfSubTotal.setValue(event.getItem().getCantidadVenta() * event.getItem().getPrecioVentaActual().doubleValue());
            pventaDTO = event.getItem();
            nfCantidad.focus();
            isModified = true;
        });

        add(barAndGridLayout);
    }

    private void createTopBar() {

        // TODO Precargar list de 50 0 100 Productos ?
        filter = new ComboBox<>("Producto");
        filter.setPlaceholder("Buscar por Nombre o Codigo Barras");
        filter.setItemLabelGenerator(PventaDTO::getNombreProducto);
        filter.setAllowCustomValue(true);
        filter.setPageSize(10);
        ComboBox.ItemFilter<PventaDTO> filter2 = (pventa, filterString) ->
                pventa.getNombreProducto().toLowerCase()
                        .contains(filterString.toLowerCase())
                        || pventa.getCodigoBarra().toLowerCase()
                        .contains(filterString.toLowerCase());

        filter.setItems(filter2, lista);
        //filter.addValueChangeMode(ValueChangeMode.ON_CHANGE);
        filter.addCustomValueSetListener(custom -> {
            if (!custom.getDetail().isBlank()) {
                //TODO Buscar resultados a BD
                NotificacionesUtil.openProgressBar("Buscando por Favor Espere", true, false);
            }
        });
        filter.addValueChangeListener(event -> {
                pventaDTO = event.getValue();
                if (pventaDTO != null) {
                    if(mapItemsventa == null) mapItemsventa = new HashMap<>();
                    nfCantidad.setEnabled(true);
                    nfCantidad.setValue(1.0);
                    nfSubTotal.setValue(pventaDTO.getPrecioVentaActual().doubleValue());
                    nfCantidad.focus();
                }
            }
        );
        filter.addFocusShortcut(Key.F10);
        filter.focus();

        nfCantidad = new NumberField("Cantidad");
        nfCantidad.setValue(0.0);
        nfCantidad.setAutoselect(true);
        nfCantidad.setEnabled(false);
        nfCantidad.setValueChangeMode(ValueChangeMode.EAGER);
        nfCantidad.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        nfCantidad.addKeyPressListener(Key.ENTER, listener -> valueHigherThanZero(nfCantidad.getValue()));

        nfSubTotal = new NumberField("SubTotal");
        nfSubTotal.setEnabled(false);
        nfSubTotal.setValue(0.0);
        nfSubTotal.setPrefixComponent(new Span("$"));
        nfSubTotal.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        totalFactura = BigDecimal.ZERO;

        lblTotal = new H2();
        lblTotal.setText("TOTAL: "+totalFactura);

        Button btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(listener -> viewLogic.save(mapItemsventa.values()));

        Button btnDelete = new Button("Borrar Item");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(listener -> deleteItemFromGrid());

        Button btnCancel = new Button("Cancelar Factura");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
        btnCancel.addClickListener(listener -> clearAll());

        isModified = false;
        formActions.add(filter, nfCantidad, nfSubTotal, lblTotal, btnSave, btnDelete, btnCancel);
    }

    private void deleteItemFromGrid() {
        Set<PventaDTO> selected = grid.getSelectionModel().getSelectedItems();
        if(selected.isEmpty()){
            Notification.show("Debes seleccionar un Item de la Factura", 4000, Notification.Position.BOTTOM_CENTER);
        } else {
            nfSubTotal.setValue(0d);
            nfCantidad.setValue(0d);
            mapItemsventa.remove(pventaDTO.getIdProducto());
            putInGrid(mapItemsventa);
            calculteTotalFactura();
            filter.focus();
        }
    }

    private void valueHigherThanZero(double value) {
        if(pventaDTO != null) {
            if (value > 0) {
                if((quantityAccumalted(pventaDTO) + value) <= pventaDTO.getInStock()) {
                    int item = mapItemsventa.size();
                    if (isModified) {
                        isModified = false;
                        if (pventaDTO == null) {
                            Notification.show("para modificar debe seleccionar un registro de la lista");
                        } else {
                            pventaDTO.setCantidadVenta(value);
                            mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                            putInGrid(mapItemsventa);
                        }
                    } else {
                        PventaDTO pv = mapItemsventa.get(pventaDTO.getIdProducto());
                        if (pv != null) {
                            //Existe
                            pv.setCantidadVenta(pv.getCantidadVenta() + value);
                            if (!mapItemsventa.replace(pv.getIdProducto(), pventaDTO, pv)) {
                                Notification.show("Error: No se pudo Asociar Arituclos");
                            }
                        } else {
                            //Nuevo
                            item++;
                            pventaDTO.setItem(item);
                            pventaDTO.setCantidadVenta(value);
                            mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                        }
                        putInGrid(mapItemsventa);
                    }
                    calculteTotalFactura();

                    filter.setValue(null);
                    nfCantidad.setValue(0.0);
                    nfCantidad.setEnabled(false);
                    nfSubTotal.setValue(0.0);
                    filter.setEnabled(true);
                    filter.focus();
                    pventaDTO = null;
                } else {
                    double temp = pventaDTO.getInStock() - quantityAccumalted(pventaDTO);
                    if(temp > 0) {
                        NotificacionesUtil.openConfirmationDialog("Solo existen " + pventaDTO.getInStock() + " en Stock, No puedes vender más de esa cantidad.\n" +
                                "¿Deseas Agregar " + temp + " a la Compra actual?", true, false);

                        Objects.requireNonNull(NotificacionesUtil.getDialogConfirmation()).addDialogCloseActionListener(
                                listener -> filter.focus());

                        getConfirmButton().focus();

                        getConfirmButton().addClickListener(event -> {
                            if(NotificacionesUtil.getDialogConfirmation().isOpened())
                                NotificacionesUtil.getDialogConfirmation().close();
                            valueHigherThanZero(temp);
                        });

                        NotificacionesUtil.getCancelButton().addClickListener(event -> {
                            if(NotificacionesUtil.getDialogConfirmation().isOpened())
                                NotificacionesUtil.getDialogConfirmation().close();
                        });
                    }else {
                        Notification.show("No existen inventario para este producto, No se puede vender. \nSi tiene 1 o más en " +
                                "mano contacta al administrador", 6000, Notification.Position.MIDDLE);
                        filter.setValue(null);
                        nfCantidad.setValue(0d);
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

    /**
     * quantityAccumulated is method that return cantidad de venta acumulada en la factura
     * @param pventa Objeto ProductoVentaDTO
     * @return retorna cantidad acumulada
     */
    private double quantityAccumalted(PventaDTO pventa) {
        return mapItemsventa
                .values()
                .stream()
                .filter(v -> v.getIdProducto().equals(pventa.getIdProducto()))
                .mapToDouble(PventaDTO::getCantidadVenta)
                .sum();
    }

    private void putInGrid(Map<String, PventaDTO> mapItemsventa) {
        List<PventaDTO> listaGrid = new ArrayList<>(mapItemsventa.values());
        grid.setItems(listaGrid);
    }

    private void calculteTotalFactura() {
        double subtotal = mapItemsventa.values()
                .stream()
                .mapToDouble(v -> v.getSubTotal().doubleValue())
                .sum();

        lblTotal.setText("TOTAL: "+subtotal);
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(PventaDTO row) {
        grid.getSelectionModel().select(row);
    }

    public void refresh(){
        lista = viewLogic.findAll();
        filter.setItems(lista);
        filter.focus();
    }

    public void refresh(PventaDTO pventaDTO){
        lista.add(pventaDTO);
        filter.setItems(lista);
    }

    public PventaGrid getGrid() {
        return grid;
    }

    public List<PventaDTO> getItemsGrid(){
        return lista;
    }

    public void clearAll() {
        grid.setItems(new ArrayList<>());
        filter.setValue(null);
        nfCantidad.setValue(0.0);
        nfSubTotal.setValue(0.0);
        mapItemsventa = null;
        totalFactura = BigDecimal.ZERO;
        lblTotal.setText("TOTAL: "+totalFactura);
        filter.focus();
    }
}