package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c " +
            "WHERE c.persona.empresa =:empresa AND " +
            "(lower(c.persona.primerNombre) like lower(concat('%', :filter, '%')) OR " +
            "lower(c.persona.primerApellido) like lower(concat('%', :filter, '%')) OR " +
            "lower(c.persona.identificacion) like lower(concat('%', :filter, '%'))) AND " +
            "c.eliminado = false")
    List<Cliente> search(String filter, Empresa empresa, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Cliente c " +
            "WHERE c.persona.empresa =:empresa AND " +
            "(lower(c.persona.primerNombre) like lower(concat('%', :filter, '%')) OR " +
            "lower(c.persona.primerApellido) like lower(concat('%', :filter, '%')) OR " +
            "lower(c.persona.identificacion) like lower(concat('%', :filter, '%'))) AND " +
            "c.eliminado = false")
    int countSearch(String filter, Empresa empresa);

    @Query("SELECT c FROM Cliente c " +
            "WHERE c.persona.empresa =:empresa AND " +
            "c.eliminado = false")
    List<Cliente> findAllByEmpresa(Empresa empresa, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Cliente c " +
            "WHERE c.persona.empresa =:empresa AND " +
            "c.eliminado = false")
    int countAllByEmpresa(Empresa empresa);


    Cliente findByEmail(String email);

    @Modifying
    @Query("DELETE FROM Cliente c WHERE c.persona IN (SELECT p FROM Persona p WHERE p.empresa =:empresa)")
    Integer deleteAllByEmpresa(Empresa empresa);

}
