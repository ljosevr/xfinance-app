package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Impuesto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto, String> {

    @Query("SELECT i FROM Impuesto  i " +
            "WHERE i.empresa =:empresa AND " +
            "i.eliminado = False")
    public List<Impuesto> findByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT i FROM Impuesto  i " +
            "WHERE i.empresa =:empresa AND " +
            "i.activo =:activo AND " +
            "i.eliminado = False")
    public List<Impuesto> findActivoOrInactivo(@Param("activo") boolean activo, @Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT i FROM Impuesto  i " +
            "WHERE i.empresa =:empresa AND " +
            "UPPER(i.nombre) LIKE CONCAT('%', UPPER(:filter),'%') OR " +
            "UPPER(i.descripcion) LIKE CONCAT('%', UPPER(:filter),'%') AND " +
            "i.eliminado = FALSE")
    List<Impuesto> findByNombreOrDescripcion(@Param("filter") String filter, @Param("empresa") Empresa empresa, Pageable pageable);

    List<Impuesto> findAllByEmpresaAndEliminadoIsFalse(Empresa empresa);

    Integer deleteAllByEmpresa(Empresa empresa);
}
