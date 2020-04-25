package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Impuesto;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import com.gigti.xfinance.backend.repositories.ImpuestoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImpuestoServiceImpl implements ImpuestoService{

    Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ImpuestoRepository impuestoRepository;

    @Override
    public List<Impuesto> findAll(Empresa empresa) {
        logger.info("--> findAll");
        return impuestoRepository.findAllByEmpresaAndEliminadoIsFalse(empresa);
    }

    @Override
    public List<Impuesto> findAll(Empresa empresa, int page, int size) {
        logger.info("--> findAll Pageable");
        Pageable pageable = PageRequest.of(page, size);
        return impuestoRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
    }

    @Override
    public List<Impuesto> findActivoOrInactivo(boolean activo, Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return impuestoRepository.findActivoOrInactivo(activo, empresa, pageable);
    }

    @Override
    public boolean deleteImpuesto(String id) {
        return false;
    }

    @Override
    public Impuesto saveImpuesto(Impuesto impuesto) {
        return null;
    }

    @Override
    public Impuesto findById(String id) {
        Optional<Impuesto> option = impuestoRepository.findById(id);
        return option.orElse(null);
    }

    @Override
    public List<Impuesto> findByNombreOrDescripcion(String filter, Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return impuestoRepository.findByNombreOrDescripcion(filter, empresa, pageable);
    }
}
