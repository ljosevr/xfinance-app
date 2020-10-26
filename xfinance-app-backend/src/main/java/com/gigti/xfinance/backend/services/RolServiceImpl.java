package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import com.gigti.xfinance.backend.repositories.RolRepository;
import com.gigti.xfinance.backend.repositories.VistaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RolServiceImpl implements RolService{
    Logger logger = LoggerFactory.getLogger(RolServiceImpl.class);
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private VistaRepository vistaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByEmpresa(Empresa empresa) {
        logger.info("--> deleteAllByEmpresa");
        try {
            logger.info("Rol Delete: " + rolRepository.deleteAllByEmpresa(empresa));
            rolRepository.flush();
            logger.info("<-- deleteAllByEmpresa");
            return true;
        }catch(Exception e) {
            logger.error("Error al Eliminar Roles: "+e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Rol> findAll(String filterText, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        logger.info("--> findAll Roles: ");
        List<Rol> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = rolRepository.findByEmpresaAndEliminadoIsFalse(empresa, offsetBasedPageRequest);
        } else  {
            listResult = rolRepository.search(filterText, empresa, offsetBasedPageRequest);
        }

        logger.info("<-- findAll Roles "+listResult.size());
        return listResult;
    }

    @Override
    public int count(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = rolRepository.countByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            count = rolRepository.countByEmpresaAndNombre(empresa, filterText);
        }

        return count;
    }

    @Override
    public Response delete(String id) {
        logger.info("--> delete Rol");
        Response response = new Response();
        try {
            Rol rol = rolRepository.findById(id).orElse(null);
            if (rol != null) {
                rol.setEliminado(true);
                rolRepository.save(rol);
                response.setMessage("Rol Eliminado");
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Rol NO encontrado");
            }
        } catch(Exception e) {
            logger.debug("Error: "+e.getMessage(),e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Rol: "+e.getMessage());
        }
        logger.info("<-- delete Rol");
        return response;
    }

    @Override
    public Response save(Rol rol) {
        logger.info("--> save Rol");
        Response response = new Response();
        try{
            Optional<Empresa> empresa = empresaRepository.findById(rol.getEmpresa().getId());
            if(empresa.isPresent()){
                rol.setEmpresa(empresa.get());
            }else{
                response.setSuccess(false);
                response.setMessage("Error: Empresa No encontrada");
                return null;
            }

            if(rol.getVistas().isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Debes agregar Minimo Una (1) Vista al Rol");
            } else {

                //Validar si Vista tiene un Menu Padre, para agregar.
                Set<Vista> vistasTemp = new HashSet<>(rol.getVistas());
                for(Vista v: vistasTemp) {
                    if(v.getVistaPadre() != null) {
                        rol.getVistas().add(v.getVistaPadre());
                    }
                }

                response.setObject(rolRepository.save(rol));
                response.setSuccess(true);
                response.setMessage("Rol Guardado Exitosamente");
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Rol");
            response.setObject(null);
        }
        logger.info("<-- save Rol");
        return response;
    }

    @Override
    public List<Vista> findAllVistas() {
        return vistaRepository.findAll();
    }
}
