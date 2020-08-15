/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private InventarioService inventarioService;


    @Transactional
    public Response saveProduct(Producto producto, Usuario usuario) {
        logger.info("--> save Producto: "+producto.toString());
        Response response = new Response();
        try {
            boolean isNewProduct = false; 
            if(StringUtils.isBlank(producto.getId())) {
                isNewProduct = true;
            }
            Producto newProducto = productoRepository.save(producto);

            if(isNewProduct && producto.isManageInitialStock()) {
                //Inventario Inicial
                InventarioInicial inventarioInicial = new InventarioInicial();
                inventarioInicial.setProducto(newProducto);
                inventarioInicial.setPrecioCosto(producto.getPrecioCosto());
                inventarioInicial.setPrecioVenta(producto.getPrecioVenta());
                inventarioInicial.setCantidad(producto.getCantidadInicial());
                inventarioInicial.setImpuesto(producto.getImpuesto());
                inventarioInicial.setFechaActualizacion(new Date());
                inventarioInicial.setInfinite(!producto.isControlarStock());
                Response response1 = inventarioService.saveInventarioInicial(inventarioInicial, usuario);
                if(response1.isSuccess()) {
                    inventarioInicial = (InventarioInicial) response1.getObject();
                    newProducto.setCantidadInicial(inventarioInicial.getCantidad());
                    newProducto.setPrecioCosto(inventarioInicial.getPrecioCosto());
                    newProducto.setPrecioVenta(inventarioInicial.getPrecioVenta());
                    newProducto.setControlarStock(inventarioInicial.isInfinite());
                }
            }

            response.setObject(producto);
            response.setMessage("Producto Guardado Exitosamente");
            response.setSuccess(true);
        }catch(Exception e){
            logger.error(" Error: "+e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Producto");
            response.setObject(null);
        }

        logger.info("<-- save Producto");
        return response;
    }

    @Transactional
    public Response delete(String id) {
        Response response = new Response();
        try {
            Producto producto = productoRepository.findById(id).orElse(null);
            if (producto != null) {
                producto.setEliminado(true);
                productoRepository.save(producto);
                response.setMessage("Producto Eliminado");
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Producto NO encontrado");
            }
        } catch (Exception e) {
            logger.debug("Error: " + e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Producto: "+e.getMessage());
        }
        return response;
    }

    public List<Producto> findByNombreProducto(Empresa empresa, String productName) {
        return productoRepository.findByEmpresaAndNombreProducto(empresa, productName);
    }

    public List<Producto> findAll(String filterText, Empresa empresa, int page, int size) {
        logger.info("--> findAll Productos: "+page + " - "+size);
        Pageable pageable = PageRequest.of(page, size);
        List<Producto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = productoRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
            listResult.forEach(p -> {
                setInventarioInicial(p);
            });
        } else  {
            listResult = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }
        return listResult;
    }

    @Override
    public List<Producto> findAll(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        logger.info("--> findAll Productos "+offsetBasedPageRequest.getPageNumber() +" - "+offsetBasedPageRequest.getPageSize()+ " - "+offsetBasedPageRequest.getOffset());
        List<Producto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = productoRepository.findByEmpresaAndEliminadoIsFalse(empresa, offsetBasedPageRequest);
        } else  {
            listResult = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }
        listResult.forEach(p -> setInventarioInicial(p));
        logger.info("<-- findAll Productos "+listResult.size());
        return listResult;
    }

    private void setInventarioInicial(Producto p) {
        InventarioInicial invInicial = inventarioService.findByProducto(p);
        BigDecimal cantidad;
        BigDecimal costo;
        BigDecimal venta;
        boolean controlarStock;

        if(invInicial == null) {
            cantidad        =  BigDecimal.ZERO;
            costo           =  BigDecimal.ZERO;
            venta           =  BigDecimal.ZERO;
            controlarStock  = false;
            p.setManageInitialStock(false);
        } else {
            cantidad        =  invInicial.getCantidad();
            costo           =  invInicial.getPrecioCosto();
            venta           =  invInicial.getPrecioVenta();
            controlarStock  = !invInicial.isInfinite();
            p.setManageInitialStock(true);
        }
        p.setCantidadInicial(cantidad);
        p.setPrecioCosto(costo);
        p.setPrecioVenta(venta);
        p.setControlarStock(controlarStock);
    }

    public List<Producto> findAllByEmpresaAndEliminadoIsFalse(Empresa empresa) {
        return productoRepository.findByEmpresaAndEliminadoIsFalse(empresa);
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

    @Override
    public int count(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = productoRepository.countByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            count = productoRepository.countByEmpresaAndNombreProducto(empresa, filterText);
        }

        return count;
    }

    public Response getPriceVenta(Producto producto) {
        logger.info("--> getPriceVenta");
        Response result = new Response();
        ProductoValorVenta pv = null;
        try{
            pv = productoValoresRepository.findByProductoAndActivoIsTrue(producto);
            if(pv == null) {
                pv = new ProductoValorVenta();
                pv.setActivo(true);
                pv.setProducto(producto);
                pv.setValorVenta(BigDecimal.ZERO);
            }
            result.setSuccess(true);
            result.setMessage("OK");
            result.setObject(pv);
        } catch(Exception e){
            logger.error("[Exception]: "+e.getMessage(), e);
            pv = null;
            result.setSuccess(false);
            result.setMessage("Error al Obtener Precio de Venta del Producto");
            result.setObject(null);
        }
        logger.info("<-- getPriceVenta");
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByEmpresa(Empresa emp) {
        logger.info("--> deleteAllByEmpresa");
        try{
            logger.info("Productos Delete: "+productoRepository.deleteAllByEmpresa(emp));
            productoRepository.flush();
            logger.info("<-- deleteAllByEmpresa");
            return true;
        }catch(Exception e) {
            logger.error("Error: "+e.getMessage(), e);
            return false;
        }
    }

    public List<Producto> findAllByEmpresa(Empresa empresa) {
        return productoRepository.findAllByEmpresa(empresa);
    }

    public List<Producto> findAllByEmpresaAndNotInfinite(Empresa empresa) {
        return productoRepository.findByEmpresaAndNotInfiniteStock(empresa);
    }
}
