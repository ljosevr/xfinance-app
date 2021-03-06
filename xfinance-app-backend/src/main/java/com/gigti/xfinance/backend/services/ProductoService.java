/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;

public interface ProductoService {

    Response saveProduct(Producto producto, Usuario usuario);
    Response delete(String id);
    List<Producto> findByNombreProducto(Empresa empresa, String productName);
    List<Producto> findAll(String filterText, Empresa empresa, int page, int size);
    List<Producto> findAll(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);
    List<Producto> findAllByEmpresaAndEliminadoIsFalse(Empresa empresa);
    Producto findByBarCode(Empresa empresa, String barCode);
    List<Producto> findByNameOrBarCode(Empresa empresa, String anything);
    Producto findById(String id);
    int count(String filterText, Empresa empresa);
    Response getPriceVenta(Producto producto);

    boolean deleteAllByEmpresa(Empresa emp);

    List<Producto> findAllByEmpresa(Empresa empresa);

    List<Producto> findAllByEmpresaAndNotInfinite(Empresa empresa);
}
