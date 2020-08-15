package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Impuesto;

import java.util.List;

public interface ImpuestoService {

    List<Impuesto> findAll(Empresa empresa);

    List<Impuesto> findAll(Empresa empresa, int page, int size);

    public List<Impuesto> findActivoOrInactivo(boolean activo, Empresa empresa, int page, int size);

    public boolean deleteImpuesto(String id);

    public Impuesto saveImpuesto(Impuesto impuesto);

    public Impuesto findById(String id);

    public List<Impuesto> findByNombreOrDescripcion(String filter, Empresa empresa, int page, int size);

    boolean deleteAllByEmpresa(Empresa emp);
}
