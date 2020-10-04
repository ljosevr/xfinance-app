package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.HandledException;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.math.BigDecimal;
import java.util.List;

public interface InventarioService {

    List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, int page, int size);
    List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);

    int getCount(String filterText, Empresa empresa);
    Response saveInventarioInicial(InventarioInicial inventarioInicial, Usuario usuario);

    /**
     * Metodo para buscar todo el Inventario Actual de los productos
     * @param filterText
     * @param empresa
     * @param page
     * @param size
     * @return
     */
    List<InventarioActualCosto> findInvActual(String filterText, Empresa empresa, int page, int size);
    List<InventarioActualCosto> findInvActual(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);
    int countInvActual(String filterText, Empresa empresa);

    /**
     * Metodo para actualizar Inventario Actual, Movimientos, y ProductoValores
     * Debe aplicarse en un metodo Transactional
     * @param producto -> Es el Producto
     * @param aumentarStock -> Flag para saber si se aumentar Stock o disminuye
     * @param cantidad -> Cantidad a guardar o mover
     * @param precioVenta -> Precio Venta aplicado
     * @param precioCosto -> precio de Costo Aplicado
     * @param tipoMovimiento -> Fuente de Modificación
     * @param updatePrices -> Sirve para saber si actualiza Precios o no
     * @param infinite -> Si el producto tiene inventario infinite
     * @param impuestoValor -> Valor del impuesto a aplicar
     * @param impuestoNombre -> Nombre del Impuesto a aplicar
     * @return -> Retorna Verdadero o Falso
     */
    boolean saveProcessInventarioActualAndPrecios(Producto producto, boolean aumentarStock, BigDecimal cantidad, BigDecimal precioVenta, BigDecimal precioCosto, TipoMovimientoEnum tipoMovimiento, boolean updatePrices, boolean infinite, BigDecimal impuestoValor, String impuestoNombre) throws HandledException;

    InventarioInicial findByProducto(Producto producto);

    boolean deleteAllInventarios(Empresa emp, List<Producto> productosList);
}
