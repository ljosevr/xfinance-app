package com.gigti.xfinance.backend.mapper;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import org.dozer.DozerBeanMapper;

public class ConvertUsuario {

    private static DozerBeanMapper mapper;

    /*public static EmpresaDTO convertEntityToDTO(Empresa empresaEntity){
        mapper = new DozerBeanMapper();
        EmpresaDTO empresaDTO = mapper.map(empresaEntity, EmpresaDTO.class);
        return empresaDTO;
    }*/

    public static Usuario convertDtoToEntity(EmpresaDTO empresaDTO){
        mapper = new DozerBeanMapper();
        Usuario usuario = mapper.map(empresaDTO, Usuario.class);
        return usuario;
    }

}
