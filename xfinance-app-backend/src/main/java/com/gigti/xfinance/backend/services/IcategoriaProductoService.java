package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.stereotype.Component;

import java.util.List;

public interface IcategoriaProductoService {

    public List<CategoriaProducto> findAll(Empresa empresa);

    public boolean deleteCategoria(String id);

    public CategoriaProducto saveCategoria(CategoriaProducto categoria);

    public CategoriaProducto findById(String id);
//    public static IcategoriaProductoService get() {
//        return MockDataService.getInstance();
//    }
}
