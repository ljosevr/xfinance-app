package com.gigti.xfinance.backend.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gigti.xfinance.backend.business.CompraBusinessService;
import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.CompraItem;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoValorVenta;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.CompraItemRepository;
import com.gigti.xfinance.backend.repositories.CompraRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    // @PersistenceContext
    // private EntityManager em; 

    @Autowired
    private CompraBusinessService compraBusinessService;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraItemRepository compraItemRepository;

    @Autowired
    private InventarioCommonService inventarioService;

    @Autowired
    private ProductoValorVentaService productoValorVentaService;

    @Override
    public int count(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd) {
        int count;
        dateStart = dateStart == null ? LocalDate.now() : dateStart;
        LocalDateTime dateStartTime = dateStart.atTime(0,0,0);
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        LocalDateTime dateEndTime = dateEnd.atTime(23,59,59);
        logger.info("Fecha Inicio: "+java.sql.Timestamp.valueOf(dateStartTime));
        logger.info("Fecha Fin: "+java.sql.Timestamp.valueOf(dateEndTime));
        if(filterText == null || filterText.isEmpty()) {
            count = compraRepository.countAllByEmpresa(empresa,
                    java.sql.Timestamp.valueOf(dateStartTime),
                    java.sql.Timestamp.valueOf(dateEndTime));
        } else  {
            count = compraRepository.countAllByEmpresaAndNumeroFactura(empresa,
                    filterText,
                    java.sql.Timestamp.valueOf(dateStartTime),
                    java.sql.Timestamp.valueOf(dateEndTime));
        }

        return count;
    }

    @Override
    public List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, int page, int size) {
        String methodName = "findAll-Compra";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        dateStart = dateStart == null ? LocalDate.now() : dateStart;
        LocalDateTime dateStartTime = dateStart.atTime(0,0,0);
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        LocalDateTime dateEndTime = dateEnd.atTime(23,59,59);
        logger.info("Fecha Inicio: "+String.valueOf(dateStartTime));
        logger.info("Fecha Fin: "+String.valueOf(dateEndTime));
        List<Compra> listResult;
        try {
            if (filterText == null || filterText.isEmpty()) {
                listResult = compraRepository.findAllByEmpresa(empresa,
                        java.sql.Timestamp.valueOf(dateStartTime),
                        java.sql.Timestamp.valueOf(dateEndTime),
                        pageable);
            } else {
                listResult = compraRepository.search(empresa,
                        filterText,
                        java.sql.Timestamp.valueOf(dateStartTime),
                        java.sql.Timestamp.valueOf(dateEndTime),
                        pageable);
            }

            logger.info("Cantidad de Registros: " + listResult.size());

            for(Compra c : listResult) {
                BigDecimal total = BigDecimal.ZERO;
                logger.info("Compra: " + c.getNumeroFactura());
                logger.info( "Compra Items: " + c.getItems().size());
                for(CompraItem item : c.getItems()) {
                    logger.info("Item: " + item.getProducto().getNombreProducto());
                    total = total.add(item.getPrecioCosto());
                }
                c.setTotalFactura(total);
            }

        } catch(Exception e) {
            logger.error("Error al Obtener Registros: ");
            listResult = new ArrayList<>();
        }
        return listResult;
    }

    @Override
    public List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, OffsetBasedPageRequest offsetBasedPageRequest) {
        String methodName = "findAll-Compra";
        logger.info("--> "+methodName);
        dateStart = dateStart == null ? LocalDate.now() : dateStart;
        LocalDateTime dateStartTime = dateStart.atTime(0,0,0);
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        LocalDateTime dateEndTime = dateEnd.atTime(23,59,59);
        logger.info("Fecha Inicio: "+java.sql.Timestamp.valueOf(dateStartTime));
        logger.info("Fecha Fin: "+java.sql.Timestamp.valueOf(dateEndTime));
        List<Compra> listResult;
        try {
            if (filterText == null || filterText.isEmpty()) {
                listResult = compraRepository.findAllByEmpresa(empresa,
                        java.sql.Timestamp.valueOf(dateStartTime),
                        java.sql.Timestamp.valueOf(dateEndTime),
                        offsetBasedPageRequest);
            } else {
                listResult = compraRepository.search(empresa,
                        filterText,
                        java.sql.Timestamp.valueOf(dateStartTime),
                        java.sql.Timestamp.valueOf(dateEndTime),
                        offsetBasedPageRequest);
            }

            logger.info("Cantidad de Registros: " + listResult.size());

        } catch(Exception e) {
            logger.error("Error al Obtener Registros: ");
            listResult = new ArrayList<>();
        }
        return listResult;
    }

    
    //@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Response saveCompra(Compra compra, Empresa empresa, Usuario usuario) {
        logger.info("--> saveCompra");
        
        Response result = new Response();
        List<CompraItem> listItems = compra.getItems();
        try {

            Compra compratemp = compraRepository.findByNumeroFacturaAndEmpresa(compra.getNumeroFactura(), empresa);
            if(compratemp != null) {
                result.setSuccess(false);
                result.setMessage("# Numero de Factura: "+compra.getNumeroFactura() +" - Ya existe");

            } else {
                compra.setUsuario(usuario);
                compra.setFechaCreacion(new Date());
                compra.setItems(null);

                BigDecimal totalFactura = BigDecimal.ZERO;
                for (CompraItem item : listItems) {
                    totalFactura = totalFactura.add(item.getPrecioTotalCosto());
                }
                compra.setTotalFactura(totalFactura);

                transactionCompra(compra, result, listItems);
            }
        } catch(Exception exception) {
            logger.error("[Exception]: "+exception.getMessage(), exception);
            result.setSuccess(false);
            result.setMessage("Error al guardar Compra: "+exception.getMessage());
        }
        logger.info("<-- saveCompra");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    private void transactionCompra(Compra compra, Response result, List<CompraItem> listItems) throws Exception {
         try{
            compra = compraBusinessService.saveCompra(compra);
            if (compra != null) {

                for (CompraItem item : listItems) {

                    boolean updatePrice = false;
                    //Validar Valor Venta
                    ProductoValorVenta productoValorVenta = productoValorVentaService.findByProductoAndActivoIsTrue(item.getProducto());
                    if (productoValorVenta != null) {
                        if (productoValorVenta.getValorVenta().compareTo(item.getPrecioVenta()) != 0) {
                            updatePrice = true;
                        }
                    } else {
                        updatePrice = true;
                    }

                    item.setCompra(compra);
                    item.setPrecioCosto(item.getPrecioTotalCosto().divide(item.getCantidad(), 2, RoundingMode.HALF_UP));
                    //Guardando Item de Compra
                    compraBusinessService.saveCompraItem(item);


                        boolean processInventario = inventarioService.saveProcessInventarioActualAndPrecios(item.getProducto(),
                            true,
                            item.getCantidad(),
                            item.getPrecioVenta(),
                            item.getPrecioCosto(),
                            TipoMovimientoEnum.COMPRA,
                            updatePrice,
                            true,
                            item.getImpuestoArticulo() != null ? item.getImpuestoArticulo() : BigDecimal.ZERO,
                            "");
                            
                            if(!processInventario) {
                                throw new Exception("Error al procesar actualización de Inventarios");
                            }   

                }

                result.setSuccess(true);
                result.setMessage("Compra " + compra.getNumeroFactura() + " Guardada Exitosamente");
            } else {
                result.setSuccess(false);
                result.setMessage("No fue posible guardar la Compra");
            }
        } catch(RuntimeException r) {
            logger.error("fallo Transacción de Compra: "+r.getMessage(), r);
            throw r;
        } catch(Exception e) {
            logger.error("fallo Transacción de Compra: "+e.getMessage(), e);
            
            throw e;
        } 
    }



    @Override
    public Response delete(String id) {
        Response response = new Response();
        return response;
        //TODO Make ELiminar
//        try {
//            Compra compra = compraRepository.findById(id).orElse(null);
//            if (compra != null) {
//                compra.setEliminado(true);
//                compra = usuarioRepository.save(compra);
//                if(compra != null) {
//                    response.setSuccess(true);
//                    response.setMessage("Usuario Eliminado Correctamente");
//                } else {
//                    response.setSuccess(false);
//                    response.setMessage("No Fue Posible eliminar Usuario");
//                }
//            } else {
//                response.setSuccess(false);
//                response.setMessage("Usuario No encontrado para eliminar");
//            }
//        } catch(Exception e) {
//            logger.log(Level.SEVERE,"Error: "+e.getMessage(),e);
//            response.setSuccess(false);
//            response.setMessage("Error al Eliminar Compra");
//        }
//        return response;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllCompras(Empresa emp, List<Producto> productosList) {
        logger.info("--> deleteAllCompras");
        try {
            logger.info("CompraItems Delete: "+compraItemRepository.deleteAllByProductoIn(productosList));
            logger.info("Compra Delete: "+compraRepository.deleteAllByEmpresa(emp));
            compraItemRepository.flush();
            compraRepository.flush();

            logger.info("<-- deleteAllCompras");
            return true;
        }catch(Exception e) {
            logger.error("Error al eliminar Compras: "+e.getMessage(), e);
            return false;
        }
    }
}
