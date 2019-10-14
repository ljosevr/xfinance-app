/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductoServiceImpl implements IProductoService, HasLogger {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Producto saveProduct(Producto producto){
        //TODO Guardar Valores de Costos y Venta - así como la Cantidad del producto

        return productoRepository.save(producto);
    }

    @Transactional
    public boolean delete(String id){
        try {
            Producto producto = productoRepository.findById(id).orElse(null);
            if (producto != null) {
                producto.setEliminado(true);
                producto = productoRepository.save(producto);
                return producto != null;
            }
        } catch(Exception e) {
            getLogger().debug("Error: "+e.getMessage(),e);
        }
        return false;
    }

    public List<Producto> findByNombreProducto(Empresa empresa, String productName){
        return productoRepository.findByEmpresaAndNombreProducto(empresa, productName);
    }

    public List<Producto> findAll(Empresa empresa, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findByEmpresa(empresa, pageable);
    }

    public Producto findByBarCode(Empresa empresa, String barCode){
        return productoRepository.findByEmpresaAndCodigoBarra(empresa, barCode);
    }

    public List<Producto> findByNameOrBarCode(Empresa empresa, String anything){
        return productoRepository.findByEmpresaAndNombreProductoContainingOrCodigoBarraContaining(empresa, anything, anything);
    }

    @Override
    public Producto findById(String id) {
        return productoRepository.findById(id).orElse(null);
    }
}
