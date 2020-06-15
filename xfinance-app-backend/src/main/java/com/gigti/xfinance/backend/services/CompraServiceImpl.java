package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.CompraItemRepository;
import com.gigti.xfinance.backend.repositories.CompraRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = Logger.getLogger(CompraServiceImpl.class.getName());

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
        dateStart = dateStart == null ? LocalDate.of(2020,1,1) : dateStart;
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        if(filterText == null || filterText.isEmpty()) {
            count = compraRepository.countAllByEmpresa(empresa,
                    java.sql.Date.valueOf(dateStart),
                    java.sql.Date.valueOf(dateEnd));
        } else  {
            count = compraRepository.countAllByEmpresaAndNumeroFactura(empresa,
                    filterText,
                    java.sql.Date.valueOf(dateStart),
                    java.sql.Date.valueOf(dateEnd));
        }

        return count;
    }

    @Override
    public List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, int page, int size) {
        String methodName = "findAll-Compra";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        dateStart = dateStart == null ? LocalDate.of(2020,1,1) : dateStart;
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        List<Compra> listResult;
        try {
            if (filterText == null || filterText.isEmpty()) {
                listResult = compraRepository.findAllByEmpresa(empresa,
                        java.sql.Date.valueOf(dateStart),
                        java.sql.Date.valueOf(dateEnd),
                        pageable);
            } else {
                listResult = compraRepository.search(empresa,
                        filterText,
                        java.sql.Date.valueOf(dateStart),
                        java.sql.Date.valueOf(dateEnd),
                        pageable);
            }

            logger.log(Level.INFO, "Cantidad de Registros: " + listResult.size());

            for(Compra c : listResult) {
                BigDecimal total = BigDecimal.ZERO;
                logger.log(Level.INFO, "Compra: " + c.getNumeroFactura());
                logger.log(Level.INFO, "Compra Items: " + c.getItems().size());
                for(CompraItem item : c.getItems()) {
                    logger.log(Level.INFO, "Item: " + item.getProducto().getNombreProducto());
                    total = total.add(item.getPrecioCosto());
                }
                c.setTotalFactura(total);
            }

//            listResult.stream()
//                    .peek(c -> System.out.println(c.getItems().size()))
//                    .forEach(c -> c.setTotalFactura(BigDecimal.valueOf(c.getItems().stream().mapToDouble(i -> i.getPrecioCosto().doubleValue()).sum())));
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error al Obtener Registros: ");
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
            logger.log(Level.SEVERE, "[Exception]: "+e.getMessage(), e);
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
}
