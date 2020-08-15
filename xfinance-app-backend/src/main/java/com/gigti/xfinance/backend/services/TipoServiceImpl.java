package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.TipoMedida;
import com.gigti.xfinance.backend.repositories.TipoMedidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TipoServiceImpl implements  TipoService{
    private static final Logger logger = LoggerFactory.getLogger(TipoServiceImpl.class);

    @Autowired
    private TipoMedidaRepository tipoMedidaRepository;

    @Override
    public List<TipoIde> getTiposIdentificacion() {
        List<TipoIde> list = new ArrayList<>();
        list.add(TipoIde.CEDULA);
        list.add(TipoIde.NIT);
        list.add(TipoIde.EXTRANJERIA);
        list.add(TipoIde.TIDENTIDAD);

        return list;
    }

    @Override
    public List<TipoMedida> findAllTiposMedidas(Empresa empresa) {
        return tipoMedidaRepository.findAllByEmpresa(empresa);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllTipos(Empresa empresa) {
        logger.info("--> deleteAllTipos");
        try{
            logger.info("Tipos de Datos Delete: "+tipoMedidaRepository.deleteAllByEmpresa(empresa));
            tipoMedidaRepository.flush();
            logger.info("<-- deleteAllTipos");
            return true;
        }catch(Exception e) {
            logger.error("Error: "+e.getMessage(), e);
            return false;
        }


    }
}
