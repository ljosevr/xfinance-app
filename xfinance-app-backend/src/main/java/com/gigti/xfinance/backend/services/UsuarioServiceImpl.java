package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import com.gigti.xfinance.backend.mapper.ConvertUsuario;
import com.gigti.xfinance.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    /**
//     * The password encoder to use when encrypting passwords.
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

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
                logger.log(Level.INFO, "persona Not found");
                usuario.getPersona().setId(persona.getId());
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
    public boolean deleteUsuario(String id) {
        try {
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario != null) {
                usuario.setEliminado(true);
                usuario = usuarioRepository.save(usuario);
                return usuario != null;
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Error: "+e.getMessage(),e);
        }
        return false;
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
}
