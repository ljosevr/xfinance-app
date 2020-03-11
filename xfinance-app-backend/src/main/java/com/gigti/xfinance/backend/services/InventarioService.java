package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.ProductoInventario;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;

import java.util.List;

public interface InventarioService {

    List<ProductoInventario> findAll(String filterText, Empresa empresa, int page, int size);
    Response saveInventarioInicial(ProductoInventario productoInventario, Usuario usuario);
}
