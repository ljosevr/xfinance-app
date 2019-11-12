package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.backend.services.IventaService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.categoria.CategoriaView;

import java.io.Serializable;
import java.util.List;

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

//    public List<PventaDTO> findByNombreOrCodigoBarra() {
//        return iventaService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
//    }
}
