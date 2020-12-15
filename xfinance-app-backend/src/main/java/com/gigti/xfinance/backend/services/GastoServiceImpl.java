package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.HandledException;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import com.gigti.xfinance.backend.repositories.GastoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GastoServiceImpl implements GastoService {

    private static final Logger logger = LoggerFactory.getLogger(GastoServiceImpl.class);

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public int count(Empresa empresa, LocalDate dateStart, LocalDate dateEnd) {

        dateStart = dateStart == null ? LocalDate.now() : dateStart;
        LocalDateTime dateStartTime = dateStart.atTime(0,0,0);
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        LocalDateTime dateEndTime = dateEnd.atTime(23,59,59);
        logger.info("Fecha Inicio: "+java.sql.Timestamp.valueOf(dateStartTime));
        logger.info("Fecha Fin: "+java.sql.Timestamp.valueOf(dateEndTime));
        return gastoRepository.countAllByEmpresa(empresa,
                    java.sql.Timestamp.valueOf(dateStartTime),
                    java.sql.Timestamp.valueOf(dateEndTime));

    }

    @Override
    public List<Gasto> findAll(Empresa empresa, LocalDate dateStart, LocalDate dateEnd, OffsetBasedPageRequest offsetBasedPageRequest) {
        String methodName = "findAll-Gastos";
        logger.info("--> "+methodName);
        dateStart = dateStart == null ? LocalDate.now() : dateStart;
        LocalDateTime dateStartTime = dateStart.atTime(0,0,0);
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        LocalDateTime dateEndTime = dateEnd.atTime(23,59,59);
        logger.info("Fecha Inicio: "+java.sql.Timestamp.valueOf(dateStartTime));
        logger.info("Fecha Fin: "+java.sql.Timestamp.valueOf(dateEndTime));
        List<Gasto> listResult;
        try {
            listResult = gastoRepository.findAllByEmpresa(empresa,
                    java.sql.Timestamp.valueOf(dateStartTime),
                    java.sql.Timestamp.valueOf(dateEndTime),
                    offsetBasedPageRequest);


            logger.info("Cantidad de Registros: " + listResult.size());


        } catch(Exception e) {
            logger.error("Error al Obtener Registros: ");
            listResult = new ArrayList<>();
        }
        return listResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Response save(Gasto gasto, Usuario usuario) {
        logger.info("--> save-Gasto");
        Response response = new Response();
        try{
            Optional<Empresa> empresa = empresaRepository.findById(gasto.getEmpresa().getId());
            if(empresa.isPresent()){
                gasto.setEmpresa(empresa.get());
            }else{
                response.setSuccess(false);
                response.setMessage("Error: Empresa No encontrada");
                return null;
            }

            response.setObject(gastoRepository.save(gasto));
            response.setSuccess(true);
            response.setMessage("Gasto Agregado Exitosamente");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Gasto");
            response.setObject(null);
        }
        logger.info("<-- save-Gasto");
        return response;
    }

    @Override
    public Response delete(String id) {
        Response response = new Response();
        gastoRepository.deleteById(id);
        response.setSuccess(true);
        response.setMessage("Gasto Eliminado Correctamente");
        return response;
    }

    @Override
    public BigDecimal getGastoTotal(Empresa empresa, LocalDate dateStart, LocalDate dateEnd) {
        String methodName = "getGastoTotal";
        logger.info("--> "+methodName);
        dateStart = dateStart == null ? LocalDate.now() : dateStart;
        LocalDateTime dateStartTime = dateStart.atTime(0,0,0);
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        LocalDateTime dateEndTime = dateEnd.atTime(23,59,59);
        logger.info("Fecha Inicio: "+java.sql.Timestamp.valueOf(dateStartTime));
        logger.info("Fecha Fin: "+java.sql.Timestamp.valueOf(dateEndTime));
        List<Gasto> listResult;
        try {
            return gastoRepository.obtainTotal(empresa,
                    java.sql.Timestamp.valueOf(dateStartTime),
                    java.sql.Timestamp.valueOf(dateEndTime));

        } catch(Exception e) {
            logger.error("Error al Obtener Total de Gastos: "+e.getMessage(), e);
        }
        return BigDecimal.ZERO;
    }


}
