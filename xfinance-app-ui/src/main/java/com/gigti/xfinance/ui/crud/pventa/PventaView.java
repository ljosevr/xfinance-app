package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaCrudLogic;
import com.gigti.xfinance.ui.util.TopBarComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = Constantes.VIEW_R_VENTA,layout = MainLayout.class)
@RouteAlias(value = Constantes.VIEW_R_VENTA,layout = MainLayout.class)

public class PventaView extends HorizontalLayout {

    private PventaGrid grid;
    private ComboBox<PventaDTO> filter;
    private PventaLogic viewLogic;
    private List<PventaDTO> lista;
    private VerticalLayout barAndGridLayout;

    @Autowired
    public PventaView() {
        //viewLogic = new CategoriaCrudLogic(iService,this);

        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new PventaGrid();
        //lista = viewLogic.findAll();
        grid.setItems(lista);
        //grid.asSingleSelect().addValueChangeListener(
          //      event -> viewLogic.rowSelected(event.getValue()));

        H3 title = new H3(Constantes.VIEW_PVENTA);
        title.setClassName("titleView");

        barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(title);

        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);

        //viewLogic.init();
    }

    public HorizontalLayout createTopBar() {

        // TODO Precargar list de 50 0 100 Productos ?
        // TODO Cambiar por lista Desplegable a Buscar
        filter = new ComboBox<>();
        filter.setPlaceholder("Buscar por Nombre o Codigo Barras");
        filter.setItemLabelGenerator(PventaDTO::getNombreProducto);
        filter.setItems();
        //filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(event -> {
                    PventaDTO pventaDTO = filter.getOptionalValue().orElse(null);
                    if (pventaDTO != null) {
                        lista.add(pventaDTO);
                    } else {
                        //TODO Buscar resultados a BD
                    }
                }
        );
        filter.addFocusShortcut(Key.F10);

        return new TopBarComponent(filter);
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
        //lista = viewLogic.findAll();
        grid.setItems(lista);
    }

    public void refresh(PventaDTO pventaDTO){
        lista.add(pventaDTO);
        grid.setItems(lista);
        grid.refresh(pventaDTO);
    }

    public PventaGrid getGrid() {
        return grid;
    }

    public List<PventaDTO> getItemsGrid(){
        return lista;
    }

}
