package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;

import java.util.List;

public interface CategoriaProductoService {

    List<CategoriaProducto> findAll(Empresa empresa);

    List<CategoriaProducto> findAll(Empresa empresa, int page, int size);

    public List<CategoriaProducto> findActivoOrInactivo(boolean activo, Empresa empresa, int page, int size);

    public boolean deleteCategoria(String id);

    public CategoriaProducto saveCategoria(CategoriaProducto categoria);

    public CategoriaProducto findById(String id);

    public List<CategoriaProducto> findByNombreOrDescripcion(String filter, Empresa empresa, int page, int size);

//    public static IcategoriaProductoService get() {
//        return MockDataService.getInstance();
//    }
}
