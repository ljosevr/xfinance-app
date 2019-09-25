package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

public interface IcategoriaProductoService {

    public List<CategoriaProducto> findAll(Empresa empresa, int page, int size);

    public boolean deleteCategoria(String id);

    public CategoriaProducto saveCategoria(CategoriaProducto categoria);

    public CategoriaProducto findById(String id);

    public List<CategoriaProducto> findByNombreOrDescripcion(String filter, Empresa empresa, int page, int size);

//    public static IcategoriaProductoService get() {
//        return MockDataService.getInstance();
//    }
}
