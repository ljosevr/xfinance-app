/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Venta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, String> {

    @Query("Select COUNT(v) FROM Venta v " +
            "WHERE v.usuario.persona.empresa =:empresa")
    long countByUsuarioEmpresa(Empresa empresa);

    @Modifying
    @Query("DELETE FROM Venta v WHERE v.usuario IN (Select u From Usuario u WHERE u.persona.empresa =:empresa)")
    Integer deleteAllByEmpresa(Empresa empresa);

    @Query("SELECT Count(c) FROM Venta c " +
            "WHERE c.usuario.persona.empresa =:empresa AND " +
            "c.fechaCreacion BETWEEN :dateStart AND :dateEnd ")
    int countAllByEmpresa(Empresa empresa, Timestamp dateStart, Timestamp dateEnd);

    @Query("SELECT Count(c) FROM Venta c " +
            "WHERE c.usuario.persona.empresa =:empresa AND " +
            "c.fechaCreacion BETWEEN :dateStart AND :dateEnd AND " +
            "UPPER(c.numeroFactura) LIKE CONCAT('%', UPPER(:filterText),'%') ")
    int countAllByEmpresaAndNumeroFactura(Empresa empresa, String filterText, Timestamp dateStart, Timestamp dateEnd);

    @Query("SELECT c FROM Venta c " +
            "WHERE c.usuario.persona.empresa =:empresa AND " +
            "c.fechaCreacion BETWEEN :dateStart AND :dateEnd ")
    List<Venta> findAllByEmpresa(Empresa empresa, Timestamp dateStart, Timestamp dateEnd, Pageable pageable);

    @Query("SELECT c FROM Venta c " +
            "WHERE c.usuario.persona.empresa =:empresa AND " +
            "c.fechaCreacion BETWEEN :dateStart AND :dateEnd AND " +
            "UPPER(c.numeroFactura) LIKE CONCAT('%', UPPER(:filterText),'%') ")
    List<Venta> search(Empresa empresa, String filterText, Timestamp dateStart, Timestamp dateEnd, Pageable pageable);
}
