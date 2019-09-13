package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Parche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcheRepository extends JpaRepository<Parche, String> {

    public Parche findByNombreAndEmpresa(String nombre, Empresa empresa);
}
