package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Factura;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.services.VentaService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.notification.Notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PventaLogic implements Serializable {

    private PventaView view;
    private VentaService ventaService;
    private static Empresa empresa;
    private String filterText = "";

    public PventaLogic(VentaService ventaService, PventaView simpleCrudView) {
        this.ventaService = ventaService;
        view = simpleCrudView;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public List<PventaDTO> findAll() {
        return ventaService.find100MostImportant(empresa);
    }

    public void save(Collection<PventaDTO> items) {
        Factura factura = ventaService.registrarFactura(CurrentUser.get(), new ArrayList<>(items));
        if(factura == null){
            Notification.show("Error al generar factura");
        } else {
            Notification.show("Factura Generada: "+factura.getNumeroFactura());
            view.refresh();
            view.clearAll();
        }
    }

//    public List<PventaDTO> findByNombreOrCodigoBarra() {
//        return iventaService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
//    }
}
