package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Producto;

import java.util.List;

public interface MovimientoService {

    boolean deleteAllByProductos(List<Producto> productoList);
}
