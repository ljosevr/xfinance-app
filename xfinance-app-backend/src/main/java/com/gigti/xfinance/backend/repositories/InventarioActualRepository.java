package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.InventarioActual;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioActualRepository extends JpaRepository<InventarioActual, String> {

    InventarioActual findAllByProducto(Producto producto);
}
