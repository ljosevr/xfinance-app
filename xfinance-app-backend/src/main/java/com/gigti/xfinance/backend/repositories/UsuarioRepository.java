/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.enums.TipoUsuarioEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    @Query("Select u From Usuario u Where UPPER(u.nombreUsuario) =UPPER(:nombreUsuario) AND u.persona.empresa =:empresa")
    Usuario findByNombreUsuarioAndEmpresa(String nombreUsuario, Empresa empresa);

    @Query("Select u From Usuario u Where UPPER(u.nombreUsuario) =UPPER(:nombreUsuario)")
    Usuario findByNombreUsuario(String nombreUsuario);

    @Query("Select u From Usuario u where u.persona.identificacion =: identificacion")
    List<Usuario> findByIdentificacion(@Param("identificacion") String identificacion);

    @Query("SELECT u FROM Usuario  u " +
            "WHERE u.persona.empresa =:empresa AND " +
            "u.eliminado = false")
    List<Usuario> findByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa, Pageable pageable);

    List<Usuario> findByRol(Rol role);

    List<Usuario> findByPersona(Persona persona);

    List<Usuario> findAllByActivoIsTrue();

    List<Usuario> findAllByActivoIsFalse();

    @Query("SELECT u FROM Usuario  u " +
            "WHERE u.persona.empresa =:empresa AND " +
            "lower(u.nombreUsuario) like lower(concat('%', :filter, '%')) AND " +
            "lower(u.persona.primerNombre) like lower(concat('%', :filter, '%')) AND " +
            "u.eliminado = false")
    List<Usuario> search(@Param("filter") String filter, Empresa empresa, Pageable pageable);

    Usuario findByEmail(String email);

    @Query("SELECT u FROM Usuario  u " +
            "WHERE u.persona.empresa =:empresa AND " +
            "u.eliminado = false AND "+
            "u.tipoUsuario = :tipoUsuario AND " +
            "u.adminDefecto =:isdefault")
    Usuario findByEmpresaAndTipoUsuario(@Param("empresa") Empresa empresa, TipoUsuarioEnum tipoUsuario, boolean isdefault);

    @Query("Select Count(u) From Usuario u " +
            "Where u.persona.empresa =:empresa " +
            "And u.eliminado = false")
    int countByEmpresa(Empresa empresa);

    @Query("Select Count(u) From Usuario u " +
            "Where u.persona.empresa =:empresa " +
            "And lower(u.nombreUsuario) like lower(concat('%', :filter, '%')) " +
            "And lower(u.persona.primerNombre) like lower(concat('%', :filter, '%')) " +
            "And u.eliminado = false")
    int countByEmpresaAndFilter(Empresa empresa, String filter);

    @Modifying
    @Query("DELETE FROM Usuario u WHERE u.persona IN (SELECT p FROM Persona p WHERE p.empresa =:empresa)")
    Integer deleteAllByEmpresa(Empresa empresa);
}
