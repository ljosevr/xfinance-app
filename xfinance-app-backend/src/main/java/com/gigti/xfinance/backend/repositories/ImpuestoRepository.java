package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto, String> {
}
