package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.CategoriaProductoRepository;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Component
public class CategoriaProductoServiceImpl implements IcategoriaProductoService, HasLogger {

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public List<CategoriaProducto> findAll(Empresa empresa) {
        return categoriaProductoRepository.findAll();
    }

    @Transactional
    @Override
    public boolean eliminarCategoria(String id) {
        return false;
    }

    @Transactional
    @Override
    public CategoriaProducto guardarCategoria(CategoriaProducto categoria) {
        try{
            Optional<Empresa> empresa = empresaRepository.findById(categoria.getEmpresa().getId());
            categoria.setEmpresa(empresa.get());
            return categoriaProductoRepository.save(categoria);
        }catch(Exception e){
            getLogger().error(e.getMessage(), e);
            return null;
        }
    }
}
