package com.gigti.xfinance.backend.services;

import java.util.List;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;

import org.vaadin.data.spring.OffsetBasedPageRequest;

public interface InventarioInicialService {

    List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);

    List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, int offSet, int limit);

    List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa);

    Response processInventarioInicial(InventarioInicial inventarioInicial, Usuario usuario);

    InventarioInicial findByProducto(Producto producto);

    Response generateReportInvInicial(String filterText, Empresa empresa, String formatType);

}
