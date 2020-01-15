/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.TipoMedidaEnum;
import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.repositories.ProductoInvDiaRepository;
import com.gigti.xfinance.backend.repositories.ProductoInvInicioRepository;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoInvDiaRepository productoInvDiaRepository;

    @Autowired
    private ProductoInvInicioRepository productoInvInicioRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Transactional
    public Producto saveProduct(Producto producto, Usuario usuario) {
        //TODO Guardar Valores de Costos y Venta - así como la Cantidad del producto
        boolean isNew = StringUtils.isBlank(producto.getId());
        Producto result = productoRepository.save(producto);
        if (result != null) {
            if (isNew) {
                //Guarda el Inventario Inicial al Crear el Producto
                ProductoInventarioInicio pid = new ProductoInventarioInicio(result, producto.getStockActual(), new Date(), usuario);
                pid = productoInvInicioRepository.save(pid);
                result.setStockActual(pid.getQuantity());
            }
            // Se guarda Valores de Costo y Venta
            ProductoValores pval = productoValoresRepository.findByProductoAndActivoIsTrue(result);
            boolean pvalExists = true;
            if (pval == null) {
                pval = new ProductoValores(result, producto.getPrecioCostoActual(), producto.getPrecioVentaActual(),
                        new Date(), true, Float.parseFloat("0.0"));
                pvalExists = false;
            }
            //validar que no se igual a lo que esta activo
            if (pval.getPrecioCosto().compareTo(producto.getPrecioCostoActual()) != 0 ||
                    pval.getPrecioVenta().compareTo(producto.getPrecioVentaActual()) != 0) {
                if(pvalExists){
                    pval.setActivo(false);
                    productoValoresRepository.save(pval);
                    ProductoValores pvalNew = new ProductoValores(result, producto.getPrecioCostoActual(), producto.getPrecioVentaActual(), new Date(), true, Float.valueOf("0"));
                    productoValoresRepository.save(pvalNew);
                    result.setPrecioVentaActual(pvalNew.getPrecioVenta());
                    result.setPrecioCostoActual(pvalNew.getPrecioCosto());
                } else {
                    pval.setActivo(true);
                    productoValoresRepository.save(pval);
                    result.setPrecioVentaActual(pval.getPrecioVenta());
                    result.setPrecioCostoActual(pval.getPrecioCosto());
                }
            } else {
                if(!pvalExists){
                    pval.setActivo(true);
                    productoValoresRepository.save(pval);
                    result.setPrecioVentaActual(pval.getPrecioVenta());
                    result.setPrecioCostoActual(pval.getPrecioCosto());
                }
            }
        }

        return result;
    }

    @Transactional
    public boolean delete(String id) {
        try {
            Producto producto = productoRepository.findById(id).orElse(null);
            if (producto != null) {
                producto.setEliminado(true);
                producto = productoRepository.save(producto);
                return producto != null;
            }
        } catch (Exception e) {
            logger.debug("Error: " + e.getMessage(), e);
        }
        return false;
    }

    public List<Producto> findByNombreProducto(Empresa empresa, String productName) {
        return productoRepository.findByEmpresaAndNombreProducto(empresa, productName);
    }

    public List<Producto> findAll(Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Producto> result = new ArrayList<>();
        //Calcula la Cantidad Actual
        for (Producto p : productoRepository.findByEmpresa(empresa, pageable)) {
            ProductoInventarioDia pid = productoInvDiaRepository.findByProducto(p);
            if (pid != null) {
                p.setStockActual(pid.getQuantity());
            } else {
                ProductoInventarioInicio pii = productoInvInicioRepository.findByProducto(p);
                if (pii != null) {
                    p.setStockActual(pii.getQuantity());
                } else {
                    p.setStockActual(0);
                }
            }

            //Obtiene Costo y Venta del producto actual
            ProductoValores pval = productoValoresRepository.findByProductoAndActivoIsTrue(p);
            if (pval != null) {
                p.setPrecioCostoActual(pval.getPrecioCosto());
                p.setPrecioVentaActual(pval.getPrecioVenta());
            } else {
                p.setPrecioCostoActual(BigDecimal.ZERO);
                p.setPrecioVentaActual(BigDecimal.ZERO);
            }
            result.add(p);
        }

        return result;
    }

    public Producto findByBarCode(Empresa empresa, String barCode) {
        return productoRepository.findByEmpresaAndCodigoBarra(empresa, barCode);
    }

    public List<Producto> findByNameOrBarCode(Empresa empresa, String anything) {
        return productoRepository.findByEmpresaAndNombreProductoContainingOrCodigoBarraContaining(empresa, anything, anything);
    }

    @Override
    public Producto findById(String id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<TipoMedidaEnum> getAllTipoMedidaEnum(){
        List<TipoMedidaEnum> lista = new ArrayList<>();
        lista.add(TipoMedidaEnum.UNIDAD);
        lista.add(TipoMedidaEnum.PAQUETE);
        lista.add(TipoMedidaEnum.KILO);
        lista.add(TipoMedidaEnum.LIBRA);

        return lista;
    }
}
