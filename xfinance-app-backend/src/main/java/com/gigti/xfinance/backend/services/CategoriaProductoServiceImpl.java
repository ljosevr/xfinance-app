package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.CategoriaProductoRepository;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class CategoriaProductoServiceImpl implements CategoriaProductoService {

    Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public List<CategoriaProducto> findAll(Empresa empresa) {
        return categoriaProductoRepository.findAllByEmpresaAndEliminadoIsFalse(empresa);
    }

    @Override
    public List<CategoriaProducto> findAll(Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoriaProductoRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
    }

    public List<CategoriaProducto> findAll(String filterText, Empresa empresa, int page, int size) {
        logger.info("--> findAll Categorias: ");
        Pageable pageable = PageRequest.of(page, size);
        List<CategoriaProducto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = categoriaProductoRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
        } else  {
            listResult = categoriaProductoRepository.search(filterText, empresa, pageable);
        }

        logger.info("<-- findAll Categorias ");
        return listResult;
    }

    public List<CategoriaProducto> findActivoOrInactivo(boolean activo, Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoriaProductoRepository.findActivoOrInactivo(activo, empresa, pageable);
    }

    @Transactional
    @Override
    public Response delete(String id) {
        logger.info("--> delete ");
        Response response = new Response();
        try {
            CategoriaProducto categoria = categoriaProductoRepository.findById(id).orElse(null);
            if (categoria != null) {
                categoria.setEliminado(true);
                categoriaProductoRepository.save(categoria);
                response.setMessage("Categoria Eliminada");
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Categoria NO encontrado");
            }
        } catch(Exception e) {
            logger.debug("Error: "+e.getMessage(),e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Categoria: "+e.getMessage());
        }
        logger.info("<-- delete");
        return response;
    }

    @Transactional
    @Override
    public CategoriaProducto saveCategoria(CategoriaProducto categoria) {
        logger.info("--> saveCategoria");
        try{
            Optional<Empresa> empresa = empresaRepository.findById(categoria.getEmpresa().getId());
            if(empresa.isPresent()){
                categoria.setEmpresa(empresa.get());
            }else{
                return null;
            }
            logger.info("<-- saveCategoria");
            return categoriaProductoRepository.save(categoria);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public CategoriaProducto findById(String id) {
        Optional<CategoriaProducto> option = categoriaProductoRepository.findById(id);
        return option.orElse(null);
    }

    @Override
    public List<CategoriaProducto> findByNombreOrDescripcion(String filter, Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoriaProductoRepository.findByNombreOrDescripcion(filter, empresa, pageable);
    }

    public int count(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = categoriaProductoRepository.countByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            count = categoriaProductoRepository.countByEmpresaAndNombre(empresa, filterText);
        }

        return count;
    }
}
