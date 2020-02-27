package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.mapper.ConvertUsuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.Utils;
import com.gigti.xfinance.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());

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
        logger.log(Level.INFO,"login: "+codigoEmpresa+ " - "+nombreUsuario);
        try{
            Empresa empresa = empresaRepository.findByCodigoEmpresa(codigoEmpresa);
            if(empresa != null) {
                logger.log(Level.INFO,"Empresa existe: "+empresa.getNombreEmpresa());
                Usuario usuario = usuarioRepository.findByNombreUsuarioAndEmpresa(nombreUsuario, empresa);
                if (usuario != null) {
                    logger.log(Level.INFO,"Usuario existe: "+usuario.getNombreUsuario());
                    //TODO
                    //if(passwordEncoder.encode(password).equals(passwordEncoder.encode(usuario.getPasswordUsuario()))){
                    if (password.equals(usuario.getPasswordUsuario())) {
                        logger.log(Level.INFO,"Concuerda Password");
                        //Ejecutar parches de initBackend - De Compañia
                        //logger.debug("Iniciando App Backend");
                        //logger.debug("Finalizando App Backend");
                        return usuario;
                    } else {
                        return null;
                    }
                }
            }
        } catch(Exception e){
            logger.log(Level.SEVERE, "Error al hacer Login: "+e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Rol> findAllRol(Empresa empresa, boolean eliminado) {
        return rolRepository.findAllByEmpresaAndEliminado(empresa, eliminado);
    }

    @Override
    public UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO) {
        logger.log(Level.INFO, "saveUsuario");
        try{
            Optional<Empresa> empresa = empresaRepository.findById(usuarioDTO.getEmpresa().getId());
            if(empresa.isPresent()){
                logger.log(Level.INFO, "isPresent Empresa");
                usuarioDTO.setEmpresa(empresa.get());
            }else{
                logger.log(Level.INFO, "NOT Present Empresa");
                return null;
            }
            Usuario usuario = ConvertUsuario.convertDtoToEntity(usuarioDTO);
            Persona persona = personaRepository.findByIdentificacion(usuario.getPersona().getIdentificacion());

            if(persona != null) {
                logger.log(Level.INFO, "persona found");
                usuario.getPersona().setId(persona.getId());
            } else {
                String pass = Utils.encrytPass("123456");
                usuario.setPasswordUsuario(pass);
            }
            usuario.setPersona(personaRepository.save(usuario.getPersona()));
            usuario = usuarioRepository.save(usuario);
            return ConvertUsuario.convertEntityToDTO(usuario);
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Response deleteUsuario(String id) {
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
            logger.log(Level.SEVERE,"Error: "+e.getMessage(),e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Usuario");
        }
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
        if(filter == null || filter.isEmpty()) {
            List<Usuario> usuarios = usuarioRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
            usuarios.forEach(e -> result.add(ConvertUsuario.convertEntityToDTO(e)));
            return result;
        } else  {
            List<Usuario> usuarios = usuarioRepository.search(filter, empresa, pageable);
            usuarios.forEach(e -> result.add(ConvertUsuario.convertEntityToDTO(e)));
            return  result;
        }
    }

    @Override
    public Response changePassword(String id, String value, String value1, String value2) {
        Response response = new Response();
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if(usuario != null) {
            try {
                value = Utils.encrytPass(value);
                value1 = Utils.encrytPass(value1);
                value2 = Utils.encrytPass(value2);
            } catch (NoSuchAlgorithmException e) {
                logger.log(Level.SEVERE, "Error al Encriptar Password: "+e.getMessage(), e);
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
                        logger.log(Level.SEVERE, "Error al cambiar Password: "+e.getMessage(), e);
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
        return response;
    }
}