/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    public Producto saveProduct(Producto producto, Usuario usuario);
    public boolean delete(String id);
    public List<Producto> findByNombreProducto(Empresa empresa, String productName);
    public List<Producto> findAll(Empresa empresa, int page, int size);
    public Producto findByBarCode(Empresa empresa, String barCode);
    public List<Producto> findByNameOrBarCode(Empresa empresa, String anything);
    public Producto findById(String id);
}
