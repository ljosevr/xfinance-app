package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.ClienteRepository;
import com.gigti.xfinance.backend.repositories.PersonaRepository;
import org.apache.commons.lang3.StringUtils;
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
public class ClienteServiceImpl implements ClienteService{
    private Logger logger = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    @Transactional
    public Response save(Cliente cliente, Usuario usuario) {
        logger.info("--> save Cliente: "+cliente.toString());
        Response response = new Response();
        boolean passValidations = false;
        try{
            cliente.getPersona().setEmpresa(cliente.getPersona().getEmpresa());
            if(cliente.getPersona().getId() == null) {
                logger.info("Cliente New");
                Persona persona = personaRepository.findByIdentificacionAndEmpresa(cliente.getPersona().getIdentificacion(), cliente.getPersona().getEmpresa());
                if (persona != null) {
                    logger.info("Persona found");
                    //Validar que no esta como cliente
                    Cliente clienteTemp = clienteRepository.findByPersonaAndEmpresa(persona, cliente.getPersona().getEmpresa());
                    if (clienteTemp != null) {
                        response.setSuccess(false);
                        response.setMessage("Identificación Ya esta asignada en otro Cliente");
                    } else {
                        //Validar Email
                        passValidations = isValidEmailCliente(cliente, response);
                        cliente.getPersona().setId(persona.getId());
                        cliente.setEliminado(false);
                    }
                } else {
                    //Validar Email
                    passValidations = isValidEmailCliente(cliente, response);
                    cliente.setEliminado(false);
                }
            } else {
                logger.info("Edit Cliente");
                //Validar Identificación

                Persona persona = personaRepository.findByIdentificacionAndEmpresa(cliente.getPersona().getIdentificacion(), cliente.getPersona().getEmpresa());
                if (persona != null) {
                    logger.info("Persona found");
                    //Validar que no esta como cliente
                    Cliente clienteTemp = clienteRepository.findByPersonaAndEmpresa(persona, cliente.getPersona().getEmpresa());
                    if (clienteTemp != null && !clienteTemp.getId().equals(cliente.getId())) {
                        response.setSuccess(false);
                        response.setMessage("Identificación Ya esta asignada en otro Cliente");
                        passValidations = false;
                    } else {
                        //Validar Email
                        passValidations = isValidEmailCliente(cliente, response);
                        cliente.setEliminado(false);
                    }
                } else {
                    //Validar Email
                    passValidations = isValidEmailCliente(cliente, response);
                    cliente.setEliminado(false);
                }
            }
            if(passValidations) {
                logger.info("Cliente Paso Validaciones");
                cliente.setPersona(personaRepository.save(cliente.getPersona()));
                cliente = clienteRepository.save(cliente);
                response.setObject(cliente);
                response.setSuccess(true);
                response.setMessage("Cliente Guardado Correctamente");
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Cliente");
        }

        logger.info("<-- save Cliente");
        return response;
    }

    private boolean isValidEmailCliente(Cliente cliente, Response response) {
        if(StringUtils.isNoneBlank(cliente.getEmail())) {
            Cliente c = clienteRepository.findByEmailAndEmpresa(cliente.getEmail(), cliente.getPersona().getEmpresa());
            if (c != null) {
                if (!c.getPersona().getId().equalsIgnoreCase(cliente.getPersona().getId())) {
                    response.setSuccess(false);
                    response.setMessage("Email Ya se encuentra Registrado en Otro Cliente");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Response delete(Cliente cliente, Usuario usuario) {
        cliente.setEliminado(true);
        Response response = new Response();
        try {
            response.setObject(clienteRepository.save(cliente));
            response.setSuccess(true);
            response.setMessage("Cliente Eliminado Correctamente");
        }catch(Exception e) {
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Cliente");
        }
        return response;
    }

    @Override
    public Response findById(String id) {
        Response response = new Response();
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            response.setSuccess(true);
            response.setObject(cliente.get());
        } else {
            response.setSuccess(false);
            response.setMessage("No fue posible encontrar el Cliente con el Id: "+id);
        }
        return response;
    }

    @Override
    public List<Cliente> find(String filter, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        if(filter == null || filter.isEmpty()) {
            return clienteRepository.findAllByEmpresa(empresa, offsetBasedPageRequest);
        }
        return clienteRepository.search(filter, empresa, offsetBasedPageRequest);
    }

    @Override
    public int countSearch(String filterText, Empresa empresa) {
        if(filterText == null || filterText.isEmpty()) {
            return clienteRepository.countAllByEmpresa(empresa);
        }
        return clienteRepository.countSearch(filterText, empresa);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllByEmpresa(Empresa empresa) {
        logger.info("--> deleteAllByEmpresa");
        try{
            logger.info("Clientes Delete: "+clienteRepository.deleteAllByEmpresa(empresa));
            clienteRepository.flush();
            logger.info("<-- deleteAllByEmpresa");
            return true;
        }catch(Exception e){
            logger.error("Error al Eliminar los Clientes: "+e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Cliente findByIdentificaction(String identification, Empresa empresa) {
        return clienteRepository.findByIdentificacionAndEmpresa(identification, empresa);
    }
}
