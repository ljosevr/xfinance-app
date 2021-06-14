package com.gigti.xfinance.backend.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.InventarioActualCosto;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.data.Movimiento;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoValorVenta;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.HandledException;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.InventarioActualCostoRepository;
import com.gigti.xfinance.backend.repositories.InventarioActualRepository;
import com.gigti.xfinance.backend.repositories.InventarioInicialRepository;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventarioCommonServiceImpl implements InventarioCommonService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioCommonServiceImpl.class);

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Autowired
    private InventarioActualCostoRepository inventarioActualCostoRepository;

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private ProductoValorVentaService productoValorService;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private InventarioCommonService inventarioService;


    /**
     * Actualizar Impuestos del producto y Precios.
     * @param inventarioInicial
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateTaxAndPrice(InventarioInicial inventarioInicial, BigDecimal cantidad, boolean aumentarStock, TipoMovimientoEnum tipoMovimientoEnum) throws Exception {
        logger.info("--> updateTaxAndPrice");
        logger.info("-- INFO: Cantidad - "+cantidad);
        logger.info("-- INFO: aumentarSotck - "+aumentarStock);
        logger.info("-- INFO: TipoMovimiento - "+tipoMovimientoEnum);

        boolean process = saveProcessInventarioActualAndPrecios(inventarioInicial.getProducto(), aumentarStock,
                cantidad, inventarioInicial.getPrecioVenta(),
                inventarioInicial.getPrecioCosto(), tipoMovimientoEnum,
                true, inventarioInicial.isManageStock(), inventarioInicial.getImpuesto().getValor(),
                inventarioInicial.getImpuesto().getNombre());

        logger.info("<-- updateTaxAndPrice");
        return process;
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
     * @param manageStock -> Si el producto tiene inventario manageStock
     * @param impuestoValor -> Valor del impuesto a aplicar
     * @param impuestoNombre -> Nombre del Impuesto a aplicar
     * @return -> Retorna Verdadero o Falso
     * @throws Exception
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public boolean saveProcessInventarioActualAndPrecios(Producto producto, boolean aumentarStock, BigDecimal cantidad, 
                    BigDecimal precioVenta, BigDecimal precioCosto, TipoMovimientoEnum tipoMovimiento, boolean updatePrices, 
                    boolean manageStock, BigDecimal impuestoValor, String impuestoNombre) throws Exception {
        String methodName = "saveProcessInventarioActualAndPrecios";
        logger.info("--> "+methodName);
            Date fecha = new Date();
            //Actualizar Inventario Costos
            setInvActualCosto(producto, cantidad, precioCosto, manageStock, fecha, tipoMovimiento);
 
            //Actualizar Inventario Agrupado
            setInvActualVenta(producto, aumentarStock, cantidad, manageStock, fecha);

            if(updatePrices) {
                //Producto Valores
                ProductoValorVenta productoValorVentaNew = new ProductoValorVenta();
                ProductoValorVenta productoValorVenta = productoValorService.findByProductoAndActivoIsTrue(producto);
                if (productoValorVenta != null) {
                    productoValorVenta.setActivo(false);
                    productoValorService.saveProductoValor(productoValorVenta);
                }
                //Nuevo ProductoValor
                productoValorVentaNew.setActivo(true);
                productoValorVentaNew.setFechaActualizacion(fecha);
                productoValorVentaNew.setValorVenta(precioVenta);
                productoValorVentaNew.setProducto(producto);

                productoValorService.saveProductoValor(productoValorVentaNew);
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

            movimientoService.saveMovimiento(movimiento);
            
            if(!inventarioService.validarDatosDeInventarios(producto)){
                throw new Exception("Inventarios No Cuadran");
            }
  
        logger.info("<-- "+methodName);
        return true;
    }

    /**
     * Metodo para validar la veracidad de los inventarios
     * @param producto
     * @throws Exception
     */
    public boolean validarDatosDeInventarios(Producto producto) {
        logger.info("--> validarDatosDeInventarios");
        //Consultar Inventario Actual del Producto
        InventarioActual invActual = inventarioActualRepository.findByProducto(producto);

        //Consultar Inventario Actual Costo
        List<InventarioActualCosto> listInvActualCosto = inventarioActualCostoRepository.findByProductoOrderByFechaCreacionAsc(producto);
        BigDecimal cantidadInvActualCosto = BigDecimal.ZERO;
        for(InventarioActualCosto costo : listInvActualCosto){
            if(costo.isActivo() && costo.isManageStock()){
                cantidadInvActualCosto = cantidadInvActualCosto.add(costo.getCantidad());  
            } 
        }
        logger.info("Cantidad Inventario Actual: "+invActual.getCantidad());
        logger.info("Cantidad Inventario Actual Costo: "+cantidadInvActualCosto);
        //Comparar Inventarios
        if(invActual.getCantidad().compareTo(cantidadInvActualCosto) == 0) {
            logger.info("Inventarios OK ");
            logger.info("<-- validarDatosDeInventarios");
            return true;
        } else {
            logger.warn("Inventarios KO - Con Descuadre");
            logger.info("<-- validarDatosDeInventarios");
            return false;
        }
    }

    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Transactional
    protected void setInvActualCosto(Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean manageStock, Date fecha, TipoMovimientoEnum tipoMovimiento) throws HandledException {
        //Inventario Actual Costo
        List<InventarioActualCosto> listInvActualCosto = findByProductoOrderByFechaCreacionAsc(producto);
        List<InventarioActualCosto> result = new ArrayList<>();

        switch (tipoMovimiento) {
            case COMPRA:
            case INV_INICIAL:
                setInvCosto_CompraOrInicial(listInvActualCosto, result, producto, cantidad, precioCosto, manageStock, fecha);
                break;
            case INV_INICIAL_UPDATE_ADD:
                setInvCosto_UpdateInvInicialAdd(listInvActualCosto, result, producto, cantidad, precioCosto, manageStock, fecha);
                break;
            case INV_INICIAL_UPDATE_DEL:
                setInvCosto_UpdateInvInicialDel(listInvActualCosto, result, cantidad, manageStock, fecha);
                break;
            case VENTA:
                setInvCosto_Venta(listInvActualCosto, result, cantidad, manageStock, fecha);
                break;
            case TRASLADO: setInvTraslado();
                break;
            case CREACION: setInvCreacion();
                break;
        }

        inventarioActualCostoRepository.saveAll(result);
    }

    //@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Transactional
    private List<InventarioActualCosto> findByProductoOrderByFechaCreacionAsc(Producto producto){
        return inventarioActualCostoRepository.findByProductoOrderByFechaCreacionAsc(producto);
    }

    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Transactional
    void setInvActualVenta(Producto producto, boolean aumentarStock, BigDecimal cantidad, boolean manageStock, Date fecha) {
        logger.info("--> setInvActualVenta");
        InventarioActual invActual = inventarioActualRepository.findByProducto(producto);
        if(invActual != null){
            invActual.setManageStock(manageStock);
            if(manageStock) {
                if (aumentarStock) {
                    invActual.setCantidad(invActual.getCantidad().add(cantidad));
                } else {
                    invActual.setCantidad(invActual.getCantidad().subtract(cantidad));
                }
            }

        } else {
            invActual = new InventarioActual();
            invActual.setManageStock(manageStock);
            invActual.setProducto(producto);
            invActual.setEmpresa(producto.getEmpresa());
            invActual.setCantidad(BigDecimal.ZERO);
            if(manageStock) {
                if (aumentarStock) {
                    invActual.setCantidad(invActual.getCantidad().add(cantidad));
                } else {
                    invActual.setCantidad(invActual.getCantidad().subtract(cantidad));
                }
            }
        }
        invActual.setFechaActualizacion(fecha);

        inventarioActualRepository.save(invActual);
        logger.info("<-- setInvActualVenta");
    }

    private void setInvCosto_CompraOrInicial(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean manageStock, Date fecha){
        logger.info("--> setInvCosto_CompraOrInicial");
        InventarioActualCosto invActualCosto;

        if(listInvActualCosto.isEmpty()) {
            logger.info("-- No Existe Inventario Costo Actual");
            listInvActualCosto = new ArrayList<>();
            invActualCosto = new InventarioActualCosto(producto, cantidad, precioCosto, manageStock, fecha, fecha, producto.getEmpresa(), true);
            result.add(invActualCosto);
            listInvActualCosto.add(invActualCosto);
        } else {
            if (manageStock) {
                logger.info("-- Manejar Stock");
                //Limited Stock
                //Inv Inicial y Compras
                invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto, precioCosto);
                if (invActualCosto == null) {
                    //Don't Exists Inv With Price Equals
                    invActualCosto = new InventarioActualCosto(producto, cantidad, precioCosto, manageStock, fecha, fecha, producto.getEmpresa(), true);
                } else {
                    invActualCosto.setCantidad(invActualCosto.getCantidad().add(cantidad));
                    invActualCosto.setFechaActualizacion(fecha);
                }
                result.add(invActualCosto);
            } else {
                //NOT MANAGE Stock
                logger.info("-- NOT MANAGE Stock");
                invActualCosto = UtilsBackend.extractInvActCostByInfiniteAndCostPrice(listInvActualCosto, precioCosto);
                if (invActualCosto == null) {
                    //Price Diferent
                    result.add(new InventarioActualCosto(producto, cantidad, precioCosto, manageStock, fecha, fecha, producto.getEmpresa(), true));
                }
            }
        }
        logger.info("<-- setInvCosto_CompraOrInicial");
    }

    private void setInvCosto_UpdateInvInicialAdd(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean manageStock, Date fecha){
        logger.info("--> setInvCosto_UpdateInvInicialAdd");
        InventarioActualCosto invActualCosto;

        if(listInvActualCosto.isEmpty()) {
            logger.info("-- No Existe Inventario Costo Actual");
            listInvActualCosto = new ArrayList<>();
            invActualCosto = new InventarioActualCosto(producto, cantidad, precioCosto, manageStock, fecha, fecha, producto.getEmpresa(), true);
            result.add(invActualCosto);
            listInvActualCosto.add(invActualCosto);
        } else {
            logger.info("-- Existe Inventario Costo Actual");
            if (manageStock) {
                logger.info("-- Manejar Stock");
                //Limited Stock
                //Inv Inicial y Compras
                invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto, precioCosto);
                if (invActualCosto == null) {
                    //Don't Exists Inv With Price Equals
                    invActualCosto = new InventarioActualCosto(producto, cantidad, precioCosto, manageStock, fecha, fecha, producto.getEmpresa(), true);
                } else {
                    invActualCosto.setCantidad(invActualCosto.getCantidad().add(cantidad));
                    invActualCosto.setFechaActualizacion(fecha);
                }
                result.add(invActualCosto);
            } else {
                //NOT MANAGE Stock
                logger.info("-- NOT MANAGE Stock");
                invActualCosto = UtilsBackend.extractInvActCostByInfiniteAndCostPrice(listInvActualCosto, precioCosto);
                if (invActualCosto == null) {
                    //Price Diferent
                    result.add(new InventarioActualCosto(producto, cantidad, precioCosto, manageStock, fecha, fecha, producto.getEmpresa(), true));
                }
            }
        }
        logger.info("<-- setInvCosto_UpdateInvInicialAdd");
    }

    private void setInvCosto_UpdateInvInicialDel(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, BigDecimal cantidad, boolean manageStock, Date fecha) throws HandledException {
        logger.info("--> setInvCosto_UpdateInvInicialDel");
        InventarioActualCosto invActualCosto;
        //Ventas
        if(manageStock) {
            invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto);
            if(invActualCosto != null) {
                if (invActualCosto.getCantidad().compareTo(cantidad) < 0) {
                    while (cantidad.compareTo(BigDecimal.ZERO) > 0) {
                        boolean updateInvCostoObject = false;
                        if (invActualCosto.getCantidad().compareTo(cantidad) < 0) {

                            cantidad = cantidad.subtract(invActualCosto.getCantidad(), new MathContext(10, RoundingMode.HALF_UP));
                            invActualCosto.setCantidad(BigDecimal.ZERO);
                            invActualCosto.setActivo(false);
                            updateInvCostoObject = true;

                        } else {
                            invActualCosto.setCantidad(invActualCosto.getCantidad().subtract(cantidad));
                            cantidad = BigDecimal.ZERO;
                        }
                        invActualCosto.setFechaActualizacion(fecha);
                        result.add(invActualCosto);
                        if(updateInvCostoObject) {
                            invActualCosto = UtilsBackend.extractInvActCostByDateAndDiff(listInvActualCosto, invActualCosto.getId());
                        }
                    }
                } else {
                    invActualCosto.setCantidad(invActualCosto.getCantidad().subtract(cantidad));
                    if (invActualCosto.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                        invActualCosto.setActivo(false);
                    }
                    invActualCosto.setFechaActualizacion(fecha);
                    result.add(invActualCosto);
                }
            } else {
                throw new HandledException("1", "Error al encontrar Inventario");
            }
        }
        logger.info("<-- setInvCosto_UpdateInvInicialDel");
    }

    private void setInvCosto_Venta(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, BigDecimal cantidad, boolean manageStock, Date fecha) throws HandledException {
        logger.info("--> setInvCosto_Venta");
        InventarioActualCosto invActualCosto;
        //Ventas
        if(manageStock) {
            invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto);
            if(invActualCosto != null) {
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
            } else {
                if(CollectionUtils.isNotEmpty(listInvActualCosto)) {                    
                    throw new HandledException("1", "Inventario No Existe o esta en Cero(0) - producto: "+listInvActualCosto.get(0).getProducto().getNombreProducto());
                } else {
                    throw new HandledException("1", "Error al encontrar Inventario");
                }
            }
        }
        logger.info("<-- setInvCosto_Venta");
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