package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Proveedor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, String> {

    @Query("SELECT p FROM Proveedor p " +
            "WHERE p.empresa =:empresa AND " +
            "(lower(p.nombre) like lower(concat('%', :filter, '%')) OR " +
            "lower(p.identificacion) like lower(concat('%', :filter, '%'))) AND " +
            "p.eliminado = false")
    List<Proveedor> search(String filter, Empresa empresa, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Proveedor p " +
            "WHERE p.empresa =:empresa AND " +
            "(lower(p.nombre) like lower(concat('%', :filter, '%')) OR " +
            "lower(p.identificacion) like lower(concat('%', :filter, '%'))) AND " +
            "p.eliminado = false")
    int countSearch(String filter, Empresa empresa);

    @Query("SELECT p FROM Proveedor p " +
            "WHERE p.empresa =:empresa AND " +
            "p.eliminado = false")
    List<Proveedor> findAllByEmpresa(Empresa empresa, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Proveedor p " +
            "WHERE p.empresa =:empresa AND " +
            "p.eliminado = false")
    int countAllByEmpresa(Empresa empresa);

    Integer deleteAllByEmpresa(Empresa empresa);

}
