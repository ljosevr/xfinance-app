package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioActualRepository extends JpaRepository<InventarioActual, String> {

    InventarioActual findByProducto(Producto producto);

    @Query("SELECT a FROM InventarioActual a " +
            "WHERE a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    List<InventarioActual> findAllByEmpresa(Empresa empresa, Pageable pageable);

    @Query("SELECT a FROM InventarioActual a " +
            "WHERE UPPER(a.producto.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') AND " +
            "a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    List<InventarioActual> search(Empresa empresa, String productName, Pageable pageable);

    @Query("SELECT count(a) FROM InventarioActual a " +
            "WHERE a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    int countAllByEmpresa(Empresa empresa);

    @Query("SELECT COUNT(a) FROM InventarioActual a " +
            "WHERE UPPER(a.producto.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') AND " +
            "a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    int countAllByEmpresaAndNombreProducto(Empresa empresa, String productName);
}
