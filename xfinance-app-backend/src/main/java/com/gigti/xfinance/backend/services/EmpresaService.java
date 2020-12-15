package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.others.Response;

import java.util.List;

public interface EmpresaService {

    List<EmpresaDTO> findAll(String filter, int page, int size);

    List<EmpresaDTO> findActivoOrInactivo(boolean activo, int page, int size);

    Response deleteEmpresa(String id);

    Response saveEmpresa(EmpresaDTO empresa);

    EmpresaDTO findById(String id);

    List<EmpresaDTO> findByNombreOrDescripcion(String filter, int page, int size);

    Empresa findEmpresaDemo();

    boolean deleteAllEmpresa(Empresa emp);

    Response registerNewEmpresa(EmpresaDTO empresa);
}
