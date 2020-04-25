package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Response;

import java.util.List;

public interface EmpresaService {

    public List<EmpresaDTO> findAll(String filter, int page, int size);

    public List<EmpresaDTO> findActivoOrInactivo(boolean activo, int page, int size);

    Response deleteEmpresa(String id);

    public EmpresaDTO saveEmpresa(EmpresaDTO empresa);

    public EmpresaDTO findById(String id);

    public List<EmpresaDTO> findByNombreOrDescripcion(String filter, int page, int size);

}
