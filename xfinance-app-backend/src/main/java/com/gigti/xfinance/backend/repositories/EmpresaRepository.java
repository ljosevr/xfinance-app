/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.enums.TipoEmpresaEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, String> {

    List<Empresa> findByEliminadoIsFalseAndTipoEmpresaIs(TipoEmpresaEnum tipoEmpresa, Pageable pageable);

    @Query("SELECT e FROM Empresa e " +
            "WHERE UPPER(e.nombreEmpresa) LIKE CONCAT('%', UPPER(:filter),'%') AND " +
            "e.eliminado = FALSE AND "+
            "e.tipoEmpresa =:tipoEmpresa")
    List<Empresa> search(@Param("filter") String filter, TipoEmpresaEnum tipoEmpresa, Pageable pageable);

    @Query("SELECT e FROM Empresa e " +
            "WHERE e.activo =:activo AND " +
            "e.eliminado = False")
    List<Empresa> findActivoOrInactivo(@Param("activo") boolean activo, Pageable pageable);

    Empresa findByIdentificacion(String identificacionCli);
    Empresa findByNombreEmpresa(String companyName);
    List<Empresa> findByNombreEmpresaContaining(String companyName);

    @Query("Select e From Empresa e Where UPPER(e.codigoEmpresa) = UPPER(:codigoEmpresa)")
    Empresa findByCodigoEmpresa(String codigoEmpresa);

    Empresa findByTipoEmpresa(TipoEmpresaEnum demo);
}
