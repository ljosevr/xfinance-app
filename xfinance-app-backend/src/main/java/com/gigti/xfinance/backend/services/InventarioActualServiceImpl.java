package com.gigti.xfinance.backend.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.InventarioActualCosto;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.AllUtils;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.InventarioActualCostoRepository;
import com.gigti.xfinance.backend.repositories.InventarioActualRepository;

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
public class InventarioActualServiceImpl implements InventarioActualService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioActualServiceImpl.class);

    @Autowired
    private InventarioActualCostoRepository inventarioActualCostoRepository;

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

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
        String methodName = "countInvActual";
        logger.info("--> "+methodName);
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = inventarioActualCostoRepository.countAllByEmpresa(empresa);
        } else  {
            count = inventarioActualCostoRepository.countAllByEmpresaAndNombreProducto(empresa, filterText);
        }
        logger.info("<-- "+methodName);
        return count;
    }

    @Transactional(readOnly = true)
    private List<InventarioActualCosto> findByProductoOrderByFechaCreacionAsc(Producto producto){
        return inventarioActualCostoRepository.findByProductoOrderByFechaCreacionAsc(producto);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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