package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Gasto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto,String> {

    @Query("SELECT Count(c) FROM Gasto c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fecha BETWEEN :dateStart AND :dateEnd ")
    int countAllByEmpresa(Empresa empresa, Timestamp dateStart, Timestamp dateEnd);

    @Query("SELECT c FROM Gasto c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fecha BETWEEN :dateStart AND :dateEnd ")
    List<Gasto> findAllByEmpresa(Empresa empresa, Timestamp dateStart, Timestamp dateEnd, Pageable pageable);

    @Query("SELECT SUM(c.valor) FROM Gasto c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fecha BETWEEN :dateStart AND :dateEnd ")
    BigDecimal obtainTotal (Empresa empresa, Timestamp dateStart, Timestamp dateEnd);

    @Modifying
    @Query("DELETE FROM Gasto c " +
            "WHERE c.empresa =:empresa")
    Integer deleteAllByEmpresa(Empresa empresa);
}
