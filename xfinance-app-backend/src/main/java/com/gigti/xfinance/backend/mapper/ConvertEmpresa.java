package com.gigti.xfinance.backend.mapper;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import org.dozer.DozerBeanMapper;

public class ConvertEmpresa {

    private static DozerBeanMapper mapper;

    public static EmpresaDTO convertEntityToDTO(Empresa empresaEntity){
        mapper = new DozerBeanMapper();
        EmpresaDTO empresaDTO = mapper.map(empresaEntity, EmpresaDTO.class);
        return empresaDTO;
    }

    public static Empresa convertDtoToEntity(EmpresaDTO empresaDTO){
        mapper = new DozerBeanMapper();
        Empresa empresa = mapper.map(empresaDTO, Empresa.class);
        return empresa;
    }

    public static EmpresaDTO convertEntityToDTOComplete(Empresa empresaEntity, Usuario usuario){
        mapper = new DozerBeanMapper();
        EmpresaDTO empresaDTO = mapper.map(empresaEntity, EmpresaDTO.class);
        empresaDTO.setEmpresaId(empresaEntity.getId());
        if(empresaDTO != null && usuario != null) {
            //Completando el Usuario Admin
            empresaDTO.setUsuarioId(usuario.getId());
            empresaDTO.setUsuarioNombre(usuario.getNombreUsuario());
            empresaDTO.setActivoUsuario(usuario.isActivo());
            empresaDTO.setTipoIdePersona(usuario.getPersona().getTipoIde());
            empresaDTO.setIdentificacionPersona(usuario.getPersona().getIdentificacion());
            empresaDTO.setPrimerNombrePersona(usuario.getPersona().getPrimerNombre());
            empresaDTO.setSegundoNombrePersona(usuario.getPersona().getSegundoNombre());
            empresaDTO.setPrimerApellidoPersona(usuario.getPersona().getPrimerApellido());
            empresaDTO.setSegundoApellidoPersona(usuario.getPersona().getSegundoApellido());
            empresaDTO.setDireccionPersona(usuario.getPersona().getDireccion());
            empresaDTO.setTelefonoPersona(usuario.getPersona().getTelefono());
            empresaDTO.setEmailPersona(usuario.getPersona().getEmail());
        }
        return empresaDTO;
    }

}
