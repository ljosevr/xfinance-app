package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra,String> {

    @Query("SELECT Count(c) FROM Compra c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fechaCompra BETWEEN :dateStart AND :dateEnd ")
    int countAllByEmpresa(Empresa empresa, Timestamp dateStart, Timestamp dateEnd);

    @Query("SELECT Count(c) FROM Compra c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fechaCompra BETWEEN :dateStart AND :dateEnd AND " +
            "UPPER(c.numeroFactura) LIKE CONCAT('%', UPPER(:filterText),'%') ")
    int countAllByEmpresaAndNumeroFactura(Empresa empresa, String filterText, Timestamp dateStart, Timestamp dateEnd);

    @Query("SELECT c FROM Compra c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fechaCompra BETWEEN :dateStart AND :dateEnd ")
    List<Compra> findAllByEmpresa(Empresa empresa, Timestamp dateStart, Timestamp dateEnd, Pageable pageable);

    @Query("SELECT c FROM Compra c " +
            "WHERE c.empresa =:empresa AND " +
            "c.fechaCompra BETWEEN :dateStart AND :dateEnd AND " +
            "UPPER(c.numeroFactura) LIKE CONCAT('%', UPPER(:filterText),'%') ")
    List<Compra> search(Empresa empresa, String filterText, Timestamp dateStart, Timestamp dateEnd, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Compra c " +
            "WHERE c.empresa =:empresa")
    Integer deleteAllByEmpresa(Empresa empresa);

    Compra findByNumeroFacturaAndEmpresa(String numeroFactura, Empresa empresa);
}
