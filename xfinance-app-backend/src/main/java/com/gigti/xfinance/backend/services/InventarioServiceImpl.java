package com.gigti.xfinance.backend.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.InventarioActualCosto;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.data.Movimiento;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoValorVenta;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.AllUtils;
import com.gigti.xfinance.backend.others.HandledException;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.InventarioActualCostoRepository;
import com.gigti.xfinance.backend.repositories.InventarioActualRepository;
import com.gigti.xfinance.backend.repositories.InventarioInicialRepository;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioServiceImpl.class);

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Autowired
    private InventarioActualCostoRepository inventarioActualCostoRepository;

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoValorVentaService productoValorService;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private InventarioService inventarioService;

    @Override
    public List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        String methodName = "findAllInvInicial Con Paginacion";
        logger.info("--> "+methodName);
        final List<InventarioInicial> result = new ArrayList<>();
        List<Producto> listaProductos;
        listaProductos = productoService.findAll(filterText, empresa, offsetBasedPageRequest);

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
                invInicial.setManageStock(true);
            }
            result.add(invInicial);
        });
        result.sort((InventarioInicial o1, InventarioInicial o2) ->
                o1.getProducto().getNombreProducto().compareTo(o2.getProducto().getNombreProducto()));
        logger.info("<-- "+methodName + ": "+result.size());
        return result;
    }

    public List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa) {
        String methodName = "findAllInvInicial Sin Paginacion";
        logger.info("--> "+methodName);
        final List<InventarioInicial> result = new ArrayList<>();
        List<Producto> listaProductos;
        if(filterText == null || filterText.isEmpty()) {
            listaProductos = productoService.findAllByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            listaProductos = productoService.findByNombreProducto(empresa, filterText);
        }

        listaProductos.forEach(p -> {
            InventarioInicial invInicial = inventarioInicialRepository.findByProducto(p);
            if(invInicial == null) {
                invInicial = new InventarioInicial();
                invInicial.setProducto(p);
                invInicial.setImpuesto(new Impuesto());
                invInicial.getImpuesto().setNombre("");
                invInicial.setPrecioCosto(BigDecimal.ZERO);
                invInicial.setPrecioVenta(BigDecimal.ZERO);
                invInicial.setCantidad(BigDecimal.ZERO);
                invInicial.setDefinitivo(false);
                invInicial.setFechaS("");
                invInicial.setManageStockS("");
                invInicial.setDefinitivoS("NO");
                invInicial.setPrecioCostoS("");
                invInicial.setPrecioVentaS("");
            } else {
                invInicial.setFechaS(AllUtils.formatDate(invInicial.getFechaActualizacion()));
                if(invInicial.isManageStock()) {
                    invInicial.setManageStockS("SI");
                } else {
                    invInicial.setManageStockS("NO");
                }
                if(invInicial.isDefinitivo()){
                    invInicial.setDefinitivoS("SI");
                } else {
                    invInicial.setDefinitivoS("NO");
                }

                invInicial.setPrecioCostoS(AllUtils.numberFormat(invInicial.getPrecioCosto()));
                invInicial.setPrecioVentaS(AllUtils.numberFormat(invInicial.getPrecioVenta()));
            }
            result.add(invInicial);
        });
        result.sort((InventarioInicial o1, InventarioInicial o2) ->
                o1.getProducto().getNombreProducto().compareTo(o2.getProducto().getNombreProducto()));
        logger.info("<-- "+methodName + ": "+result.size());
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response processInventarioInicial(InventarioInicial inventarioInicialNuevo, Usuario usuario) {
        String methodName = "processInventarioInicial";
        logger.info("--> "+methodName);
        Response result = new Response();
        try {
            if(usuario != null ) {

                if(StringUtils.isBlank(inventarioInicialNuevo.getId())) {
                    //El inventario Inicial no existe se debe crear
                    logger.info("-- El inventario Inicial no existe se debe crear");
                    createInvInicial(inventarioInicialNuevo, usuario, result);
                } else {
                    //El inventario Inicial Ya Existe. Se debe Actualizar
                    logger.info("-- El inventario Inicial Ya Existe. Se debe Actualizar");

                    //Consultar El Inventario Actual.
                    
                    InventarioActual invActualOld = findInvActualByProducto(inventarioInicialNuevo.getProducto());

                    if(invActualOld != null){
                        //Impuesto
                        Producto prod = inventarioInicialNuevo.getProducto();
                        prod.setImpuesto(inventarioInicialNuevo.getImpuesto());

                        //Actualizando Producto por Impuesto
                        Response responseProduct = productoService.saveProduct(prod, usuario);
                        if(responseProduct.isSuccess()){
                            //Update Inventario Inicial    
                            updateInvInicial(inventarioInicialNuevo,invActualOld,usuario,result);
                        } else {
                            throw new Exception(responseProduct.getMessage());
                        }
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
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void createInvInicial(InventarioInicial inventarioInicial, Usuario usuario, Response result) throws Exception {
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
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private boolean updateTaxAndPrice(InventarioInicial inventarioInicial, BigDecimal cantidad, boolean aumentarStock, TipoMovimientoEnum tipoMovimientoEnum) throws Exception {
        logger.info("--> updateTaxAndPrice");
        logger.info("-- INFO: InvInicial - "+inventarioInicial);
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
     * Metodp para actualizar inventario Inicial mientras no sea Definitivo.
     * @param invInicialNuevo
     * @param usuario
     * @param result
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void updateInvInicial(InventarioInicial invInicialNuevo, InventarioActual invActual, Usuario usuario, Response result) throws Exception {
        logger.info("--> updateInvInicial");
        //Inventario Inicial
        invInicialNuevo.setUsuario(usuario);
        logger.info("-- INFO: InvInicialNuevo - "+invInicialNuevo);
        logger.info("-- INFO: Cantidad - "+invInicialNuevo.getCantidad());
        logger.info("-- INFO: ManageStock - "+invInicialNuevo.isManageStock());
        boolean process;

        if(!invInicialNuevo.isManageStock()) {
            logger.info("-- InvNuevo No maneja Stock");
            //No se maneja Inventario
                if(!invActual.isManageStock()) {
                    logger.info("-- InvActual No maneja Stock");
                    //Solo se debe actualizar Precios e Impuestos.
                    process = updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);

                } else {
                    logger.info("-- InvActual Si maneja Stock");
                    process = updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
                }

        } else {
            logger.info("-- InvNuevo Si maneja Stock");
            //Inventario NUEVO Maneja STOCK

            if (!invActual.isManageStock()) {
                logger.info("-- InvActual NO maneja Stock");
                //Inventario Actual NO MANEJA STOCK - Ahora Es Controlado

                process = updateTaxAndPrice(invInicialNuevo, invInicialNuevo.getCantidad(), true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
            } else {
                logger.info("-- InvActual SI maneja Stock");
                //Inventario Actual MANEJA STOCK - Calcular Diferencia

                int resultado = invActual.getCantidad().compareTo(invInicialNuevo.getCantidad());
                logger.info("-- Diferencia de Stocks: "+resultado);
                logger.info("-- (-1) (Primero Menor) - (0) (Iguales) - (1) (Primero Mayor): "+resultado);
                if(resultado == -1) {
                    //Sumar
                    BigDecimal cantidad = invInicialNuevo.getCantidad().subtract(invActual.getCantidad());
                    logger.info("-- Suma Inventario");
                    process = updateTaxAndPrice(invInicialNuevo, cantidad, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
                } else if(resultado == 1) {
                    //restar
                    logger.info("-- Resta Inventario");
                    BigDecimal cantidad = invActual.getCantidad().subtract(invInicialNuevo.getCantidad());
                    process = updateTaxAndPrice(invInicialNuevo, cantidad, false, TipoMovimientoEnum.INV_INICIAL_UPDATE_DEL);
                } else {
                    //No aumenta Ni resta
                    logger.info("-- No debe hacer Nada Inventario");
                    process = updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
                }
            }
        }

        if(BooleanUtils.isFalse(process)){
            result.setSuccess(false);
            result.setMessage("Error al Procesar Inventario Actual y Precios");
            result.setObject(null);
        } else {

            invInicialNuevo.setFechaActualizacion(new Date());
            invInicialNuevo = this.saveInventarioInicial(invInicialNuevo);

            result.setSuccess(true);
            result.setMessage("Inventario Actualizado Exitosamente");
            result.setObject(invInicialNuevo);
        }
        logger.info("<-- updateInvInicial");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private InventarioInicial saveInventarioInicial(InventarioInicial inventarioInicial){
        return inventarioInicialRepository.save(inventarioInicial);
    }

    @Override
    public List<InventarioActual> findInvActual(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        String methodName = "findInvActual";
        logger.info("--> "+methodName);
        List<InventarioActual> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = inventarioActualRepository.findAllByEmpresa(empresa, offsetBasedPageRequest);
        } else  {
            listResult = inventarioActualRepository.search(empresa, filterText, offsetBasedPageRequest);
        }

        logger.info("<-- "+methodName + " - "+listResult.size());
        return listResult;
    }

    private List<InventarioActual> findInvActualWithoutPagination(String filterText, Empresa empresa) {
        String methodName = "findInvActual-Sin Filtro";
        logger.info("--> "+methodName);
        List<InventarioActual> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = inventarioActualRepository.findAllByEmpresa(empresa);
        } else  {
            listResult = inventarioActualRepository.search(empresa, filterText);
        }

        listResult.forEach(inv -> {
            inv.setFechaS(AllUtils.formatDate(inv.getFechaActualizacion()));
            if(inv.isManageStock()){
                inv.setManageStockS("SI");
            } else {
                inv.setManageStockS("NO");
            }
        });

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

    @Override
    public InventarioInicial findByProducto(Producto producto) {
        return inventarioInicialRepository.findByProducto(producto);
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
        InventarioActual invActual = this.findInvActualByProducto(producto);
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

    private void setInvCosto_UpdateInvInicialDel(List<InventarioActualCosto> listInvActualCosto, List<InventarioActualCosto> result, BigDecimal cantidad, boolean infinite, Date fecha) throws HandledException {
        logger.info("--> setInvCosto_UpdateInvInicialDel");
        InventarioActualCosto invActualCosto;
        //Ventas
        if(!infinite) {
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

    @Override
    public Response generateReportInvInicial(String filterText, Empresa empresa, String formatType) {
        Response response = new Response();
        List<InventarioInicial> listInvInicial = findAllInvInicial(filterText,empresa);
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource(
                    "META-INF/resources/reports/InventarioInicial.jrxml").getInputStream());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listInvInicial);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy","TuInventarioSeguro.com");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            if(formatType.equalsIgnoreCase("pdf")){
               byte[] fileGenerate = JasperExportManager.exportReportToPdf(jasperPrint);
               response.setSuccess(true);
               response.setObject(fileGenerate);
               response.setMessage("Reporte Generado Correctamente");
            }
//            if(formatType.equalsIgnoreCase("xls")){
//                JasperExportManager.exportReportTo(jasperPrint);
//            }
        } catch (Exception e) {
            logger.error("Error al Generar Reporte: "+e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Generar Reporte");

        }

        return response;
    }

    @Override
    public Response generateReportInvActual(String filterText, Empresa empresa, String formatType) {
        Response response = new Response();
        List<InventarioActual> listInvActual = findInvActualWithoutPagination(filterText,empresa);
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource(
                    "META-INF/resources/reports/InventarioActual.jrxml").getInputStream());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listInvActual);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy","TuInventarioSeguro.com");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            if(formatType.equalsIgnoreCase("pdf")){
                byte[] fileGenerate = JasperExportManager.exportReportToPdf(jasperPrint);
                response.setSuccess(true);
                response.setObject(fileGenerate);
                response.setMessage("Reporte Generado Correctamente");
            }
//            if(formatType.equalsIgnoreCase("xls")){
//                JasperExportManager.exportReportTo(jasperPrint);
//            }
        } catch (Exception e) {
            logger.error("Error al Generar Reporte: "+e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Generar Reporte");
        }

        return response;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public InventarioActual findInvActualByProducto(Producto producto) {
        return inventarioActualRepository.findByProducto(producto);
    }
}