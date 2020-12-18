package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.HandledException;
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
            listaProductos = productoRepository.findAllByEmpresaAndNombreProducto(empresa, filterText);
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
                invInicial.setDefinitivo(false);

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
            listaProductos = productoRepository.findAllByEmpresaAndNombreProducto(empresa, filterText);
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
                invInicial.setDefinitivo(false);

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
                    //El inventario Inicial no existe se debe crear
                    logger.info("-- El inventario Inicial no existe se debe crear");
                    createInvInicial(inventarioInicial, usuario, result);
                } else {
                    //El inventario Inicial Ya Existe. Se debe Actualizar
                    logger.info("-- El inventario Inicial Ya Existe. Se debe Actualizar");
                    //EsDefinitivo

                    //Consultar El Inventario Actual.ƒ
                    InventarioInicial invInicialActual = inventarioInicialRepository.findById(inventarioInicial.getId()).get();

                    if(invInicialActual != null){
                        updateInvInicial(inventarioInicial,invInicialActual,usuario,result);
                    } else {
                        result.setSuccess(false);
                        result.setMessage("Error al Obtener Inventario actual");
                    }
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

    /**
     * Metodo para crear Inventario Inicial.
     * @param inventarioInicial
     * @param usuario
     * @param result
     * @throws HandledException
     */
    private void createInvInicial(InventarioInicial inventarioInicial, Usuario usuario, Response result) throws HandledException {
        logger.info("--> createInvInicial");
        //Inventario Inicial
        inventarioInicial.setUsuario(usuario);
        inventarioInicial.setFechaActualizacion(new Date());
        inventarioInicial.setManageStock(inventarioInicial.isManageStock());
        inventarioInicial = inventarioInicialRepository.save(inventarioInicial);

        boolean process = updateTaxAndPrice(inventarioInicial, inventarioInicial.getCantidad(), true, TipoMovimientoEnum.INV_INICIAL);

        if(BooleanUtils.isFalse(process)){
            result.setSuccess(false);
            result.setMessage("Error al Procesar Inventario Actual y Precios");
            result.setObject(null);
        } else {
            result.setSuccess(true);
            result.setMessage("Inventario Guardado Exitosamente");
            result.setObject(inventarioInicial);
        }
        logger.info("<-- createInvInicial");
    }

    /**
     * Actualizar Impuestos del producto y Precios.
     * @param inventarioInicial
     * @return
     * @throws HandledException
     */
    private boolean updateTaxAndPrice(InventarioInicial inventarioInicial, BigDecimal cantidad, boolean aumentarStock, TipoMovimientoEnum tipoMovimientoEnum) throws HandledException {
        logger.info("--> updateTaxAndPrice");
        logger.info("-- INFO: InvInicial - "+inventarioInicial);
        logger.info("-- INFO: Cantidad - "+cantidad);
        logger.info("-- INFO: aumentarSotck - "+aumentarStock);
        logger.info("-- INFO: TipoMovimiento - "+tipoMovimientoEnum);
        //Impuesto
        Producto prod = inventarioInicial.getProducto();
        prod.setImpuesto(inventarioInicial.getImpuesto());

        productoRepository.save(prod);

        boolean process = saveProcessInventarioActualAndPrecios(inventarioInicial.getProducto(), aumentarStock,
                cantidad, inventarioInicial.getPrecioVenta(),
                inventarioInicial.getPrecioCosto(), tipoMovimientoEnum,
                true, inventarioInicial.isManageStock(), inventarioInicial.getImpuesto().getValor(),
                inventarioInicial.getImpuesto().getNombre());

        logger.info("<-- updateTaxAndPrice");
        return process;
    }

    /**
     * Metodp para actualizar inventario Inicial mientras no sea Definitivo.
     * @param invInicialNuevo
     * @param usuario
     * @param result
     * @throws HandledException
     */
    private void updateInvInicial(InventarioInicial invInicialNuevo, InventarioInicial invInicialActual, Usuario usuario, Response result) throws HandledException {
        logger.info("--> updateInvInicial");
        //Inventario Inicial
        invInicialNuevo.setUsuario(usuario);
        invInicialNuevo.setFechaActualizacion(new Date());
        invInicialNuevo.setManageStock(invInicialNuevo.isManageStock());
        invInicialNuevo = inventarioInicialRepository.save(invInicialNuevo);
        boolean process;

        if(!invInicialNuevo.isManageStock()) {
            //No se maneja Inventario
                if(!invInicialActual.isManageStock()) {
                    //Solo se debe actualizar Precios e Impuestos.
                    process = updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE);

                } else {
                    //Inventarios diferentes actualizar Cantidades
                    process = updateTaxAndPrice(invInicialNuevo, invInicialActual.getCantidad(), false, TipoMovimientoEnum.INV_INICIAL_UPDATE);
                }

        } else {
            //Inventario NUEVO Maneja STOCK

            if (!invInicialActual.isManageStock()) {
                //Inventario Actual NO MANEJA STOCK - Ahora Es Controlado

                process = updateTaxAndPrice(invInicialNuevo, invInicialNuevo.getCantidad(), true, TipoMovimientoEnum.INV_INICIAL_UPDATE);

            } else {
                //Inventario Actual MANEJA STOCK - Calcular Diferencia

                BigDecimal cantidad = invInicialActual.getCantidad().subtract(invInicialNuevo.getCantidad());
                if(cantidad.compareTo(BigDecimal.ZERO) > 0) {
                    //restar
                    process = updateTaxAndPrice(invInicialNuevo, cantidad, false, TipoMovimientoEnum.INV_INICIAL_UPDATE);
                } else if(cantidad.compareTo(BigDecimal.ZERO) < 0) {
                    //Sumar
                    process = updateTaxAndPrice(invInicialNuevo, cantidad.abs(), true, TipoMovimientoEnum.INV_INICIAL_UPDATE);
                } else {
                    //No aumenta Ni resta
                    process = updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE);
                }
            }
        }

        if(BooleanUtils.isFalse(process)){
            result.setSuccess(false);
            result.setMessage("Error al Procesar Inventario Actual y Precios");
            result.setObject(null);
        } else {
            result.setSuccess(true);
            result.setMessage("Inventario Actualizado Exitosamente");
            result.setObject(invInicialNuevo);
        }
        logger.info("<-- updateInvInicial");
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
     * @param manageStock -> Si el producto tiene inventario manageStock
     * @param impuestoValor -> Valor del impuesto a aplicar
     * @param impuestoNombre -> Nombre del Impuesto a aplicar
     * @return -> Retorna Verdadero o Falso
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean saveProcessInventarioActualAndPrecios(Producto producto, boolean aumentarStock, BigDecimal cantidad, BigDecimal precioVenta, BigDecimal precioCosto, TipoMovimientoEnum tipoMovimiento, boolean updatePrices, boolean manageStock, BigDecimal impuestoValor, String impuestoNombre) throws HandledException {
        String methodName = "saveProcessInventarioActualAndPrecios";
        logger.info("--> "+methodName);
        try {
            Date fecha = new Date();

            //Actualizar Inventario Costos
            setInvActualCosto(producto, cantidad, precioCosto, manageStock, fecha, tipoMovimiento);

            //Actualizar Inventario Agrupado
            setInvActualVenta(producto, aumentarStock, cantidad, manageStock, fecha);

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

        }catch(HandledException e){
            logger.error(methodName  + ": "+e.getMessage(), e);
            throw e;
        }
        logger.info("<-- "+methodName);
        return true;
    }

    @Override
    public InventarioInicial findByProducto(Producto producto) {
        return inventarioInicialRepository.findByProducto(producto);
    }

    private void setInvActualCosto(Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean infinite, Date fecha, TipoMovimientoEnum tipoMovimiento) throws HandledException {
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

    @Transactional(propagation = Propagation.REQUIRED)
    void setInvActualVenta(Producto producto, boolean aumentarStock, BigDecimal cantidad, boolean manageStock, Date fecha) {
        logger.info("--> setInvActualVenta");
        InventarioActual invActualVenta = inventarioActualRepository.findByProducto(producto);
        if(invActualVenta != null){
            if(manageStock) {
                if (aumentarStock) {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().add(cantidad));
                } else {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().subtract(cantidad));
                }
            }

        } else {
            invActualVenta = new InventarioActual();
            invActualVenta.setManageStock(manageStock);
            invActualVenta.setProducto(producto);
            invActualVenta.setEmpresa(producto.getEmpresa());
            invActualVenta.setCantidad(BigDecimal.ZERO);
            if(manageStock) {
                if (aumentarStock) {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().add(cantidad));
                } else {
                    invActualVenta.setCantidad(invActualVenta.getCantidad().subtract(cantidad));
                }
            }
        }
        invActualVenta.setFechaActualizacion(fecha);

        inventarioActualRepository.save(invActualVenta);
        logger.info("<-- setInvActualVenta");
    }

    private void setInvCosto_CompraOrInicial(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, Producto producto, BigDecimal cantidad, BigDecimal precioCosto, boolean manageStock, Date fecha){
        logger.info("--> setInvCosto_CompraOrInicial");
        InventarioActualCosto invActualCosto;

        //if(listInvActualCosto == null && listInvActualCosto.isEmpty()) {
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

    private void setInvCosto_Venta(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, BigDecimal cantidad, boolean infinite, Date fecha) throws HandledException {
        logger.info("--> setInvCosto_Venta");
        InventarioActualCosto invActualCosto;
        //Ventas
        if(!infinite) {
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
                throw new HandledException("1", "Error al encontrar Inventario");
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