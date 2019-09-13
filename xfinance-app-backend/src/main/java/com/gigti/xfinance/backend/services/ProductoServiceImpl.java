/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Producto saveProduct(Producto producto){
        return productoRepository.save(producto);
    }

    @Transactional
    public void delete(Producto producto){
        if(!producto.getActivo()){
            producto.setActivo(false);
        }
        productoRepository.save(producto);
    }

    public List<Producto> findByName(Empresa empresa, String productName){
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
    public Optional<Producto> findById(String id) {
        return productoRepository.findById(id);
    }
}
