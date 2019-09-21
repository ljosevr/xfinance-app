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
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@UIScope
public class ProductoServiceImpl implements IProductoService, HasLogger {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Producto saveProduct(Producto producto){
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

    public List<Producto> findAll(Empresa empresa){
        return productoRepository.findByEmpresa(empresa);
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
