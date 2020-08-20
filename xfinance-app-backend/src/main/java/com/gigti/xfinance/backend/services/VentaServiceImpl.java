package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private static final Logger logger = LoggerFactory.getLogger(VentaServiceImpl.class);

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaItemRepository ventaItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private InventarioActualCostoRepository inventarioActualCostoRepository;
    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private InventarioService inventarioService;

    @Override
    public List<PventaDTO> findByName(String filter, Empresa empresa, int page, int size) {
        logger.info("--> findByName");
        Pageable pageable = PageRequest.of(page, size);
        return getListPventaDTO(filter, empresa, pageable);
    }

    @Override
    public PventaDTO findByBarCode(String filter, Empresa empresa) {
        logger.info("--> findByBarCode");
        return getPventaDTO(filter, empresa);
    }

    @Override
    public List<PventaDTO> findByBarCodeAndName(String filter, Empresa empresa, int page, int size) {
        logger.info("--> findByBarCodeAndName");
        Pageable pageable = PageRequest.of(page, size);
        PventaDTO pventa = getPventaDTO(filter, empresa);
        if(pventa != null) {
            List<PventaDTO> lista = new ArrayList<>();
            lista.add(pventa);
            return lista;
        } else {
            return getListPventaDTO(filter, empresa, pageable);
        }
    }

    @Override
    public int count(Empresa empresa) {
        return productoRepository.countByEmpresa(empresa);
    }

    @Override
    @Transactional
    public Venta registrarVenta(Usuario usuario, List<PventaDTO> listVenta) {
        logger.info("--> Registrar Venta");
        logger.info("--> Items Venta: "+listVenta.size());
        try {
            Venta venta = new Venta();
            venta.setUsuario(usuario);
            venta.setFechaCreacion(new Date());
            //TODO Luego cambiar por # Factura de Dian
            // TODO Agregar luego iniciar factura en Numero XXX definido por los usuarios
            long cantidadFacturasxEmpresa = ventaRepository.countByUsuarioEmpresa(usuario.getPersona().getEmpresa());
            cantidadFacturasxEmpresa++;
            venta.setNumeroFacturaInterno(cantidadFacturasxEmpresa);
            venta.setNumeroFactura(UtilsBackend.generateNumberTicket(cantidadFacturasxEmpresa));
            venta.setTotalVenta(BigDecimal.valueOf(listVenta.stream().mapToDouble(p -> p.getSubTotal().doubleValue()).sum()));
            //venta.setD
            ventaRepository.save(venta);
            List<VentaItem> listItems = new ArrayList<>();

            //Items Factura
            listVenta.forEach(pv -> {
                logger.info("Item Venta: "+pv.toString());
                Producto producto = productoRepository.findById(pv.getIdProducto()).orElse(null);
                List<InventarioActualCosto> listInvActualCosto = inventarioActualCostoRepository.findByProductoOrderByFechaCreacionAsc(producto);
                logger.info("Inventarios de Venta: "+listInvActualCosto.toString());
                InventarioActualCosto invActualCosto;

                if(listInvActualCosto.size() > 1) {
                    invActualCosto = UtilsBackend.extractInvActCostByDate(listInvActualCosto);
                } else if(listInvActualCosto.size() == 1) {
                    invActualCosto = listInvActualCosto.get(0);
                } else {
                    invActualCosto = null;
                }
                BigDecimal cantidad = pv.getCantidadVenta();
                if(invActualCosto != null && !invActualCosto.isInfinite() && invActualCosto.getCantidad().compareTo(cantidad) < 0) {
                    while (cantidad.compareTo(BigDecimal.ZERO) > 0) {
                        VentaItem item = new VentaItem();
                        if(invActualCosto.getCantidad().compareTo(cantidad) < 0) {
                            cantidad = cantidad.subtract(invActualCosto.getCantidad());
                            invActualCosto = UtilsBackend.extractInvActCostByDateAndDiff(listInvActualCosto, invActualCosto.getId());
                        } else {
                            item.setCantidad(cantidad);
                            cantidad = BigDecimal.ZERO;
                        }

                        item.setVenta(venta);
                        item.setProducto(producto);
                        item.setPrecioCosto(invActualCosto.getPrecioCosto());
                        item.setPrecioVenta(pv.getPrecioVentaActual());
                        //item.setDescuentoArticulo(pv.getDescuento());
                        item.setImpuestoArticulo(pv.getImpuestoValor());
                        item.setImpuestoNombre(pv.getImpuestoNombre());

                        listItems.add(item);
                    }
                } else if(invActualCosto != null && invActualCosto.isInfinite()){
                    VentaItem item = new VentaItem();
                    item.setVenta(venta);
                    item.setProducto(producto);
                    item.setCantidad(pv.getCantidadVenta());
                    item.setPrecioCosto(invActualCosto.getPrecioCosto());
                    item.setPrecioVenta(pv.getPrecioVentaActual());
                    //item.setDescuentoArticulo(pv.getDescuento());
                    item.setImpuestoArticulo(pv.getImpuestoValor());
                    item.setImpuestoNombre(pv.getImpuestoNombre());

                    listItems.add(item);
                }

                inventarioService.saveProcessInventarioActualAndPrecios(producto,
                        false,
                        pv.getCantidadVenta(),
                        pv.getPrecioVentaActual(),
                        BigDecimal.ZERO,
                        TipoMovimientoEnum.VENTA,
                        false,
                        pv.isInfinite(),
                        pv.getImpuestoValor(),
                        pv.getImpuestoNombre());
            });

            ventaItemRepository.saveAll(listItems);
            logger.info("<-- Registrar Venta");
            return venta;
        }catch(Exception e){
            logger.error("Error: al generar Factura: "+e.getMessage(), e);
            return null;
        }
    }

    private List<PventaDTO> getListPventaDTO(String filter, Empresa empresa, Pageable pageable){
        String methodName = "getListPventaDTO";
        logger.info("--> "+methodName);
        List<Producto> listProductos;
        if(filter == null || filter.isEmpty()) {
            listProductos = productoRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
        } else {
            listProductos = productoRepository.search(filter, empresa, pageable);
        }
        List<PventaDTO> result = new ArrayList<>();

        listProductos.forEach(p -> {
            BigDecimal inStock = BigDecimal.ZERO;
            boolean infinite = false;
            InventarioActual actual = inventarioActualRepository.findByProducto(p);
            if(actual != null) {
                inStock = actual.getCantidad().compareTo(BigDecimal.ZERO) > 0 ? actual.getCantidad() : BigDecimal.ZERO;
                infinite = actual.isInfinite();
            }
            if(inStock.compareTo(BigDecimal.ZERO) > 0 || infinite) {
                ProductoValorVenta productoValorVenta = productoValoresRepository.findByProductoAndActivoIsTrue(p);
                if (productoValorVenta != null) {
                    result.add(convertProductoToPventaDTO(p, productoValorVenta.getValorVenta(), inStock, infinite));
                } else {
                    result.add(convertProductoToPventaDTO(p, BigDecimal.ZERO, inStock, infinite));
                }
            }
        });
        logger.info("<-- "+methodName);
        return result;
    }

    private PventaDTO getPventaDTO(String filter, Empresa empresa){
        String methodName = "getPventaDTO";
        logger.info("--> "+methodName);

        Producto producto = productoRepository.findByBarCode(filter, empresa);
        PventaDTO result = null;
        if(producto != null) {

            BigDecimal inStock = BigDecimal.ZERO;
            boolean infinite = false;
            InventarioActual actual = inventarioActualRepository.findByProducto(producto);
            if (actual != null) {
                inStock = actual.getCantidad().compareTo(BigDecimal.ZERO) > 0 ? actual.getCantidad() : BigDecimal.ZERO;
                infinite = actual.isInfinite();
            }
            if (inStock.compareTo(BigDecimal.ZERO) > 0 || infinite) {
                ProductoValorVenta productoValorVenta = productoValoresRepository.findByProductoAndActivoIsTrue(producto);
                if (productoValorVenta != null) {
                    result = convertProductoToPventaDTO(producto, productoValorVenta.getValorVenta(), inStock, infinite);
                } else {
                    result = convertProductoToPventaDTO(producto, BigDecimal.ZERO, inStock, infinite);
                }
            }
        }
        logger.info("<-- "+methodName);
        return result;
    }

    private PventaDTO convertProductoToPventaDTO(Producto p, BigDecimal precioVenta, BigDecimal inStock, boolean infinite){
        PventaDTO pv = new PventaDTO();
        pv.setIdProducto(p.getId());
        pv.setCodigoBarra(p.getCodigoBarra());
        pv.setNombreProducto(p.getNombreProducto());
        pv.setCantidadVenta(BigDecimal.ZERO);
        pv.setPrecioVentaActual(precioVenta);
        pv.setInStock(inStock);
        pv.setUnidadMedida(p.getTipoMedida() == null ? "N.D" : p.getTipoMedida().getSimbolo());
        pv.setImpuestoId(p.getImpuesto().getId());
        pv.setImpuestoValor(p.getImpuesto().getValor());
        pv.setImpuestoNombre(p.getImpuesto().getNombre());
        pv.setInfinite(infinite);

        return pv;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllVentas(Empresa emp, List<Producto> productoList) {
        logger.info("--> deleteAllVentas");
        try {

            Integer cantidad = ventaItemRepository.deleteAllByProductoIn(productoList);
            logger.info("Items Delete: "+cantidad);
            logger.info("Ventas Delete: "+ventaRepository.deleteAllByEmpresa(emp));
            ventaItemRepository.flush();
            ventaRepository.flush();
            logger.info("<-- deleteAllVentas");
            return true;
        }catch(Exception e) {
            logger.error("Error al eliminar ventas: "+e.getMessage(), e);
            return false;
        }
    }

}
