package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IusuarioServiceImpl implements IusuarioService {

    Logger logger = LoggerFactory.getLogger(IinitBackServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private IinitBackService iinitBackService;

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
}
