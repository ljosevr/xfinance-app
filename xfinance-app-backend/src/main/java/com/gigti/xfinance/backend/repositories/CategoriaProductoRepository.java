package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, String> {

    List<CategoriaProducto> findAllByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa);

    @Query("SELECT c FROM CategoriaProducto  c " +
            "WHERE c.empresa =:empresa AND " +
            "c.eliminado = False")
    public List<CategoriaProducto> findByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT c FROM CategoriaProducto  c " +
            "WHERE c.empresa =:empresa AND " +
            "c.activo =:activo AND " +
            "c.eliminado = False")
    public List<CategoriaProducto> findActivoOrInactivo(@Param("activo") boolean activo, @Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT c FROM CategoriaProducto c " +
            "WHERE UPPER(c.nombre) LIKE CONCAT('%', UPPER(:filter),'%') OR " +
            "UPPER(c.descripcion) LIKE CONCAT('%', UPPER(:filter),'%') AND " +
            "c.empresa =:empresa AND " +
            "c.eliminado = FALSE")
    List<CategoriaProducto> findByNombreOrDescripcion(@Param("filter") String filter, @Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT c FROM CategoriaProducto c " +
            "WHERE c.empresa =:empresa AND " +
            "lower(c.nombre) like lower(concat('%', :filter, '%')) AND " +
            "c.eliminado = false")
    List<CategoriaProducto> search(String filter, Empresa empresa, Pageable pageable);

    int countByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa);

    @Query("SELECT COUNT(c) FROM CategoriaProducto c " +
            "WHERE UPPER(c.nombre) LIKE CONCAT('%', UPPER(:categoryName),'%') " +
            "AND c.empresa =:empresa AND " +
            "c.eliminado = FALSE")
    int countByEmpresaAndNombre(Empresa empresa, String categoryName);

    Integer deleteAllByEmpresa(Empresa empresa);
}
