package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.Utils;
import com.gigti.xfinance.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private InventarioService inventarioService;

    @Override
    public List<PventaDTO> findAll(String filter, Empresa empresa, int page, int size) {
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
            venta.setTotalVenta(BigDecimal.valueOf(listVenta.stream().mapToDouble(p -> p.getSubTotal().doubleValue()).sum()));
            //venta.setD
            ventaRepository.save(venta);
            List<VentaItem> listItems = new ArrayList<>();

            //Items Factura
            listVenta.forEach(pv -> {
                Producto producto = productoRepository.findById(pv.getIdProducto()).orElse(null);

                VentaItem item = new VentaItem();
                item.setVenta(venta);
                item.setProducto(producto);
                item.setCantidad(pv.getCantidadVenta());
                item.setPrecioCosto(pv.getPrecioCostoActual());
                item.setPrecioVenta(pv.getPrecioVentaActual());
                item.setItem(pv.getItem());
                //item.setDescuentoArticulo(pv.getDescuento());
                item.setImpuestoArticulo(pv.getImpuestoValor());
                item.setImpuestoNombre(pv.getImpuestoNombre());

                listItems.add(item);

                inventarioService.saveProcessInventarioActualAndPrecios(producto,
                        false,
                        pv.getCantidadVenta(),
                        pv.getPrecioVentaActual(),
                        pv.getPrecioCostoActual(),
                        TipoMovimientoEnum.VENTA,
                        false,
                        pv.isInfinite(),
                        pv.getImpuestoValor(),
                        pv.getImpuestoNombre());
            });

            ventaItemRepository.saveAll(listItems);
            return venta;
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error: al generar Factura: "+e.getMessage(), e);
            return null;
        }
    }

    private List<PventaDTO> getListPventaDTO(String filter, Empresa empresa, Pageable pageable){
        String methodName = "getListPventaDTO";
        logger.info("--> "+methodName);
        List<Producto> listProductos = new ArrayList<>();
        if(filter == null || filter.isEmpty()) {
            listProductos = productoRepository.findByEmpresa(empresa, pageable);
        } else {
            listProductos = productoRepository.search(filter, empresa, pageable);
        }
        List<PventaDTO> result = new ArrayList<>();

        for(Producto p : listProductos) {
            BigDecimal inStock = BigDecimal.ZERO;
            boolean infinite = false;
            InventarioActual actual = inventarioActualRepository.findByProducto(p);
            if(actual != null) {
                inStock = actual.getCantidad().compareTo(BigDecimal.ZERO) > 0 ? actual.getCantidad() : BigDecimal.ZERO;
                infinite = actual.isInfinite();
            }
            if(inStock.compareTo(BigDecimal.ZERO) > 0 || infinite) {
                ProductoValor pvalores = productoValoresRepository.findByProductoAndActivoIsTrue(p);
                if (pvalores != null) {
                    result.add(convertProductoToPventaDTO(p, pvalores, inStock, infinite));
                }
            }
        }
        logger.info("<-- "+methodName);
        return result;
    }

    private PventaDTO convertProductoToPventaDTO(Producto p, ProductoValor pvalores, BigDecimal inStock, boolean infinite){
        PventaDTO pv = new PventaDTO();
        pv.setIdProducto(p.getId());
        pv.setCodigoBarra(p.getCodigoBarra());
        pv.setNombreProducto(p.getNombreProducto());
        pv.setCantidadVenta(BigDecimal.ZERO);
        pv.setPrecioCostoActual(pvalores.getPrecioCosto());
        pv.setPrecioVentaActual(pvalores.getPrecioVenta());
        pv.setInStock(inStock);
        pv.setUnidadMedida(p.getTipoMedida() == null ? "NE" : p.getTipoMedida().toString());
        pv.setImpuestoId(p.getImpuesto().getId());
        pv.setImpuestoValor(p.getImpuesto().getValor());
        pv.setImpuestoNombre(p.getImpuesto().getNombre());
        pv.setInfinite(infinite);

        return pv;
    }

}
