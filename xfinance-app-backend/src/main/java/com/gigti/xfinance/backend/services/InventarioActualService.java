package com.gigti.xfinance.backend.services;

import java.util.List;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.Response;

import org.vaadin.data.spring.OffsetBasedPageRequest;

public interface InventarioActualService {

     /**
     * Metodo para buscar el Inventario Actual de los productos
     * @param filterText
     * @param empresa
     * @param offsetBasedPageRequest
     * @return
     */
    List<InventarioActual> findInvActual(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);
    
    int countInvActual(String filterText, Empresa empresa);

    Response generateReportInvActual(String filterText, Empresa empresa, String formatType);

    InventarioActual findInvActualByProducto(Producto producto);
}
