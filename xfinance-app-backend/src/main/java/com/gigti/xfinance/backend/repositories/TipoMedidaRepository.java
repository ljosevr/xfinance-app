package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.TipoMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoMedidaRepository extends JpaRepository<TipoMedida, String> {

    List<TipoMedida> findAllByEmpresa(Empresa empresa);

    Integer deleteAllByEmpresa(Empresa empresa);
}
