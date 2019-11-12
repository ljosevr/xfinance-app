package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;

import java.util.List;

public interface IEmpresaService {

    public List<EmpresaDTO> findAll(int page, int size);

    public List<EmpresaDTO> findActivoOrInactivo(boolean activo, int page, int size);

    public boolean deleteEmpresa(String id);

    public EmpresaDTO saveEmpresa(EmpresaDTO empresa);

    public EmpresaDTO findById(String id);

    public List<EmpresaDTO> findByNombreOrDescripcion(String filter, int page, int size);

}
