/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.enums.TipoMedidaEnum;
import com.gigti.xfinance.backend.others.Response;

import java.util.List;

public interface ProductoService {

    Producto saveProduct(Producto producto, Usuario usuario);
    Response delete(String id);
    List<Producto> findByNombreProducto(Empresa empresa, String productName);
    List<Producto> findAll(String filterText, Empresa empresa, int page, int size);
    List<Producto> findAll(Empresa empresa);
    Producto findByBarCode(Empresa empresa, String barCode);
    List<Producto> findByNameOrBarCode(Empresa empresa, String anything);
    Producto findById(String id);
    List<TipoMedidaEnum> getAllTipoMedidaEnum();
    int count(String filterText, Empresa empresa);
}
