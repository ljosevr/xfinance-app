package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.ResponseBool;
import com.gigti.xfinance.backend.repositories.RolRepository;
import com.gigti.xfinance.backend.repositories.UsuarioRepository;
import com.gigti.xfinance.backend.repositories.VistaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private VistaRepository vistaRepository;
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
    public Usuario login(String nombreUsuario, String password) {
        try{
            Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
            if(usuario != null) {

                //if(passwordEncoder.encode(password).equals(passwordEncoder.encode(usuario.getPasswordUsuario()))){
                if(password.equals(usuario.getPasswordUsuario())){
                    //TODO
                    //Ejecutar parches de initBackend - De Compañia
                    logger.debug("Iniciando App Backend");
                    logger.debug("Finalizando App Backend");
                    return usuario;
                }else{
                    return null;
                }
            }
        } catch(Exception e){
            logger.error("Error al hacer Login: "+e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        return null;
    }

    @Override
    public List<Rol> findAllRol(Empresa empresa, boolean activo) {
        return rolRepository.findAllByEmpresaAndEliminado(empresa, activo);
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
            logger.error("Error al Guardar Rol: "+e.getMessage(), e);
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
            logger.error("Error al Eliminar Rol: "+e.getMessage(), e);
            response.setCode("0");
            response.setMsg("Error al Guardar Rol");
        }

        return response;
    }

    @Override
    public List<Vista> findAllVistas() {
        return vistaRepository.findAll();
    }
}
