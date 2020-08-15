/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoValorVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoValoresRepository extends JpaRepository<ProductoValorVenta, String> {

    ProductoValorVenta findByProductoAndActivoIsTrue(Producto producto);

    List<ProductoValorVenta> findByActivoIsFalse();

    Integer deleteAllByProductoIn(List<Producto> productosList);
}
