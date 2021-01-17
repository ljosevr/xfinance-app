/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.InventarioInicial;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioInicialRepository extends JpaRepository<InventarioInicial, String> {

    @Query("SELECT p FROM InventarioInicial p " +
            "WHERE p.producto.empresa =:empresa AND " +
            "lower(p.producto.nombreProducto" +
            ") like lower(concat('%', :filter, '%')) ")
    List<InventarioInicial> search(@Param("filter") String filter, Empresa empresa, Pageable pageable);

    InventarioInicial findByProducto(Producto p);

    Integer deleteAllByProductoIn(List<Producto> productosList);
}
