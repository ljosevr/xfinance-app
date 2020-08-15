package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Producto;

import java.util.List;

public interface ProductoValorVentaService {

    boolean deleteAllByProductos(List<Producto> productoList);
}
