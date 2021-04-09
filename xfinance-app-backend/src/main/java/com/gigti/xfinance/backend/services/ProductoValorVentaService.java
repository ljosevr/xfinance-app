package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoValorVenta;

import java.util.List;

public interface ProductoValorVentaService {

    boolean deleteAllByProductos(List<Producto> productoList);

    ProductoValorVenta findByProductoAndActivoIsTrue(Producto producto);

    ProductoValorVenta saveProductoValor(ProductoValorVenta productoValorVenta);
}
