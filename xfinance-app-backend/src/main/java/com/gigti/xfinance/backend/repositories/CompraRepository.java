package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra,String> {

}
