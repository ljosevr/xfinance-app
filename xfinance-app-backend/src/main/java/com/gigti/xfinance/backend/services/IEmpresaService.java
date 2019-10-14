package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;

import java.util.List;

public interface IEmpresaService {

    public List<Empresa> findAll(int page, int size);

    public List<Empresa> findActivoOrInactivo(boolean activo, int page, int size);

    public boolean deleteEmpresa(String id);

    public Empresa saveEmpresa(Empresa empresa);

    public Empresa findById(String id);

    public List<Empresa> findByNombreOrDescripcion(String filter, int page, int size);

}
