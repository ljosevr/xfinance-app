package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoValorVenta;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoValorVentaServiceImpl implements ProductoValorVentaService{
    Logger logger = LoggerFactory.getLogger(ProductoValorVentaServiceImpl.class);
    
    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByProductos(List<Producto> productoList) {
        logger.info("--> deleteAllByProductos");
        try {
            logger.info("Producto Valores Delete: " + productoValoresRepository.deleteAllByProductoIn(productoList));
            productoValoresRepository.flush();
            logger.info("<-- deleteAllByProductos");
            return true;
        }catch(Exception e) {
            logger.error("Error al Eliminar Producto Valores: "+e.getMessage(), e);
            return false;
        }
    }

    @Override
    //@Transactional(readOnly = true)
    @Transactional
    public ProductoValorVenta findByProductoAndActivoIsTrue(Producto producto) {
        ProductoValorVenta productoValorVenta = productoValoresRepository.findByProductoAndActivoIsTrue(producto);

        return productoValorVenta;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ProductoValorVenta saveProductoValor(ProductoValorVenta productoValorVenta) {
        return productoValoresRepository.save(productoValorVenta);
    }
}
