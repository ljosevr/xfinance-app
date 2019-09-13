package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IusuarioServiceImpl implements IusuarioService, HasLogger {

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

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if(usuario != null) {

            //if(passwordEncoder.encode(password).equals(passwordEncoder.encode(usuario.getPasswordUsuario()))){
            if(password.equals(usuario.getPasswordUsuario())){
                //TODO
                //Ejecutar parches de initBackend - De Compañia
                getLogger().debug("Iniciando App Backend");
                getLogger().debug("Finalizando App Backend");
                return usuario;
            }else{
                return null;
            }
        }
        return null;
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        return null;
    }
}
