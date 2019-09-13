/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    public Empresa findByIdentificacion(String identificacionCli);
    public Empresa findByNombreEmpresa(String companyName);
    public List<Empresa> findByNombreEmpresaContaining(String companyName);

//    @Query("select c from Empresa c where r.companyName like %:companyName%")
//    public List<Empresa> findByNameExp(@Param("companyName") String companyName);


}
