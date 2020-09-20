package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.CompraItemRepository;
import com.gigti.xfinance.backend.repositories.CompraRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraItemRepository compraItemRepository;

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

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

            for(Compra c : listResult) {
                BigDecimal total = BigDecimal.ZERO;
                logger.info("Compra: " + c.getNumeroFactura());
                logger.info("Compra Items: " + c.getItems().size());
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

    @Transactional
    @Override
    public Response saveCompra(Compra compra, Empresa empresa, Usuario usuario) {
        logger.info("--> saveCompra");
        Response result = new Response();
        List<CompraItem> listItems = compra.getItems();
        try {
            compra.setUsuario(usuario);
            compra.setFechaCreacion(new Date());
            compra.setItems(null);
            compra = compraRepository.save(compra);
            if (compra != null) {
                Compra finalCompra = compra;
                listItems.forEach(item -> {
                    item.setCompra(finalCompra);
                    item.setPrecioCosto(item.getPrecioTotalCosto().divide(item.getCantidad()));
                });
                compraItemRepository.saveAll(listItems);

                listItems.forEach(item -> {

                    boolean updatePrice = false;
                    //Validar Valor Venta
                    ProductoValorVenta productoValorVenta = productoValoresRepository.findByProductoAndActivoIsTrue(item.getProducto());
                    if(productoValorVenta != null){
                        if(productoValorVenta.getValorVenta().compareTo(item.getPrecioVenta()) != 0){
                            updatePrice = true;
                        }
                    } else {
                        updatePrice = true;
                    }
                            inventarioService.saveProcessInventarioActualAndPrecios(item.getProducto(),
                                    true,
                                    item.getCantidad(),
                                    item.getPrecioVenta(),
                                    item.getPrecioCosto(),
                                    TipoMovimientoEnum.COMPRA,
                                    updatePrice,
                                    false,
                                    item.getImpuestoArticulo() != null ? item.getImpuestoArticulo() : BigDecimal.ZERO,
                                    "");
                        }
                );

                result.setSuccess(true);
                result.setMessage("Compra "+compra.getNumeroFactura() + " Guardada Exitosamente");
            } else {
                result.setSuccess(false);
                result.setMessage("No fue posible guardar la Compra");
            }
        }catch(Exception e) {
            logger.error("[Exception]: "+e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("Error al guardar Compra");
        }
        logger.info("<-- saveCompra");
        return result;
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
