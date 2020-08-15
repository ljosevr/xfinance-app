package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioServiceImpl.class);

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Autowired
    private InventarioActualCostoRepository inventarioActualCostoRepository;

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private ImpuestoRepository impuestoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;


    @Override
    public List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, int page, int size) {
        String methodName = "findAllInvInicial";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        final List<InventarioInicial> result = new ArrayList<>();
        List<Producto> listaProductos;
        if(filterText == null || filterText.isEmpty()) {
            listaProductos = productoRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
        } else  {
            listaProductos = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }

        listaProductos.forEach(p -> {
            InventarioInicial invInicial = inventarioInicialRepository.findByProducto(p);
            if(invInicial == null) {
                invInicial = new InventarioInicial();
                invInicial.setProducto(p);
                invInicial.setImpuesto(new Impuesto());
                invInicial.setPrecioCosto(BigDecimal.ZERO);
                invInicial.setPrecioVenta(BigDecimal.ZERO);
                invInicial.setCantidad(BigDecimal.ZERO);

            }
            result.add(invInicial);
        });
        result.sort((InventarioInicial o1, InventarioInicial o2) ->
                o1.getProducto().getNombreProducto().compareTo(o2.getProducto().getNombreProducto()));
        logger.info("<-- "+methodName);
        return result;
    }

    @Override
    public List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        String methodName = "findAllInvInicial";
        logger.info("--> "+methodName);
        final List<InventarioInicial> result = new ArrayList<>();
        List<Producto> listaProductos;
        if(filterText == null || filterText.isEmpty()) {
            listaProductos = productoRepository.findByEmpresaAndEliminadoIsFalse(empresa, offsetBasedPageRequest);
        } else  {
            listaProductos = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }

        listaProductos.forEach(p -> {
            InventarioInicial invInicial = inventarioInicialRepository.findByProducto(p);
            if(invInicial == null) {
                invInicial = new InventarioInicial();
                invInicial.setProducto(p);
                invInicial.setImpuesto(new Impuesto());
                invInicial.setPrecioCosto(BigDecimal.ZERO);
                invInicial.setPrecioVenta(BigDecimal.ZERO);
                invInicial.setCantidad(BigDecimal.ZERO);

            }
            result.add(invInicial);
        });
        result.sort((InventarioInicial o1, InventarioInicial o2) ->
                o1.getProducto().getNombreProducto().compareTo(o2.getProducto().getNombreProducto()));
        logger.info("<-- "+methodName + ": "+result.size());
        return result;
    }

    @Override
    public int getCount(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = productoRepository.countByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            count = productoRepository.countByEmpresaAndNombreProducto(empresa, filterText);
        }

        return count;
    }

    @Override
    @Transactional
    public Response saveInventarioInicial(InventarioInicial inventarioInicial, Usuario usuario) {
        String methodName = "saveInventarioInicial";
        logger.info("--> "+methodName);
        Response result = new Response();
        try {
            if(usuario != null ) {

                if(StringUtils.isBlank(inventarioInicial.getId())) {
                    //Inventario Inicial
                    inventarioInicial.setUsuario(usuario);
                    inventarioInicial.setFechaActualizacion(new Date());
                    inventarioInicial = inventarioInicialRepository.save(inventarioInicial);

                    //Impuesto
                    Producto prod = inventarioInicial.getProducto();
                    prod.setImpuesto(inventarioInicial.getImpuesto());

                    productoRepository.save(prod);

                    boolean process = saveProcessInventarioActualAndPrecios(inventarioInicial.getProducto(), true, inventarioInicial.getCantidad(), inventarioInicial.getPrecioVenta(), inventarioInicial.getPrecioCosto(), TipoMovimientoEnum.INV_INICIAL, true, inventarioInicial.isInfinite(),inventarioInicial.getImpuesto().getValor(), inventarioInicial.getImpuesto().getNombre());
                    if(BooleanUtils.isFalse(process)){
                        result.setSuccess(false);
                        result.setMessage("Error al Procesar Inventario Actual y Precios");
                        result.setObject(null);
                    } else {
                        result.setSuccess(true);
                        result.setMessage("Inventario Guardado Exitosamente");
                        result.setObject(inventarioInicial);
                    }
                } else {
                    result.setSuccess(false);
                    result.setMessage("Inventario Inicial del producto Ya existe");
                }
            } else{
                result.setSuccess(false);
                result.setMessage("Usuario Null");
            }
        }catch(Exception e){
            logger.error(methodName  + ": "+e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("Error al Procesar");
        }
        logger.info("<-- "+methodName);
        return result;
    }

    @Override
    public List<InventarioActualCosto> findInvActual(String filterText, Empresa empresa, int page, int size) {
        String methodName = "findInvActual";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        List<InventarioActualCosto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = inventarioActualCostoRepository.findAllByEmpresa(empresa, pageable);
        } else  {
            listResult = inventarioActualCostoRepository.search(empresa, filterText, pageable);
        }

        logger.info("<-- "+methodName);
        return listResult;
    }

    @Override
    public List<InventarioActualCosto> findInvActual(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        String methodName = "findInvActual";
        logger.info("--> "+methodName);
        List<InventarioActualCosto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = inventarioActualCostoRepository.findAllByEmpresa(empresa, offsetBasedPageRequest);
        } else  {
            listResult = inventarioActualCostoRepository.search(empresa, filterText, offsetBasedPageRequest);
        }

        logger.info("<-- "+methodName + " - "+listResult.size());
        return listResult;
    }

    @Override
    public int countInvActual(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = inventarioActualCostoRepository.countAllByEmpresa(empresa);
        } else  {
            count = inventarioActualCostoRepository.countAllByEmpresaAndNombreProducto(empresa, filterText);
        }

        return count;
    }

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
    @Override
    public boolean saveProcessInventarioActualAndPrecios(Producto producto, boolean aumentarStock, BigDecimal cantidad, BigDecimal precioVenta, BigDecimal precioCosto, TipoMovimientoEnum tipoMovimiento, boolean updatePrices, boolean infinite, BigDecimal impuestoValor, String impuestoNombre) {
        String methodName = "saveProcessInventarioActualAndPrecios";
        logger.info("--> "+methodName);
        try {
            Date fecha = new Date();

            //Actualizar Inventario Costos
            setInvActualCosto(producto, cantidad, precioCosto, infinite, fecha, tipoMovimiento);

            //Actualizar Inventario Agrupado
            setInvActualVenta(producto, aumentarStock, cantidad, infinite, fecha);

            if(updatePrices) {
                //Producto Valores
                ProductoValorVenta productoValorVentaNew = new ProductoValorVenta();
                ProductoValorVenta productoValorVenta = productoValoresRepository.findByProductoAndActivoIsTrue(producto);
                if (productoValorVenta != null) {
                    productoValorVenta.setActivo(false);
                    productoValoresRepository.save(productoValorVenta);
                }
                //Nuevo ProductoValor
                productoValorVentaNew.setActivo(true);
                productoValorVentaNew.setFechaActualizacion(fecha);
                productoValorVentaNew.setValorVenta(precioVenta);
                productoValorVentaNew.setProducto(producto);

                productoValoresRepository.save(productoValorVentaNew);
            }

            //Movimientos
            Movimiento movimiento = new Movimiento();
            movimiento.setProducto(producto);
            movimiento.setCantidad(cantidad);
            movimiento.setFecha(fecha);
            movimiento.setPrecioCosto(precioCosto);
            movimiento.setPrecioVenta(precioVenta);
            movimiento.setTipoMovimiento(tipoMovimiento);
            movimiento.setImpuestoValor(impuestoValor);
            movimiento.setImpuestoNombre(impuestoNombre);

            movimientoRepository.save(movimiento);

        }catch(Exception e){
            logger.error(methodName  + ": "+e.getMessage(), e);
            return false;
        }
        logger.info("<-- "+methodName);
        return true;
    }

    @Override
    public InventarioInicial findByProducto(Producto producto) {
        return inventarioInicialRepository.findByProducto(producto);
    }

    private void setInvActualCosto(Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean infinite, Date fecha, TipoMovimientoEnum tipoMovimiento) {
        //Inventario Actual Costo
        List<InventarioActualCosto> listInvActualCosto = inventarioActualCostoRepository.findByProductoOrderByFechaCreacionAsc(producto);
        List<InventarioActualCosto> result = new ArrayList<>();

        switch (tipoMovimiento) {
            case COMPRA:
            case INV_INICIAL:
                setInvCosto_CompraOrInicial(listInvActualCosto, result, producto, cantidad, precioCosto, infinite, fecha);
                break;
            case VENTA:
                setInvCosto_Venta(listInvActualCosto, result, cantidad, infinite, fecha);
                break;
            case TRASLADO: setInvTraslado();
                break;
            case CREACION: setInvCreacion();
                break;
        }

        inventarioActualCostoRepository.saveAll(result);
    }

    private void setInvActualVenta(Producto producto, boolean aumentarStock, BigDecimal cantidad, boolean infinite, Date fecha) {
        InventarioActual invActualVenta = inventarioActualRepository.findByProducto(producto);
        if(invActualVenta != null){
            if(!infinite) {
                if (aumentarStock) {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().add(cantidad));
                } else {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().subtract(cantidad));
                }
            }

        } else {
            invActualVenta = new InventarioActual();
            invActualVenta.setInfinite(infinite);
            invActualVenta.setProducto(producto);
            invActualVenta.setEmpresa(producto.getEmpresa());
            invActualVenta.setCantidad(BigDecimal.ZERO);
            if(!infinite) {
                if (aumentarStock) {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().add(cantidad));
                } else {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().subtract(cantidad));
                }
            }
        }
        invActualVenta.setFechaActualizacion(fecha);

        inventarioActualRepository.save(invActualVenta);
    }

    private void setInvCosto_CompraOrInicial(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean infinite, Date fecha){
        InventarioActualCosto invActualCosto;

        //if(listInvActualCosto == null && listInvActualCosto.isEmpty()) {
        if(listInvActualCosto == null) {
            listInvActualCosto = new ArrayList<>();
            invActualCosto = new InventarioActualCosto(producto, cantidad, precioCosto, infinite, fecha, fecha, producto.getEmpresa(), true);
            result.add(invActualCosto);
            listInvActualCosto.add(invActualCosto);
        } else {
            if (!infinite) {
                //Limited Stock
                //Inv Inicial y Compras
                invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto, precioCosto);
                if (invActualCosto == null) {
                    //Don't Exists Inv With Price Equals
                    invActualCosto = new InventarioActualCosto(producto, cantidad, precioCosto, infinite, fecha, fecha, producto.getEmpresa(), true);
                } else {
                    invActualCosto.setCantidad(invActualCosto.getCantidad().add(cantidad));
                    invActualCosto.setFechaActualizacion(fecha);
                }
                result.add(invActualCosto);
            } else {
                //isInfinite Stock
                invActualCosto = UtilsBackend.extractInvActCostByInfiniteAndCostPrice(listInvActualCosto, precioCosto);
                if (invActualCosto == null) {
                    //Price Diferent
                    result.add(new InventarioActualCosto(producto, cantidad, precioCosto, infinite, fecha, fecha, producto.getEmpresa(), true));
                }
            }
        }
    }

    private void setInvCosto_Venta(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, BigDecimal cantidad, boolean infinite, Date fecha){
        InventarioActualCosto invActualCosto;
        //Ventas
        if(!infinite) {
            invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto);
            if (invActualCosto.getCantidad().compareTo(cantidad) < 0) {
                while (cantidad.compareTo(BigDecimal.ZERO) > 0) {
                    if (invActualCosto.getCantidad().compareTo(cantidad) < 0) {
                        cantidad = cantidad.subtract(invActualCosto.getCantidad());
                        invActualCosto.setCantidad(BigDecimal.ZERO);
                        invActualCosto.setActivo(false);
                        invActualCosto = UtilsBackend.extractInvActCostByDateAndDiff(listInvActualCosto, invActualCosto.getId());
                    } else {
                        invActualCosto.setCantidad(invActualCosto.getCantidad().subtract(cantidad));
                        cantidad = BigDecimal.ZERO;
                    }
                    invActualCosto.setFechaActualizacion(fecha);
                    result.add(invActualCosto);
                }
            } else {
                invActualCosto.setCantidad(invActualCosto.getCantidad().subtract(cantidad));
                if (invActualCosto.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                    invActualCosto.setActivo(false);
                }
                invActualCosto.setFechaActualizacion(fecha);
                result.add(invActualCosto);
            }
        }
    }

    private void setInvTraslado(){

    }

    private void setInvCreacion(){

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllInventarios(Empresa emp, List<Producto> productosList) {
        logger.info("--> deleteAllInventarios");
        try{

            logger.info("Inventario Inicial Delete: "+inventarioInicialRepository.deleteAllByProductoIn(productosList));
            inventarioInicialRepository.flush();
            logger.info("Inventario Costo Delete: "+inventarioActualCostoRepository.deleteAllByProductoIn(productosList));
            inventarioActualCostoRepository.flush();
            logger.info("Inventario Actual Delete: "+inventarioActualRepository.deleteAllByProductoIn(productosList));
            inventarioActualRepository.flush();

            logger.info("<-- deleteAllInventarios");
            return true;
        } catch(Exception e) {
            logger.error("Error: "+e.getMessage(), e);
            return false;
        }
    }
}