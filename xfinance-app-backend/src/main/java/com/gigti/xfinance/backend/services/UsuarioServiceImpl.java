package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.ResponseBool;
import com.gigti.xfinance.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
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
                    //if(passwordEncoder.encode(password).equals(passwordEncoder.encode(usuario.getPasswordUsuario()))){
                    if (password.equals(usuario.getPasswordUsuario())) {
                        logger.log(Level.INFO,"Concuerda Password");
                        //TODO
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
    public List<Usuario> findByNombreUsuario(String nombreUsuario, Empresa empresa, int page, int pageSize) {

        return null;
    }

    @Override
    public List<Rol> findAllRol(Empresa empresa, boolean eliminado) {
        return rolRepository.findAllByEmpresaAndEliminado(empresa, eliminado);
    }

    @Override
    public Rol findRolById(String id, Empresa empresa, boolean activo) {
        return rolRepository.findAllByIdAndEmpresaAndEliminado(id, empresa, activo);
    }

    @Override
    @Transactional
    public Rol saveRol(Rol rol, List<Vista> vistasRol) {
        try{
            rol.setVistas(new HashSet<>());
            rol.getVistas().addAll(vistasRol);
            return rolRepository.save(rol);
        }catch(Exception e){
            logger.log(Level.SEVERE,"Error al Guardar Rol: "+e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public ResponseBool deleteRol(Rol rol) {
        ResponseBool response = new ResponseBool(false,"","");
        try{
            //TODO rol Asociado a un Usuario

            rol.setEliminado(true);
            rol = rolRepository.save(rol);
            if(rol != null) {
                response.setMsg("Rol Eliminado");
                response.setCode("OK");
            }else{
                response.setCode("0");
                response.setMsg("Error al Guardar Rol");
            }
        }catch(Exception e){
            logger.log(Level.SEVERE,"Error al Eliminar Rol: "+e.getMessage(), e);
            response.setCode("0");
            response.setMsg("Error al Guardar Rol");
        }

        return response;
    }

    @Override
    public List<Vista> findAllVistas() {
        return vistaRepository.findAll();
    }

    @Override
    public Usuario findUsuarioById(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        try{
            Optional<Empresa> empresa = empresaRepository.findById(usuario.getEmpresa().getId());
            if(empresa.isPresent()){
                usuario.setEmpresa(empresa.get());
            }else{
                return null;
            }

            if(usuario.getPersona() != null) {
                usuario.setPersona(personaRepository.save(usuario.getPersona()));
                return usuarioRepository.save(usuario);
            } else {
                return null;
            }
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
    public List<Usuario> findAll(Empresa empresa, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return usuarioRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
    }

    @Override
    public List<Usuario> findAll(String filter, Empresa empresa, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(filter == null || filter.isEmpty()) {
            return usuarioRepository.findByEmpresaAndEliminadoIsFalse(empresa, pageable);
        } else  {
            return  usuarioRepository.search(filter, empresa, pageable);
        }
    }
}
