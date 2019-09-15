package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, String> {

    public List<CategoriaProducto> findByEmpresaAndEliminadoIsFalse(Empresa empresa);

    public List<CategoriaProducto> findByEmpresaAndActivoTrue(Empresa empresa);

    public List<CategoriaProducto> findByEmpresaAndActivoFalse(Empresa empresa);
}
