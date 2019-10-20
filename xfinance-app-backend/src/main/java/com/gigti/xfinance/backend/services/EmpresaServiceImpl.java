package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.TipoEmpresa;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Service
public class EmpresaServiceImpl implements IEmpresaService, HasLogger {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public List<Empresa> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return empresaRepository.findByEliminadoIsFalseAndTipoEmpresaIs(TipoEmpresa.NORMAL, pageable);
    }

    @Override
    public List<Empresa> findActivoOrInactivo(boolean activo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return empresaRepository.findActivoOrInactivo(activo, pageable);
    }

    @Transactional
    @Override
    public boolean deleteEmpresa(String id) {
        try {
            Empresa empresa = empresaRepository.findById(id).orElse(null);
            if (empresa != null) {
                empresa.setEliminado(true);
                empresa = empresaRepository.save(empresa);
                return empresa != null;
            }
        } catch(Exception e) {
            getLogger().debug("Error: "+e.getMessage(),e);
        }
        return false;
    }

    @Transactional
    @Override
    public Empresa saveEmpresa(Empresa empresa) {
        try{
            if(empresa.getFechaActivacion() == null) {
                empresa.setFechaActivacion(new Date());
            }
            if(empresa.getTipoEmpresa() == null){
                empresa.setTipoEmpresa(TipoEmpresa.NORMAL);
            }
            if(empresa.getTipoIde() == null){
                empresa.setTipoIde(TipoIde.NIT);
            }

            return empresaRepository.save(empresa);
        }catch(Exception e){
            getLogger().error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Empresa findById(String id) {
        return empresaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Empresa> findByNombreOrDescripcion(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return empresaRepository.findByNombreOrDescripcionAndTipoEmpresa(filter, TipoEmpresa.NORMAL, pageable);
    }
}
