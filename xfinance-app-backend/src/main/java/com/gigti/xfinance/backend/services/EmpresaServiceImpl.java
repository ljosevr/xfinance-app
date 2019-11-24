package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.mapper.ConvertEmpresa;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.EmpresaRepository;
import com.gigti.xfinance.backend.repositories.PersonaRepository;
import com.gigti.xfinance.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class EmpresaServiceImpl implements IEmpresaService, HasLogger {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<EmpresaDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Empresa> listTemp = empresaRepository.findByEliminadoIsFalseAndTipoEmpresaIs(TipoEmpresa.NORMAL, pageable);
        List<EmpresaDTO> listResult = new ArrayList<>();
        for(Empresa empresa : listTemp){
            if(empresa != null){
                Usuario user = null;
                if (empresa.getUsuarios() != null) {
                    for(Usuario user1 : empresa.getUsuarios()){
                        if(user1.getTipoUsuario().equals(TipoUsuario.ADMIN)){
                            user = user1;
                            break;
                        }
                    }
                }
                EmpresaDTO empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresa, user);
                listResult.add(empresaDTO);
            }
        }
        return listResult;
    }

    @Override
    public List<EmpresaDTO> findActivoOrInactivo(boolean activo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Empresa> listTemp = empresaRepository.findActivoOrInactivo(activo, pageable);
        List<EmpresaDTO> listResult = new ArrayList<>();
        for(Empresa empresa : listTemp){
            if(empresa != null){
                Usuario user = null;
                for(Usuario user1 : empresa.getUsuarios()) {
                    if (user1.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
                        user = user1;
                        break;
                    }
                }
                EmpresaDTO empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresa, user);
                listResult.add(empresaDTO);
            }
        }
        return listResult;
    }

    @Override
    public EmpresaDTO findById(String id) {
        EmpresaDTO empresaDTO = new EmpresaDTO();
        Empresa empresa = empresaRepository.findById(id).orElse(null);
        if(empresa != null){
            Usuario user = null;
            for(Usuario user1 : empresa.getUsuarios()){
                if(user1.getTipoUsuario().equals(TipoUsuario.ADMIN)){
                    user = user1;
                    break;
                }
            }
            empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresa, user);
        }
        return empresaDTO;
    }
    @Override
    public List<EmpresaDTO> findByNombreOrDescripcion(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Empresa> listTemp = empresaRepository.findByNombreOrDescripcionAndTipoEmpresa(filter, TipoEmpresa.NORMAL, pageable);
        List<EmpresaDTO> listResult = new ArrayList<>();
        for(Empresa empresa : listTemp){
            if(empresa != null){
                Usuario user = null;
                for(Usuario user1 : empresa.getUsuarios()){
                    if(user1.getTipoUsuario().equals(TipoUsuario.ADMIN)){
                        user = user1;
                        break;
                    }
                }
                EmpresaDTO empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresa, user);
                listResult.add(empresaDTO);
            }
        }
        return listResult;
    }

    @Transactional
    @Override
    public boolean deleteEmpresa(String id) {
        try {
            Empresa empresa = empresaRepository.findById(id).orElse(null);
            if (empresa != null) {
                empresa.setEliminado(true);
                empresa = empresaRepository.save(empresa);

                //TODO eliminar el Usuario Admin Tambien.
                return empresa != null;
            }
        } catch(Exception e) {
            getLogger().debug("Error: "+e.getMessage(),e);
        }
        return false;
    }

    @Transactional
    @Override
    public EmpresaDTO saveEmpresa(EmpresaDTO empresa) {
        try{
            Empresa empresaEnt = ConvertEmpresa.convertDtoToEntity(empresa);

            if(empresaEnt.getIdInterno() == null){
                empresaEnt.setIdInterno(empresaRepository.count());
            }

            if(empresaEnt.getFechaActivacion() == null) {
                empresaEnt.setFechaActivacion(new Date());
            }
            if(empresaEnt.getTipoEmpresa() == null){
                empresaEnt.setTipoEmpresa(TipoEmpresa.NORMAL);
            }
            if(empresaEnt.getTipoIde() == null){
                empresaEnt.setTipoIde(TipoIde.NIT);
            }
            empresaEnt.setId(empresa.getEmpresaId());

            empresaEnt = empresaRepository.save(empresaEnt);

            empresa.setEmpresaId(empresaEnt.getId());
            empresa.setFechaActivacion(empresaEnt.getFechaActivacion());

            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setId(empresa.getUsuarioId());
            usuarioAdmin.setActivo(empresa.isActivoUsuario());
            usuarioAdmin.setEmpresa(empresaEnt);
            usuarioAdmin.setNombreUsuario(empresa.getUsuarioNombre());

            Persona persona = new Persona(
                    empresa.getTipoIdePersona(),
                    empresa.getIdentificacionPersona(),
                    empresa.getPrimerNombrePersona(),
                    empresa.getSegundoNombrePersona(),
                    empresa.getPrimerApellidoPersona(),
                    empresa.getSegundoApellidoPersona(),
                    null,
                    empresa.getEmailPersona(),
                    empresa.getTelefonoPersona(),
                    empresa.getDireccionPersona()
            );
            persona.setId(empresa.getPersonaId());
            persona = personaRepository.save(persona);

            usuarioAdmin.setPersona(persona);
            usuarioAdmin.setRol(Rol.ADMIN);
            usuarioAdmin.setTipoUsuario(TipoUsuario.ADMIN);
            //TODO crear metodo crear Password Aleatorio y Encriptar y Luego enviar por Email
            usuarioAdmin.setPasswordUsuario("1234");

            usuarioAdmin = usuarioRepository.save(usuarioAdmin);
            empresa.setUsuarioId(usuarioAdmin.getId());

            return empresa;

        }catch(Exception e){
            getLogger().error("Error - saveEmpresa: "+e.getMessage(), e);
            return null;
        }
    }

}
