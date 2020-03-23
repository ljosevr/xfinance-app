package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.CompraItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraItemRepository extends JpaRepository<CompraItem,String> {
}
