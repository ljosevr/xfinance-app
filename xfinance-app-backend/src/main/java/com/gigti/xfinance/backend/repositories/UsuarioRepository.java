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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    @Query("Select u From Usuario u Where UPPER(u.nombreUsuario) =UPPER(:nombreUsuario) AND u.empresa =:empresa")
    public Usuario findByNombreUsuarioAndEmpresa(String nombreUsuario, Empresa empresa);

    @Query("Select u From Usuario u Where UPPER(u.nombreUsuario) =UPPER(:nombreUsuario)")
    Usuario findByNombreUsuario(String nombreUsuario);

    @Query("Select u From Usuario u where u.persona.identificacion =: identificacion")
    public List<Usuario> findByIdentificacion(@Param("identificacion") String identificacion);

    @Query("SELECT u FROM Usuario  u " +
            "WHERE u.empresa =:empresa AND " +
            "u.eliminado = false")
    public List<Usuario> findByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa, Pageable pageable);

    public List<Usuario> findByRol(Rol role);

    public List<Usuario> findByPersona(Persona persona);

    public List<Usuario> findAllByActivoIsTrue();

    public List<Usuario> findAllByActivoIsFalse();

    @Query("SELECT u FROM Usuario  u " +
            "WHERE u.empresa =:empresa AND " +
            "lower(u.nombreUsuario) like lower(concat('%', :filter, '%')) AND " +
            "u.eliminado = false")
    List<Usuario> search(@Param("filter") String filter, Empresa empresa, Pageable pageable);
}
