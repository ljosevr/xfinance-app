package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.stereotype.Component;

import java.util.List;

public interface IcategoriaProductoService {

    public List<CategoriaProducto> findAll(Empresa empresa);

    public boolean eliminarCategoria(String id);

    public CategoriaProducto guardarCategoria(CategoriaProducto categoria);

//    public static IcategoriaProductoService get() {
//        return MockDataService.getInstance();
//    }
}
