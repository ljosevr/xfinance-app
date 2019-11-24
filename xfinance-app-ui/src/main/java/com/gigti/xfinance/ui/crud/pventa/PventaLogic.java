package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Factura;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.backend.services.IventaService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.categoria.CategoriaView;
import com.vaadin.flow.component.notification.Notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PventaLogic implements Serializable {

    private PventaView view;
    private IventaService iventaService;
    private static Empresa empresa;
    private String filterText = "";

    public PventaLogic(IventaService iventaService, PventaView simpleCrudView) {
        this.iventaService = iventaService;
        view = simpleCrudView;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public List<PventaDTO> findAll() {
        return iventaService.find100MostImportant(empresa);
    }

    public void save(Collection<PventaDTO> items) {
        Factura factura = iventaService.registrarFactura(CurrentUser.get(), new ArrayList<>(items));
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
