package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Movimiento;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.repositories.MovimientoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService{
    Logger logger = LoggerFactory.getLogger(MovimientoServiceImpl.class);
    @Autowired
    private MovimientoRepository movimientoRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByProductos(List<Producto> productoList) {
        logger.info("--> deleteAllByProductos");
        try {
            logger.info("Movimientos Delete: " + movimientoRepository.deleteAllByProductoIn(productoList));
            movimientoRepository.flush();
            logger.info("<-- deleteAllByProductos");
            return true;
        }catch(Exception e) {
            logger.error("Error al Eliminar Movimientos: "+e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Movimiento saveMovimiento(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }
}
