package com.gigti.xfinance.backend.mapper;

import com.gigti.xfinance.backend.data.Persona;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioDTO;
import org.dozer.DozerBeanMapper;

public class ConvertUsuario {

    private static DozerBeanMapper mapper;

    public static UsuarioDTO convertEntityToDTO(Usuario usuario){
        mapper = new DozerBeanMapper();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        if(usuario != null) {
            usuarioDTO.setRol(usuario.getRol());
            usuarioDTO.setUsuarioid(usuario.getId());
            usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
            usuarioDTO.setEmpresa(usuario.getEmpresa());
            usuarioDTO.setActivo(usuario.isActivo());
            usuarioDTO.setEliminado(usuario.isEliminado());
            usuarioDTO.setDireccion(usuario.getPersona().getDireccion());
            usuarioDTO.setEmail(usuario.getPersona().getEmail());
            usuarioDTO.setIdentificacion(usuario.getPersona().getIdentificacion());
            usuarioDTO.setTipoIde(usuario.getPersona().getTipoIde());
            usuarioDTO.setPrimerApellido(usuario.getPersona().getPrimerApellido());
            usuarioDTO.setPrimerNombre(usuario.getPersona().getPrimerNombre());
            usuarioDTO.setSegundoApellido(usuario.getPersona().getSegundoApellido());
            usuarioDTO.setSegundoNombre(usuario.getPersona().getSegundoNombre());
            usuarioDTO.setTelefono(usuario.getPersona().getTelefono());
            usuarioDTO.setFechaNacimiento(usuario.getPersona().getFechaNacimiento());
            usuarioDTO.setId(usuario.getPersona().getId());
        }

        return usuarioDTO;
    }

    public static Usuario convertDtoToEntity(UsuarioDTO usuarioDTO){
        mapper = new DozerBeanMapper();

        Usuario usuario = new Usuario();
        if(usuarioDTO != null) {
            if(usuarioDTO.getUsuarioid() != null && !usuarioDTO.getUsuarioid().isEmpty()){
                usuario.setId(usuarioDTO.getUsuarioid());
            }

            usuario.setRol(usuarioDTO.getRol());
            usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
            usuario.setEmpresa(usuarioDTO.getEmpresa());
            usuario.setActivo(usuarioDTO.isActivo());
            usuario.setEliminado(usuarioDTO.isEliminado());

            Persona persona = new Persona();

            persona.setDireccion(usuarioDTO.getDireccion());
            persona.setEmail(usuarioDTO.getEmail());
            persona.setIdentificacion(usuarioDTO.getIdentificacion());
            persona.setTipoIde(usuarioDTO.getTipoIde());
            persona.setPrimerApellido(usuarioDTO.getPrimerApellido());
            persona.setPrimerNombre(usuarioDTO.getPrimerNombre());
            persona.setSegundoApellido(usuarioDTO.getSegundoApellido());
            persona.setSegundoNombre(usuarioDTO.getSegundoNombre());
            persona.setTelefono(usuarioDTO.getTelefono());
            persona.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
            persona.setId(usuarioDTO.getId());
            usuario.setPersona(persona);
        }
        
        return usuario;
    }

}
