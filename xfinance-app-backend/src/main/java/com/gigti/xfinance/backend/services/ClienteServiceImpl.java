package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.ClienteRepository;
import com.gigti.xfinance.backend.repositories.PersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import javax.transaction.Transactional;
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
        try{
            cliente.getPersona().setEmpresa(cliente.getPersona().getEmpresa());

            Persona persona = personaRepository.findByIdentificacion(cliente.getPersona().getIdentificacion());

            if(persona != null) {
                logger.info("Persona found");

                //Validar Email
                if (validarEmailCliente(cliente, response)) {
                    return response;
                }

                cliente.getPersona().setId(persona.getId());
            } else {
                //Validar Email
                if (validarEmailCliente(cliente, response)) {
                    return response;
                }

                cliente.setEliminado(false);
            }
            cliente.setPersona(personaRepository.save(cliente.getPersona()));
            cliente = clienteRepository.save(cliente);
            response.setObject(cliente);
            response.setSuccess(true);
            response.setMessage("Cliente Guardado Correctamente");

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Cliente");
        }

        logger.info("<-- save Cliente");
        return response;
    }

    private boolean validarEmailCliente(Cliente cliente, Response response) {
        Cliente c = clienteRepository.findByEmail(cliente.getEmail());
        if (c != null) {
            if(!c.getPersona().getIdentificacion().equalsIgnoreCase(cliente.getPersona().getIdentificacion())) {
                response.setSuccess(false);
                response.setMessage("Email Ya se encuentra Registrado en Otro Cliente");
                return true;
            }
        }
        return false;
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
}
