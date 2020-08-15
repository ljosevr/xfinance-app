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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;
import java.util.Optional;


@Service
public class CategoriaProductoServiceImpl implements CategoriaProductoService {

    Logger logger = LoggerFactory.getLogger(CategoriaProductoServiceImpl.class);

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

    @Override
    public List<CategoriaProducto> findAll(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        logger.info("--> findAll Categorias: ");
        List<CategoriaProducto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = categoriaProductoRepository.findByEmpresaAndEliminadoIsFalse(empresa, offsetBasedPageRequest);
        } else  {
            listResult = categoriaProductoRepository.search(filterText, empresa, offsetBasedPageRequest);
        }

        logger.info("<-- findAll Categorias "+listResult.size());
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
    public Response saveCategoria(CategoriaProducto categoria) {
        logger.info("--> saveCategoria");
        Response response = new Response();
        try{
            Optional<Empresa> empresa = empresaRepository.findById(categoria.getEmpresa().getId());
            if(empresa.isPresent()){
                categoria.setEmpresa(empresa.get());
            }else{
                response.setSuccess(false);
                response.setMessage("Error: Empresa No encontrada");
                return null;
            }

            response.setObject(categoriaProductoRepository.save(categoria));
            response.setSuccess(true);
            response.setMessage("Categoria Guardada Exitosamente");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Categoria");
            response.setObject(null);
        }
        logger.info("<-- saveCategoria");
        return response;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByEmpresa(Empresa empresa) {
        logger.info("--> deleteAllByEmpresa");
        try{
            logger.info("Categorias Delete: "+categoriaProductoRepository.deleteAllByEmpresa(empresa));
            categoriaProductoRepository.flush();
            logger.info("<-- deleteAllByEmpresa");
            return true;
        }catch(Exception e) {
            logger.error("Error: "+e.getMessage(), e);
            return false;
        }
    }
}
