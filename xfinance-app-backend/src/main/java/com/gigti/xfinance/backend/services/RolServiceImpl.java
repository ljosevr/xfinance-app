package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.repositories.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServiceImpl implements RolService{
    Logger logger = LoggerFactory.getLogger(RolServiceImpl.class);
    @Autowired
    private RolRepository rolRepository;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByEmpresa(Empresa empresa) {
        logger.info("--> deleteAllByEmpresa");
        try {
            logger.info("Rol Delete: " + rolRepository.deleteAllByEmpresa(empresa));
            rolRepository.flush();
            logger.info("<-- deleteAllByEmpresa");
            return true;
        }catch(Exception e) {
            logger.error("Error al Eliminar Roles: "+e.getMessage(), e);
            return false;
        }
    }
}
