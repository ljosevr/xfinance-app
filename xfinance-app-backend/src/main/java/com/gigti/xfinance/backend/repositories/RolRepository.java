/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, String> {

    public Rol findByNombreAndEmpresaAndEliminado(String nombre, Empresa empresa, boolean eliminado);

    @Query("SELECT r FROM Rol  r " +
            "WHERE r.empresa =:empresa AND " +
            "r.eliminado =:eliminado AND "+
            "UPPER(r.nombre) <> 'ROOT'")
    public List<Rol> findAllByEmpresaAndEliminado(Empresa empresa, boolean eliminado);

    public Rol findAllByIdAndEmpresaAndEliminado(String id, Empresa empresa, boolean eliminado);

//    @Query("SELECT r FROM Rol  r " +
//            "WHERE r.empresa =:empresa AND " +
//            "r.eliminado = false AND "+
//            "r.porDefecto =: porDefecto AND "+
//            "r.nombre != " + "ROOT" );
    public List<Rol> findAllByEmpresaAndPorDefectoAndNombreIsNotAndEliminadoFalse(Empresa empresa, boolean porDefecto, String nombreNot);
}
