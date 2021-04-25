package com.gigti.xfinance.backend.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.AllUtils;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.InventarioInicialRepository;
import com.gigti.xfinance.backend.repositories.ProductoRepository;

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
public class InventarioInicialServiceImpl implements InventarioInicialService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioInicialServiceImpl.class);

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioActualService inventarioActualService;

    @Autowired
    private InventarioCommonService inventarioCommonService;

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

    @Override
    public List<InventarioInicial> findAllInvInicial(String filterText, Empresa empresa, int page, int size) {
        String methodName = "findAllInvInicial Con Paginacion";
        logger.info("--> "+methodName);
        final List<InventarioInicial> result = new ArrayList<>();
        List<Producto> listaProductos;
        listaProductos = productoService.findAll(filterText, empresa, page, size);

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
                    
                    InventarioActual invActualOld = inventarioActualService.findInvActualByProducto(inventarioInicialNuevo.getProducto());

                    if(invActualOld != null){
                        //Impuesto
                        Producto prod = inventarioInicialNuevo.getProducto();
                        prod.setImpuesto(inventarioInicialNuevo.getImpuesto());

                        //Actualizando Producto por Impuesto
                        Producto producto = productoRepository.save(prod);

                        if(producto != null){
                            //Update Inventario Inicial    
                            updateInvInicial(inventarioInicialNuevo,invActualOld,usuario,result);
                        } else {
                            throw new Exception("Error al actualizar producto");
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
            result.setMessage("Eror InvInicial: "+e.getMessage());
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

        boolean process = inventarioCommonService.updateTaxAndPrice(inventarioInicial, inventarioInicial.getCantidad(), true, TipoMovimientoEnum.INV_INICIAL);

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
        logger.info("-- INFO: Cantidad - "+invInicialNuevo.getCantidad());
        logger.info("-- INFO: ManageStock - "+invInicialNuevo.isManageStock());
        boolean process;

        if(!invInicialNuevo.isManageStock()) {
            logger.info("-- InvNuevo No maneja Stock");
            //No se maneja Inventario
                if(!invActual.isManageStock()) {
                    logger.info("-- InvActual No maneja Stock");
                    //Solo se debe actualizar Precios e Impuestos.
                    process = inventarioCommonService.updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);

                } else {
                    logger.info("-- InvActual Si maneja Stock");
                    process = inventarioCommonService.updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
                }

        } else {
            logger.info("-- InvNuevo Si maneja Stock");
            //Inventario NUEVO Maneja STOCK

            if (!invActual.isManageStock()) {
                logger.info("-- InvActual NO maneja Stock");
                //Inventario Actual NO MANEJA STOCK - Ahora Es Controlado

                process = inventarioCommonService.updateTaxAndPrice(invInicialNuevo, invInicialNuevo.getCantidad(), true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
            } else {
                logger.info("-- InvActual SI maneja Stock");
                //Inventario Actual MANEJA STOCK - Calcular Diferencia

                int resultado = invActual.getCantidad().compareTo(invInicialNuevo.getCantidad());
                logger.info("-- Diferencia de Stocks: "+resultado);
                if(resultado == -1) {
                    //Sumar
                    BigDecimal cantidad = invInicialNuevo.getCantidad().subtract(invActual.getCantidad());
                    logger.info("-- Suma Inventario");
                    process = inventarioCommonService.updateTaxAndPrice(invInicialNuevo, cantidad, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
                } else if(resultado == 1) {
                    //restar
                    logger.info("-- Resta Inventario");
                    BigDecimal cantidad = invActual.getCantidad().subtract(invInicialNuevo.getCantidad());
                    process = inventarioCommonService.updateTaxAndPrice(invInicialNuevo, cantidad, false, TipoMovimientoEnum.INV_INICIAL_UPDATE_DEL);
                } else {
                    //No aumenta Ni resta
                    logger.info("-- No debe hacer Nada Inventario");
                    process = inventarioCommonService.updateTaxAndPrice(invInicialNuevo, BigDecimal.ZERO, true, TipoMovimientoEnum.INV_INICIAL_UPDATE_ADD);
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
    public InventarioInicial findByProducto(Producto producto) {
        return inventarioInicialRepository.findByProducto(producto);
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
}