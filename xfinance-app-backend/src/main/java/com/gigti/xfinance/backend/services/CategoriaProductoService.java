package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;

public interface CategoriaProductoService {

    List<CategoriaProducto> findAll(Empresa empresa);

    List<CategoriaProducto> findAll(Empresa empresa, int page, int size);

    List<CategoriaProducto> findAll(String filterText, Empresa empresa, int page, int size);

    List<CategoriaProducto> findAll(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);

    List<CategoriaProducto> findActivoOrInactivo(boolean activo, Empresa empresa, int page, int size);

    Response delete(String id);

    Response saveCategoria(CategoriaProducto categoria);

    CategoriaProducto findById(String id);

    List<CategoriaProducto> findByNombreOrDescripcion(String filter, Empresa empresa, int page, int size);

    int count(String filterText, Empresa empresa);

    boolean deleteAllByEmpresa(Empresa empresa);
}
