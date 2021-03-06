/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, String> {

    Rol findByNombreAndEmpresaAndEliminado(String nombre, Empresa empresa, boolean eliminado);

    @Query("SELECT r FROM Rol  r " +
            "WHERE r.empresa =:empresa AND " +
            "r.eliminado =:eliminado AND "+
            "UPPER(r.nombre) <> 'ROOT'")
    List<Rol> findAllByEmpresaAndEliminado(Empresa empresa, boolean eliminado);

    Rol findAllByIdAndEmpresaAndEliminado(String id, Empresa empresa, boolean eliminado);

//    @Query("SELECT r FROM Rol  r " +
//            "WHERE r.empresa =:empresa AND " +
//            "r.eliminado = false AND "+
//            "r.porDefecto =: porDefecto AND "+
//            "r.nombre != " + "ROOT" );
    List<Rol> findAllByEmpresaAndPorDefectoAndNombreIsNotAndEliminadoFalse(Empresa empresa, boolean porDefecto, String nombre);

        @Query("SELECT r FROM Rol  r " +
            "WHERE r.empresa IS NULL AND " +
            "r.eliminado = false AND "+
            "r.porDefecto = TRUE AND "+
            "r.nombre <>:nombre AND "+
            "r.activo = TRUE")
    List<Rol> findAllRolByDefault(String nombre);

    @Query("SELECT r FROM Rol  r " +
            "WHERE "+
            "r.eliminado = false AND "+
            "r.porDefecto = TRUE AND "+
            "r.nombre =:nombre AND "+
            "r.nombre <> :notName")
    List<Rol> findAllRolByDefaultAndNombre(String nombre, String notName);

    Integer deleteAllByEmpresa(Empresa empresa);

    @Query("SELECT r FROM Rol  r " +
            "WHERE r.empresa =:empresa AND " +
            "r.eliminado = False")
    List<Rol> findByEmpresaAndEliminadoIsFalse(Empresa empresa, Pageable pageable);

    @Query("SELECT r FROM Rol r " +
            "WHERE r.empresa =:empresa AND " +
            "lower(r.nombre) like lower(concat('%', :filterText, '%')) AND " +
            "r.eliminado = false")
    List<Rol> search(String filterText, Empresa empresa, Pageable pageable);

    int countByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa);

    @Query("SELECT COUNT(r) FROM Rol r " +
            "WHERE UPPER(r.nombre) LIKE CONCAT('%', UPPER(:rolName),'%') " +
            "AND r.empresa =:empresa AND " +
            "r.eliminado = FALSE")
    int countByEmpresaAndNombre(Empresa empresa, String rolName);
}
