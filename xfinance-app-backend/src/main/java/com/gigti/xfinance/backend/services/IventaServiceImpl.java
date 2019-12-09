package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.Utils;
import com.gigti.xfinance.backend.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IventaServiceImpl implements IventaService {

    Logger logger = LoggerFactory.getLogger(IinitBackServiceImpl.class);

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ItemFacturaRepository itemFacturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private ProductoInvDiaRepository productoInvDiaRepository;

    @Autowired
    private ProductoInvInicioRepository productoInvInicioRepository;

    @Override
    public List<PventaDTO> find100MostImportant(Empresa empresa) {
        Pageable pageable = PageRequest.of(0, 100);
        return getListPventaDTO(empresa, pageable);
    }

    @Override
    public List<PventaDTO> findAll(Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getListPventaDTO(empresa, pageable);
    }

    @Override
    @Transactional
    public Factura registrarFactura(Usuario usuario, List<PventaDTO> listVenta) {
        try {
            Factura factura = new Factura();
            factura.setUsuario(usuario);
            factura.setFechaCreacion(new Date());
            //TODO Luego cambiar por # Factura de Dian
            long cantidadFacturasxEmpresa = facturaRepository.countByUsuario_Empresa(usuario.getEmpresa());
            cantidadFacturasxEmpresa++;
            factura.setNumeroFacturaInterno(cantidadFacturasxEmpresa);
            factura.setNumeroFactura(Utils.generateNumberTicket(usuario.getEmpresa().getIdInterno(), cantidadFacturasxEmpresa));
            factura.setTotalFactura(listVenta.stream().mapToDouble(p -> p.getSubTotal().doubleValue()).sum());
            facturaRepository.save(factura);
            List<ItemFactura> listItems = new ArrayList<>();
            //Items Factura
            for (PventaDTO pv : listVenta) {
                Producto producto = productoRepository.findById(pv.getIdProducto()).orElse(null);

                ItemFactura item = new ItemFactura();
                item.setFactura(factura);
                item.setProducto(producto);
                item.setCantidad(pv.getCantidadVenta());
                item.setPrecioCosto(pv.getPrecioCostoActual());
                item.setPrecioVenta(pv.getPrecioVentaActual());
                item.setItem(pv.getItem());
                listItems.add(item);

                //descontar cantidad del Inventario
                double inStock = 0;
                ProductoInventarioDia pid = productoInvDiaRepository.findByProducto(producto);
                if(pid == null) {
                    ProductoInventarioInicio pii = productoInvInicioRepository.findByProducto(producto);
                    if(pii == null){
                        pid = new ProductoInventarioDia();
                        pid.setQuantity(pv.getCantidadVenta() * -1);
                        pid.setProducto(producto);
                        pid.setTrackingDate(new Date());
                        productoInvDiaRepository.save(pid);
                    } else {
                        pid = new ProductoInventarioDia();
                        pid.setQuantity(pii.getQuantity() - pv.getCantidadVenta());
                        pid.setProducto(producto);
                        pid.setTrackingDate(new Date());
                        productoInvDiaRepository.save(pid);
                    }
                } else {
                    pid.setQuantity(pid.getQuantity() - pv.getCantidadVenta());
                    pid.setProducto(producto);
                    pid.setTrackingDate(new Date());
                    productoInvDiaRepository.save(pid);
                }
            }
            itemFacturaRepository.saveAll(listItems);
            return factura;
        }catch(Exception e){
            logger.error("Error: al generar Factura: "+e.getMessage(), e);
            return null;
        }
    }

    private List<PventaDTO> getListPventaDTO(Empresa empresa,Pageable pageable){
        List<Producto> list = productoRepository.findByEmpresa(empresa, pageable);
        List<PventaDTO> listDTO = new ArrayList<>();

        for(Producto p : list){
            double inStock = 0;
            ProductoInventarioDia pid = productoInvDiaRepository.findByProducto(p);
            if(pid == null) {
                ProductoInventarioInicio pii = productoInvInicioRepository.findByProducto(p);
                if(pii == null){
                    continue;
                } else {
                    inStock = pii.getQuantity() > 0 ? pii.getQuantity() : 0;
                }
            } else {
                inStock = pid.getQuantity() > 0 ? pid.getQuantity() : 0;
            }
            if(inStock > 0) {
                ProductoValores pvalores = productoValoresRepository.findByProductoAndActivoIsTrue(p);
                if (pvalores != null) {
                    listDTO.add(convertProductoToPventaDTO(p, pvalores, inStock));
                }
            }
        }

        return listDTO;
    }

    private PventaDTO convertProductoToPventaDTO(Producto p, ProductoValores pvalores, double inStock){
        PventaDTO pv = new PventaDTO();
        pv.setIdProducto(p.getId());
        pv.setCodigoBarra(p.getCodigoBarra());
        pv.setNombreProducto(p.getNombreProducto());
        pv.setCantidadVenta(0);
        pv.setPrecioCostoActual(pvalores.getPrecioCosto());
        pv.setPrecioVentaActual(pvalores.getPrecioVenta());
        pv.setInStock(inStock);

        return pv;
    }

}
