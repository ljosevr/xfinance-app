package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.IventaService;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = Constantes.VIEW_R_VENTA,layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_VENTA,layout = MainLayout.class)

public class PventaView extends HorizontalLayout {

    private boolean isModified;
    private PventaGrid grid;
    private ComboBox<PventaDTO> filter;
    private NumberField nfCantidad;
    private NumberField nfSubTotal;
    private H2 lblTotal;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCancel;
    private PventaLogic viewLogic;
    private List<PventaDTO> lista;

    private Map<String,PventaDTO> mapItemsventa;
    //private Set<PventaDTO> listaItemsVenta;
    private VerticalLayout barAndGridLayout;
    private PventaDTO pventaDTO;
    private BigDecimal totalFactura;
    private FormLayout formActions;

    @Autowired
    public PventaView(IventaService iventaService) {
        formActions = new FormLayout();
        formActions.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 4));

        viewLogic = new PventaLogic(iventaService,this);
        lista = viewLogic.findAll();
        //setSizeFull();
        //HorizontalLayout topLayout = createTopBar();
        createTopBar();

        grid = new PventaGrid();
        //listaItemsVenta = new HashSet<>();

        H3 title = new H3(Constantes.VIEW_PVENTA);
        title.setClassName("titleView");

        barAndGridLayout = new VerticalLayout();
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

    public HorizontalLayout createTopBar() {

        // TODO Precargar list de 50 0 100 Productos ?
        // TODO Cambiar por lista Desplegable a Buscar
        filter = new ComboBox<>("Producto");
        filter.setPlaceholder("Buscar por Nombre o Codigo Barras");
        filter.setItemLabelGenerator(PventaDTO::getNombreProducto);
        filter.setAllowCustomValue(true);
        filter.setItems(lista);
        filter.setPageSize(10);
        //filter.addValueChangeMode(ValueChangeMode.ON_CHANGE);
        filter.addCustomValueSetListener(custom -> {
            if (!custom.getDetail().isBlank()) {
                //TODO Buscar resultados a BD
                Notification.show("Buscando por favor espere");
                ProgressBar progressBar = new ProgressBar();
                progressBar.setMin(1);
                progressBar.setMin(10);
                progressBar.setValue(1);
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
        nfCantidad.setValueChangeMode(ValueChangeMode.EAGER);
        nfCantidad.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        nfCantidad.addKeyPressListener(Key.ENTER, listener ->{
            valueHigherThanZero();
        });

        nfSubTotal = new NumberField("SubTotal");
        nfSubTotal.setEnabled(false);
        nfSubTotal.setValue(0.0);
        nfSubTotal.setPrefixComponent(new Span("$"));
        nfSubTotal.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        totalFactura = BigDecimal.ZERO;

        lblTotal = new H2();
        lblTotal.setText("TOTAL: "+totalFactura);

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);

        btnDelete = new Button("Borrar Item");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

        btnCancel = new Button("Cancelar Factura");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

        //TopBarComponent layout = new TopBarComponent();
        //layout.add(filter, nfCantidad, nfSubTotal, lblTotal, btnSave, btnDelete,btnCancel);
        isModified = false;
        formActions.add(filter, nfCantidad, nfSubTotal, lblTotal, btnSave, btnDelete, btnCancel);
        return null;
    }

    private void valueHigherThanZero() {
        if(pventaDTO != null) {
            if (nfCantidad.getValue() > 0) {
                int item = mapItemsventa.size();
                if (isModified) {
                    isModified = false;
                    if(pventaDTO == null){
                        Notification.show("para modificar debe seleccionar un registro de la lista");
                    } else {
                        pventaDTO.setCantidadVenta(nfCantidad.getValue());
                        mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                        putInGrid(mapItemsventa);
                    }
                } else {
                    PventaDTO pv = mapItemsventa.get(pventaDTO.getIdProducto());
                    if(pv != null) {
                        //Existe
                        pv.setCantidadVenta(pv.getCantidadVenta() + nfCantidad.getValue());
                        if(!mapItemsventa.replace(pv.getIdProducto(), pventaDTO, pv)){
                            Notification.show("Error: No se pudo Asociar Arituclos");
                        }
                    } else {
                        //Nuevo
                        item++;
                        pventaDTO.setItem(item);
                        pventaDTO.setCantidadVenta(nfCantidad.getValue());
                        mapItemsventa.put(pventaDTO.getIdProducto(), pventaDTO);
                    }
                    putInGrid(mapItemsventa);
                }

                calculteTotalFactura();

                filter.setValue(null);
                nfCantidad.setValue(0.0);
                nfSubTotal.setValue(0.0);
                filter.setEnabled(true);
                filter.focus();
                pventaDTO = null;
            } else {
                nfCantidad.focus();
            }
        } else {
            filter.focus();
        }
    }

    private void putInGrid(Map<String, PventaDTO> mapItemsventa) {
        List<PventaDTO> listaGrid = new ArrayList<>();
        for(String id : mapItemsventa.keySet()){
            PventaDTO pv = mapItemsventa.get(id);
            listaGrid.add(pv);
        }
        grid.setItems(listaGrid);
    }

    private void calculteTotalFactura() {

        //TODO recalcular mejor con la lista
        double subtotal = 0;

        for(String key : mapItemsventa.keySet()) {
            PventaDTO pv = mapItemsventa.get(key);
            subtotal += pv.getCantidadVenta() * pv.getPrecioVentaActual().doubleValue();
        }

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
        if(lista == null){
            lista = viewLogic.findAll();
        }

        //grid.setItems(lista);
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

}
