package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.others.Response;

import java.math.BigDecimal;
import java.util.List;

public interface InventarioService {

    List<InventarioInicial> findAll(String filterText, Empresa empresa, int page, int size);
    Response saveInventarioInicial(InventarioInicial inventarioInicial, Usuario usuario);

    /**
     * Metodo para actualizar Inventario Actual, Movimientos, y ProductoValores
     * Debe aplicarse en un metodo Transactional
     * @param producto -> Es el Producto
     * @param aumentarStock -> Flag para saber si se aumentar Stock o disminuye
     * @param cantidad -> Cantidad a guardar o mover
     * @param precioVenta -> Precio Venta aplicado
     * @param precioCosto -> precio de Costo Aplicado
     * @param impuestoId -> Id del Impuesto a aplicar
     * @param tipoMovimiento -> Fuente de Modificación
     * @return -> Retorna Verdadero o Falso
     */
    boolean saveProcessInventarioActualAndPrecios(Producto producto, boolean aumentarStock, BigDecimal cantidad, BigDecimal precioVenta, BigDecimal precioCosto, String impuestoId, TipoMovimiento tipoMovimiento);
}
