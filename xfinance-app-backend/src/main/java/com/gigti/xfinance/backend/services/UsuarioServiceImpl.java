package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.mapper.ConvertUsuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private VistaRepository vistaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public Usuario login(String codigoEmpresa, String nombreUsuario, String password) {
        logger.info("login: "+codigoEmpresa+ " - "+nombreUsuario);
        try{
            Empresa empresa = empresaRepository.findByCodigoEmpresa(codigoEmpresa);
            if(empresa != null) {
                logger.info("Empresa existe: "+empresa.getNombreEmpresa());
                Usuario usuario = usuarioRepository.findByNombreUsuarioAndEmpresa(nombreUsuario, empresa);
                if (usuario != null) {
                    logger.info("Usuario existe: "+usuario.getNombreUsuario());
                    if (password.equals(usuario.getPasswordUsuario())) {
                        logger.info("Concuerda Password");
                        return usuario;
                    } else {
                        return null;
                    }
                }
            }
        } catch(Exception e){
            logger.error("Error al hacer Login: "+e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Rol> findAllRol(Empresa empresa, boolean eliminado) {
        return rolRepository.findAllByEmpresaAndEliminado(empresa, eliminado);
    }

    @Override
    public Response saveUsuario(UsuarioDTO usuarioDTO) {
        logger.info("--> saveUsuario");
        Response response = new Response();
        try{
            Optional<Empresa> empresa = empresaRepository.findById(usuarioDTO.getEmpresa().getId());
            if(empresa.isPresent()){
                logger.info("isPresent Empresa");
                usuarioDTO.setEmpresa(empresa.get());
            }else{
                response.setSuccess(false);
                response.setMessage("no existe Empresa. Contacte al administrador");
                logger.info("NOT Present Empresa");
                return response;
            }
            Usuario usuario = ConvertUsuario.convertDtoToEntity(usuarioDTO);
            Persona persona = personaRepository.findByIdentificacionAndEmpresa(usuario.getPersona().getIdentificacion(), usuarioDTO.getEmpresa());

            if(persona != null) {
                logger.info("persona found");
                if(usuario.getPersona().getId() == null) {
                    response.setSuccess(false);
                    response.setMessage("Identificación Ya se encuentra Registrada en Otro usuario");
                    return response;
                }
                //Validar Email
                if(validarEmailUsuario(usuario, response)) {
                    return response;
                }

                usuario.getPersona().setId(persona.getId());
            } else {
                //Validar Email
                if(validarEmailUsuario(usuario, response)) {
                    return response;
                }

                String pass = UtilsBackend.encrytPass("123456");
                usuario.setPasswordUsuario(pass);
            }
            usuario.setPersona(personaRepository.save(usuario.getPersona()));
            usuario = usuarioRepository.save(usuario);
            response.setObject(ConvertUsuario.convertEntityToDTO(usuario));
            response.setSuccess(true);
            response.setMessage("Usuario Guardado Correctamente");
            return response;
        }catch(Exception e){
            logger.error("Error al Guardar usuario: "+e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Guardar Usuario");
            return response;
        }
    }



    private boolean validarEmailUsuario(Usuario usuario, Response response) {
        Usuario u = usuarioRepository.findByEmail(usuario.getEmail());
        if (u != null) {
            if(!u.getPersona().getIdentificacion().equalsIgnoreCase(usuario.getPersona().getIdentificacion())) {
                response.setSuccess(false);
                response.setMessage("Email Ya se encuentra Registrado en Otro Usuario");
                return true;
            }
        }
        return false;
    }

    @Override
    public Response deleteUsuario(String id) {
        logger.info("--> deleteUsuario");
        Response response = new Response();
        try {
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario != null) {
                usuario.setEliminado(true);
                usuario = usuarioRepository.save(usuario);
                if(usuario != null) {
                    response.setSuccess(true);
                    response.setMessage("Usuario Eliminado Correctamente");
                } else {
                    response.setSuccess(false);
                    response.setMessage("No Fue Posible eliminar Usuario");
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Usuario No encontrado para eliminar");
            }
        } catch(Exception e) {
            logger.error("Error: "+e.getMessage(),e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Usuario");
        }
        logger.info("<-- deleteUsuario ");
        return response;
    }

    @Override
    public List<UsuarioDTO> findAll(Empresa empresa, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<UsuarioDTO> result = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
        usuarios.forEach(e -> result.add(ConvertUsuario.convertEntityToDTO(e)));
        return result;
    }

    @Override
    public List<UsuarioDTO> findAll(String filter, Empresa empresa, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<UsuarioDTO> result = new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();
        if(filter == null || filter.isEmpty()) {
            usuarios = usuarioRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
        } else  {
            usuarios = usuarioRepository.search(filter, empresa, pageable);
        }
        usuarios.forEach(e -> result.add(ConvertUsuario.convertEntityToDTO(e)));
        return result;
    }

    @Override
    public List<UsuarioDTO> findAll(String filter, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest) {
        logger.info("--> findAll");
        List<UsuarioDTO> result = new ArrayList<>();
        List<Usuario> usuarios;
        if(filter == null || filter.isEmpty()) {
            usuarios = usuarioRepository.findByEmpresaAndEliminadoIsFalse(empresa, offsetBasedPageRequest);
        } else  {
            usuarios = usuarioRepository.search(filter, empresa, offsetBasedPageRequest);
        }
        usuarios.forEach(e -> result.add(ConvertUsuario.convertEntityToDTO(e)));
        logger.info("<-- findAll: "+result.size());
        return result;
    }

    @Override
    public Response changePassword(String id, String value, String value1, String value2) {
        logger.info("--> changePassword");
        Response response = new Response();
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if(usuario != null) {
            try {
                value = UtilsBackend.encrytPass(value);
                value1 = UtilsBackend.encrytPass(value1);
                value2 = UtilsBackend.encrytPass(value2);
            } catch (NoSuchAlgorithmException e) {
                logger.error("Error al Encriptar Password: "+e.getMessage(), e);
            }
            if(value1.equals(value2)) {
                if(usuario.getPasswordUsuario().equals(value)){
                    usuario.setPasswordUsuario(value1);
                    try{
                        Usuario usuario1 = usuarioRepository.save(usuario);
                        if(usuario1 != null) {
                            response.setSuccess(true);
                            response.setMessage("Password Cambiado Correctamente");
                            response.setObject(usuario1);
                        } else {
                            response.setSuccess(false);
                            response.setMessage("Error al cambiar Password");
                        }
                    } catch (Exception e) {
                        logger.error("Error al cambiar Password: "+e.getMessage(), e);
                        response.setSuccess(false);
                        response.setMessage("Error al cambiar Password");
                    }
                } else {
                    response.setSuccess(false);
                    response.setMessage("Password Actual no coincide, con el registrado");
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Nuevo Password No coincide");
            }
        } else {
            response.setSuccess(false);
            response.setMessage("Usuario No encontrado");
        }
        logger.info("<-- changePassword");
        return response;
    }

    @Override
    public Usuario findByName(String userName) {
        return usuarioRepository.findByNombreUsuario(userName);
    }

    @Override
    public int count(String filter, Empresa empresa) {
        logger.info("--> Count");
        int count;
        if(filter == null || filter.isEmpty()) {
            count = usuarioRepository.countByEmpresa(empresa);
        } else  {
            count = usuarioRepository.countByEmpresaAndFilter(empresa, filter);
        }
        logger.info("<-- Count");
        return count;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllUsuariosByEmpresa(Empresa empresa) {
        logger.info("--> deleteAllUsuariosByEmpresa");
        try{
            logger.info("Usuarios Delete: "+usuarioRepository.deleteAllByEmpresa(empresa));
            logger.info("Personas Delete: "+personaRepository.deleteAllByEmpresa(empresa));
            usuarioRepository.flush();
            personaRepository.flush();
            logger.info("<-- deleteAllUsuariosByEmpresa");
            return true;
        } catch(Exception e){
            logger.error("Error: "+e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
