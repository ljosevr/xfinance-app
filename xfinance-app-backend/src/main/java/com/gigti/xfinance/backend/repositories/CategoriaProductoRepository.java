package com.gigti.xfinance.backend.repositories;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, String> {

    public List<CategoriaProducto> findByEmpresaAndEliminadoIsFalse(Empresa empresa);

    public List<CategoriaProducto> findByEmpresaAndActivoTrue(Empresa empresa);

    public List<CategoriaProducto> findByEmpresaAndActivoFalse(Empresa empresa);

    @Query("SELECT c FROM CategoriaProducto c " +
            "WHERE UPPER(c.nombre) LIKE CONCAT('%', UPPER(:filter),'%') OR " +
            "UPPER(c.descripcion) LIKE CONCAT('%', UPPER(:filter),'%') AND " +
            "c.empresa =:empresa AND " +
            "c.eliminado = FALSE")
    public List<CategoriaProducto> findByNombreOrDescripcion(@Param("filter") String filter, @Param("empresa") Empresa empresa);
}
