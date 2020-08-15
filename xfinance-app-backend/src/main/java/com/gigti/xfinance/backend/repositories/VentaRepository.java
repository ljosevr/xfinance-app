/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, String> {

    @Query("Select COUNT(v) FROM Venta v " +
            "WHERE v.usuario.persona.empresa =:empresa")
    long countByUsuarioEmpresa(Empresa empresa);

    @Modifying
    @Query("DELETE FROM Venta v WHERE v.usuario IN (Select u From Usuario u WHERE u.persona.empresa =:empresa)")
    Integer deleteAllByEmpresa(Empresa empresa);
}
