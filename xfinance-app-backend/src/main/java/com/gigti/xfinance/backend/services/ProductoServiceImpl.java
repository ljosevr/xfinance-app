/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.TipoMedidaEnum;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.repositories.InventarioInicialRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;

//    @Autowired
//    private ProductoInvDiaRepository productoInvDiaRepository;
//
//    @Autowired
//    private ProductoInvInicioRepository productoInvInicioRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Transactional
    public Producto saveProduct(Producto producto, Usuario usuario) {
        //TODO Guardar Valores de Costos y Venta - así como la Cantidad del producto
        boolean isNew = StringUtils.isBlank(producto.getId());
        Producto result = productoRepository.save(producto);
//        if (result != null) {
//            if (isNew && producto.getStockActual().compareTo(BigDecimal.ZERO) > 0) {
//                //Guarda el Inventario al Crear el Producto
//
//                ProductoInventario pInventario = new ProductoInventario();
//                pInventario.setProducto(result);
//                pInventario.setActivo(true);
//                pInventario.setDescripcion("INICIAL");
//                pInventario.setPrecioCosto(producto.getPrecioCostoActual());
//                pInventario.setPrecioVenta(producto.getPrecioVentaActual());
//                pInventario.setQuantity(producto.getStockActual());
//                pInventario.setTrackingDate(new Date());
//                pInventario.setUsuario(usuario);
//
//                ProductoInventario resultProdInv = productoInventarioRepository.save(pInventario);
//
//                if(resultProdInv != null) {
//                    result.setPrecioCostoActual(pInventario.getPrecioCosto());
//                    result.setPrecioVentaActual(pInventario.getPrecioVenta());
//                    result.setStockActual(pInventario.getQuantity());
//                }
//            }
//        }

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
//            List<ProductoInventario> productosInventario = new ArrayList<>(inventarioRepository.findByProductoAndActivoIsTrue(p));
//            if (CollectionUtils.isNotEmpty(productosInventario) ) {
//                p.setInventarios(productosInventario);
//            }

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
