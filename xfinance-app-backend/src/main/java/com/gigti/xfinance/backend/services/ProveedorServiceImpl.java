package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.ProveedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private Logger logger = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public Response save(Proveedor proveedor, Usuario usuario) {
        logger.info("--> save Proveedor: "+proveedor.toString());
        Response response = new Response();
        boolean passValidations = true;
        try {

            //Validar Identificacion
            Proveedor proveedorTemp = proveedorRepository.findAllByIdentificacionAndEmpresa(proveedor.getIdentificacion(),usuario.getPersona().getEmpresa());

            if(proveedorTemp != null && proveedorTemp.getId() != proveedor.getId()) {
                response.setMessage("Identificación Ya Existe en Otro Proveedor ("+proveedorTemp.getNombre()+")");
                response.setSuccess(false);
                passValidations = false;
            } else {
                //Validar Nombre
                proveedorTemp = proveedorRepository.findAllByNombreAndEmpresa(proveedor.getNombre(),usuario.getPersona().getEmpresa());

                if(proveedorTemp != null && proveedorTemp.getId() != proveedor.getId()) {
                    response.setMessage("Nombre Ya Existe en Otro Proveedor");
                    response.setSuccess(false);
                    passValidations = false;
                } else {
                    //Validar Email
                    proveedorTemp = proveedorRepository.findAllByEmailAndEmpresa(proveedor.getEmail(),usuario.getPersona().getEmpresa());

                    if(proveedorTemp != null && proveedorTemp.getId() != proveedor.getId()) {
                        response.setMessage("Email Ya Existe en Otro Proveedor ("+proveedorTemp.getNombre()+")");
                        response.setSuccess(false);
                        passValidations = false;
                    }
                }
            }

            if(passValidations) {
                proveedor.setEmpresa(usuario.getPersona().getEmpresa());
                response.setObject(proveedorRepository.save(proveedor));
                response.setMessage("Proveedor Guardado Exitosamente");
                response.setSuccess(true);
            }

        }catch(Exception e){
            logger.error(" Error: "+e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Proveedor");
            response.setObject(null);
        }

        logger.info("<-- save Proveedor");
        return response;
    }

    @Override
    @Transactional
    public Response delete(Proveedor proveedor, Usuario usuario) {
        proveedor.setEliminado(true);
        Response response = new Response();
        try {
            response.setObject(proveedorRepository.save(proveedor));
            response.setSuccess(true);
            response.setMessage("Proveedor Eliminado Correctamente");
        }catch(Exception e) {
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Proveedor");
        }
        return response;
    }

    @Override
    public Response findById(String id) {
        Response response = new Response();
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        if (proveedor.isPresent()) {
            response.setSuccess(true);
            response.setObject(proveedor.get());
        } else {
            response.setSuccess(false);
            response.setMessage("No fue posible encontrar el Proveedor con el Id: "+id);
        }
        return response;
    }

    @Override
    public List<Proveedor> find(String filter, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        if(filter == null || filter.isEmpty()) {
            return proveedorRepository.findAllByEmpresa(empresa, offsetBasedPageRequest);
        }
        return proveedorRepository.search(filter, empresa, offsetBasedPageRequest);
    }

    @Override
    public int countSearch(String filterText, Empresa empresa) {
        if(filterText == null || filterText.isEmpty()) {
            return proveedorRepository.countAllByEmpresa(empresa);
        }
        return proveedorRepository.countSearch(filterText, empresa);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByEmpresa(Empresa empresa) {
        logger.info("--> deleteAllByEmpresa");
        try {
            logger.info("Proveedores Delete: " + proveedorRepository.deleteAllByEmpresa(empresa));
            proveedorRepository.flush();
            logger.info("<-- deleteAllByEmpresa");
            return true;
        }catch(Exception e) {
            logger.error("Error al Eliminar: "+e.getMessage(), e);
            return false;
        }
    }
}
