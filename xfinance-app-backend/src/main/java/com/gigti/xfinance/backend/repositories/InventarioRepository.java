/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoInventario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<ProductoInventario, String> {

    @Query("SELECT p FROM ProductoInventario p " +
            "WHERE p.producto.empresa =:empresa AND " +
            "p.inicial = true")
    List<ProductoInventario> findByEmpresaAndInicialIsTrue(@Param("empresa") Empresa empresa, Pageable pageable);

    @Query("SELECT p FROM ProductoInventario p " +
            "WHERE p.producto.empresa =:empresa AND " +
            "lower(p.producto.nombreProducto" +
            ") like lower(concat('%', :filter, '%')) AND " +
            "p.inicial = true")
    List<ProductoInventario> search(@Param("filter") String filter, Empresa empresa, Pageable pageable);

    List<ProductoInventario> findByProductoAndActivoIsTrue(Producto p);

    List<ProductoInventario> findByProducto(Producto p);

}
