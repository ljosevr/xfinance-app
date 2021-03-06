/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    Optional<Producto> findById(String id);

    int countByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa);

    int countByEmpresa(Empresa empresa);

    @Query("SELECT COUNT(p) FROM Producto p " +
            "WHERE UPPER(p.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') " +
            "AND p.empresa =:empresa AND " +
            "p.eliminado = FALSE")
    int countByEmpresaAndNombreProducto(Empresa empresa, String productName);

    @Query("SELECT p FROM Producto p " +
            "WHERE  p.empresa =:empresa AND " +
            "p.eliminado = FALSE " +
            "ORDER BY p.nombreProducto")
    List<Producto> findByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT p FROM Producto p " +
            "WHERE  p.empresa =:empresa AND " +
            "p.eliminado = FALSE")
    List<Producto> findByEmpresaAndEliminadoIsFalse(@Param("empresa") Empresa empresa);

    @Query("SELECT p FROM Producto p " +
            "WHERE UPPER(p.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') " +
            "AND p.empresa =:empresa AND " +
            "p.eliminado = FALSE " +
            "ORDER BY p.nombreProducto")
    List<Producto> findByEmpresaAndNombreProducto(Empresa empresa, String productName, Pageable pageable);

    Producto findByEmpresaAndCodigoBarra(Empresa empresa, String codigoBarra);

    List<Producto> findByEmpresaAndNombreProductoContainingOrCodigoBarraContaining(Empresa empresa, String anything, String anything2);

    @Query("SELECT p FROM Producto p " +
            "WHERE p.empresa =:empresa AND " +
            "lower(p.nombreProducto) like lower(concat('%', :filter, '%')) AND " +
            "p.eliminado = false")
    List<Producto> search(String filter, Empresa empresa, Pageable pageable);

    @Query("SELECT p FROM Producto p " +
            "WHERE p.empresa =:empresa AND " +
            "p.codigoBarra =:filter AND " +
            "p.eliminado = false")
    Producto findByBarCode(String filter, Empresa empresa);

    Integer deleteAllByEmpresa(Empresa emp);

    List<Producto> findAllByEmpresa(Empresa empresa);

    @Query("SELECT p FROM Producto p " +
            "LEFT JOIN InventarioActual i ON p = i.producto AND i.manageStock = FALSE " +
            "WHERE p.empresa =:empresa AND " +
            "p.eliminado = FALSE " )
    List<Producto> findByEmpresaAndNotInfiniteStock(@Param("empresa") Empresa empresa);

    @Query("SELECT p FROM Producto p " +
            "WHERE UPPER(p.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') " +
            "AND p.empresa =:empresa AND " +
            "p.eliminado = FALSE " +
            "ORDER BY p.nombreProducto")
    List<Producto> findAllByEmpresaAndNombreProducto(Empresa empresa, String productName);

    @Query("SELECT p FROM Producto p " +
            "WHERE UPPER(p.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') " +
            "AND p.empresa =:empresa AND " +
            "p.eliminado = FALSE " +
            "ORDER BY p.nombreProducto")
    List<Producto> findAllByEmpresaAndNombreProducto(Empresa empresa, String productName, Pageable pageable);

    @Query("SELECT p FROM Producto p " +
            "WHERE p.nombreProducto = :productName " +
            "AND p.empresa =:empresa AND " +
            "p.eliminado = FALSE " +
            "ORDER BY p.nombreProducto")
    Producto findByEmpresaAndNombreProducto(Empresa empresa, String productName);
}
