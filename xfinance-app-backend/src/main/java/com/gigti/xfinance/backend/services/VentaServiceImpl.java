package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.Utils;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import com.gigti.xfinance.backend.repositories.VentaItemRepository;
import com.gigti.xfinance.backend.repositories.VentaRepository;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VentaServiceImpl implements VentaService {

    private static final Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaItemRepository ventaItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

//    @Override
//    public List<PventaDTO> find100MostImportant(Empresa empresa) {
//        Pageable pageable = PageRequest.of(0, 100);
//        return getListPventaDTO(empresa, pageable);
//    }

    @Override
    public ListDataProvider<PventaDTO> findAll(String filter, Empresa empresa, int page, int size) {
        logger.info("--> findAll");
        Pageable pageable = PageRequest.of(page, size);
        return getListPventaDTO(filter, empresa, pageable);
    }

    @Override
    public int count(Empresa empresa) {
        return productoRepository.countByEmpresa(empresa);
    }

    @Override
    @Transactional
    public Venta registrarVenta(Usuario usuario, List<PventaDTO> listVenta) {
        try {
            Venta venta = new Venta();
            venta.setUsuario(usuario);
            venta.setFechaCreacion(new Date());
            //TODO Luego cambiar por # Factura de Dian
            // TODO Agregar luego iniciar factura en Numero XXX definido por los usuarios
            long cantidadFacturasxEmpresa = ventaRepository.countByUsuario_Empresa(usuario.getEmpresa());
            cantidadFacturasxEmpresa++;
            venta.setNumeroFacturaInterno(cantidadFacturasxEmpresa);
            venta.setNumeroFactura(Utils.generateNumberTicket(cantidadFacturasxEmpresa));
//            venta.setTotalVenta(listVenta.stream().mapToDouble(p -> p.getSubTotal().doubleValue()).sum());
            ventaRepository.save(venta);
            List<VentaItem> listItems = new ArrayList<>();
            //Items Factura
            for (PventaDTO pv : listVenta) {
                Producto producto = productoRepository.findById(pv.getIdProducto()).orElse(null);

                VentaItem item = new VentaItem();
                item.setVenta(venta);
                item.setProducto(producto);
                item.setCantidad(pv.getCantidadVenta());
                item.setPrecioCosto(pv.getPrecioCostoActual());
                item.setPrecioVenta(pv.getPrecioVentaActual());
                item.setItem(pv.getItem());
                listItems.add(item);

//                //descontar cantidad del Inventario
//                double inStock = 0;
//                ProductoInventarioDia pid = productoInvDiaRepository.findByProducto(producto);
//                if(pid == null) {
//                    ProductoInventarioInicio pii = productoInvInicioRepository.findByProducto(producto);
//                    if(pii == null){
//                        pid = new ProductoInventarioDia();
//                        pid.setQuantity(pv.getCantidadVenta() * -1);
//                        pid.setProducto(producto);
//                        pid.setTrackingDate(new Date());
//                        productoInvDiaRepository.save(pid);
//                    } else {
//                        pid = new ProductoInventarioDia();
//                        pid.setQuantity(pii.getQuantity() - pv.getCantidadVenta());
//                        pid.setProducto(producto);
//                        pid.setTrackingDate(new Date());
//                        productoInvDiaRepository.save(pid);
//                    }
//                } else {
//                    pid.setQuantity(pid.getQuantity() - pv.getCantidadVenta());
//                    pid.setProducto(producto);
//                    pid.setTrackingDate(new Date());
//                    productoInvDiaRepository.save(pid);
//                }
            }
            ventaItemRepository.saveAll(listItems);
            return venta;
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error: al generar Factura: "+e.getMessage(), e);
            return null;
        }
    }

    private ListDataProvider<PventaDTO> getListPventaDTO(String filter, Empresa empresa, Pageable pageable){
        String methodName = "getListPventaDTO";
        logger.info("--> "+methodName);
        List<Producto> result = new ArrayList<>();
        if(filter == null || filter.isEmpty()) {
            result = productoRepository.findByEmpresa(empresa, pageable);
        } else {
            result = productoRepository.search(filter, empresa, pageable);
        }
        Collection<PventaDTO> listDTO = new ArrayList<>();

        for(Producto p : result) {
            double inStock = 0;
//            ProductoInventarioDia pid = productoInvDiaRepository.findByProducto(p);
//            if(pid == null) {
//                ProductoInventarioInicio pii = productoInvInicioRepository.findByProducto(p);
//                if(pii == null){
//                    continue;
//                } else {
//                    inStock = pii.getQuantity() > 0 ? pii.getQuantity() : 0;
//                }
//            } else {
//                inStock = pid.getQuantity() > 0 ? pid.getQuantity() : 0;
//            }
            if(inStock > 0) {
                ProductoValor pvalores = productoValoresRepository.findByProductoAndActivoIsTrue(p);
                if (pvalores != null) {
                    listDTO.add(convertProductoToPventaDTO(p, pvalores, inStock));
                }
            }
        }
        logger.info("<-- "+methodName);
        return new ListDataProvider<>(listDTO);
    }

    private PventaDTO convertProductoToPventaDTO(Producto p, ProductoValor pvalores, double inStock){
        PventaDTO pv = new PventaDTO();
        pv.setIdProducto(p.getId());
        pv.setCodigoBarra(p.getCodigoBarra());
        pv.setNombreProducto(p.getNombreProducto());
        pv.setCantidadVenta(0);
//        pv.setPrecioCostoActual(pvalores.getPrecioCosto());
        pv.setPrecioVentaActual(pvalores.getPrecioVenta());
        pv.setInStock(inStock);
        pv.setUnidadMedida(p.getTipoMedida() == null ? "NE" : p.getTipoMedida().toString());

        return pv;
    }

}
