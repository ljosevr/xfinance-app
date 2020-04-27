package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioActualCosto;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioActualCostoRepository extends JpaRepository<InventarioActualCosto, String> {

    List<InventarioActualCosto> findByProductoOrderByFechaCreacionAsc(Producto producto);

    @Query("SELECT a FROM InventarioActualCosto a " +
            "WHERE a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    List<InventarioActualCosto> findAllByEmpresa(Empresa empresa, Pageable pageable);

    @Query("SELECT a FROM InventarioActualCosto a " +
            "WHERE UPPER(a.producto.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') AND " +
            "a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    List<InventarioActualCosto> search(Empresa empresa, String productName, Pageable pageable);

    @Query("SELECT count(DISTINCT a.producto) FROM InventarioActualCosto a " +
            "WHERE a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    int countAllByEmpresa(Empresa empresa);

    @Query("SELECT COUNT(DISTINCT a.producto) FROM InventarioActualCosto a " +
            "WHERE UPPER(a.producto.nombreProducto) LIKE CONCAT('%', UPPER(:productName),'%') AND " +
            "a.producto.empresa =:empresa AND " +
            "a.producto.eliminado = false")
    int countAllByEmpresaAndNombreProducto(Empresa empresa, String productName);
}
