/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;


import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.TipoIde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {

    List<Persona> findByPrimerApellidoContaining(String lastName);

    List<Persona> findByPrimerNombreContaining(String lastName);

    Persona findByIdentificacionAndEmpresa(String identification, Empresa empresa);

    boolean existsByIdentificacionAndTipoIde(String identificacion, TipoIde tipoIde);

    boolean existsByIdentificacionAndTipoIdeAndIdIsNot(String identificacion, TipoIde tipoIde, String id);

    Integer deleteAllByEmpresa(Empresa empresa);
}
