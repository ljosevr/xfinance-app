package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.TipoDescuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDescuentoRepository extends JpaRepository<TipoDescuento, Integer> {
}
