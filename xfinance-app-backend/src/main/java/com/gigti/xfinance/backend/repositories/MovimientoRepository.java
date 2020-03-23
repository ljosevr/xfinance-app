package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Movimiento;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, String> {

    List<Movimiento> findAllByProducto(Producto producto);
}
