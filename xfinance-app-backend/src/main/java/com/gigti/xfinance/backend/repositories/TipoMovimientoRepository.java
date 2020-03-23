package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {
}
