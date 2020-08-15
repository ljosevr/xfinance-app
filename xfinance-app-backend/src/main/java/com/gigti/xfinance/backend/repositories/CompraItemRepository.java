package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.CompraItem;
import com.gigti.xfinance.backend.data.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraItemRepository extends JpaRepository<CompraItem,String> {
    Integer deleteAllByProductoIn(List<Producto> productoList);
}
