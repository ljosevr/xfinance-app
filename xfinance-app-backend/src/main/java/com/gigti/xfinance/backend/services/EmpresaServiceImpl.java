package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.mapper.ConvertEmpresa;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.Utils;
import com.gigti.xfinance.backend.repositories.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger logger = Logger.getLogger(EmpresaServiceImpl.class.getName());
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Override
    public List<EmpresaDTO> findAll(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Empresa> empresas;
        List<EmpresaDTO> result = new ArrayList<>();
        if(filter == null || filter.isEmpty()) {
            empresas = empresaRepository.findByEliminadoIsFalseAndTipoEmpresaIs(TipoEmpresa.NORMAL, pageable);
        } else  {
            empresas = empresaRepository.search(filter, TipoEmpresa.NORMAL, pageable);
        }
        empresas.forEach(emp -> result.add(getData(emp)));

        return result;
    }

    private EmpresaDTO getData(Empresa emp) {
        if(emp != null){
            Usuario user = null;
            if (emp.getUsuarios() != null) {
                for(Usuario user1 : emp.getUsuarios()){
                    if(user1.getTipoUsuario().equals(TipoUsuario.ADMIN)){
                        user = user1;
                        break;
                    }
                }
            }
            EmpresaDTO empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(emp, user);

            return empresaDTO;
        } else {
            return null;
        }
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
        List<Empresa> listTemp = empresaRepository.search(filter, TipoEmpresa.NORMAL, pageable);
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
    public Response deleteEmpresa(String id) {
        Response response = new Response();
        try {
            Empresa empresa = empresaRepository.findById(id).orElse(null);
            if (empresa != null) {
                empresa.setEliminado(true);
                empresa = empresaRepository.save(empresa);
                //TODO eliminar el Usuario Admin Tambien.

                if(empresa != null) {
                    response.setSuccess(true);
                    response.setMessage("Empresa Eliminada Correctamente");
                } else {
                    response.setSuccess(false);
                    response.setMessage("No Fue Posible eliminar Empresa");
                }

            } else {
                response.setSuccess(false);
                response.setMessage("Empresa No encontrado para eliminar");
            }

        } catch(Exception e) {
            logger.log(Level.SEVERE,"Error: "+e.getMessage(),e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Empresa");
        }
        return response;
    }

    @Transactional
    @Override
    public EmpresaDTO saveEmpresa(EmpresaDTO empresa) {
        try{
            Empresa empresaEnt;
            boolean isNew = false;
            if(StringUtils.isNotBlank(empresa.getEmpresaId())) {
                Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa.getEmpresaId());

                if(optionalEmpresa.isPresent()) {
                    empresaEnt = ConvertEmpresa.convertDtoToEntity(empresa);
                } else {
                    empresaEnt = new Empresa();
                    isNew = true;
                }
            } else {
                empresaEnt = new Empresa();
                isNew = true;
            }

            if(isNew) {
                if (empresaEnt.getFechaActivacion() == null) {
                    empresaEnt.setFechaActivacion(new Date());
                }
                if (empresaEnt.getTipoEmpresa() == null) {
                    empresaEnt.setTipoEmpresa(TipoEmpresa.NORMAL);
                }
                if (empresaEnt.getTipoIde() == null) {
                    empresaEnt.setTipoIde(TipoIde.NIT);
                }
            }

            empresaEnt = empresaRepository.save(empresaEnt);

            empresa.setEmpresaId(empresaEnt.getId());
            empresa.setFechaActivacion(empresaEnt.getFechaActivacion());

            Usuario usuarioAdmin;
            if(StringUtils.isNotBlank(empresa.getUsuarioId())){
                Optional<Usuario> optionalUsuario = usuarioRepository.findById(empresa.getUsuarioId());
                if(optionalUsuario.isPresent()) {
                    usuarioAdmin = optionalUsuario.get();
                } else {
                    isNew = true;
                    usuarioAdmin = new Usuario();
                }
            } else{
                isNew = true;
                usuarioAdmin = new Usuario();
            }
            if(isNew) {
                usuarioAdmin.setEmpresa(empresaEnt);
                Rol rolAdmin = rolRepository.findByNombreAndEmpresaAndEliminado(Rol.ADMIN.getNombre(), empresaEnt, false);
                usuarioAdmin.setRol(rolAdmin);
                usuarioAdmin.setTipoUsuario(TipoUsuario.ADMIN);
                //TODO crear metodo crear Password Aleatorio y Encriptar y Luego enviar por Email
                usuarioAdmin.setPasswordUsuario(Utils.encrytPass("123456"));
                usuarioAdmin.setAdminDefecto(true);
            }

            usuarioAdmin.setActivo(empresa.isActivo());
            usuarioAdmin.setNombreUsuario(empresa.getUsuarioNombre());

            Persona persona;
            //TODO Falta setear PersonaId
            if(StringUtils.isNotBlank(empresa.getPersonaId())){
                Optional<Persona> optionalPersona = personaRepository.findById(empresa.getPersonaId());
                if(optionalPersona.isPresent()) {
                    persona = optionalPersona.get();
                } else {
                    isNew = true;
                    persona = new Persona();
                }
            } else {
                isNew = true;
                persona = new Persona();
            }

            if(isNew) {
                List<Rol> listaRolesOrigen = rolRepository.findAllByEmpresaAndPorDefectoAndNombreIsNotAndEliminadoFalse(null,true, "ROOT");

                List<Rol> listaRolesDestino = new ArrayList<>();
                for(Rol r : listaRolesOrigen) {
                    Rol rol = new Rol();
                    rol.setFechaActivacion(new Date());

                    rol.setEmpresa(empresaEnt);
                    rol.setPorDefecto(r.isPorDefecto());
                    rol.setEliminado(r.isEliminado());
                    rol.setDescripcion(r.getDescripcion());
                    rol.setNombre(r.getNombre());
                    Set<Vista> newVistas = new HashSet<>();
                    newVistas.addAll(r.getVistas());
                    rol.setVistas(newVistas);

                    listaRolesDestino.add(rol);
                }

                rolRepository.saveAll(listaRolesDestino);
            }

            persona.setTipoIde(empresa.getTipoIdePersona());
            persona.setIdentificacion(empresa.getIdentificacionPersona());
            persona.setPrimerNombre(empresa.getPrimerNombrePersona());
            persona.setSegundoNombre(empresa.getSegundoNombrePersona());
            persona.setPrimerApellido(empresa.getPrimerApellidoPersona());
            persona.setSegundoApellido(empresa.getSegundoApellidoPersona());
            persona.setEmail(empresa.getEmailPersona());
            persona.setTelefono(empresa.getTelefonoPersona());
            persona.setDireccion(empresa.getDireccionPersona());

            persona = personaRepository.save(persona);

            usuarioAdmin.setPersona(persona);

            usuarioAdmin = usuarioRepository.save(usuarioAdmin);

            empresa.setUsuarioId(usuarioAdmin.getId());

            if(isNew) {
                //Categoria de Productos
                CategoriaProducto categoria = new CategoriaProducto();
                categoria.setActivo(true);
                categoria.setDescripcion("Categoria x Defecto");
                categoria.setEliminado(false);
                categoria.setEmpresa(empresaEnt);
                categoria.setNombre("Normal");

                categoriaProductoRepository.save(categoria);
            }
            return empresa;

        }catch(Exception e){
            logger.log(Level.SEVERE,"saveEmpresa: "+e.getMessage(), e);
            return null;
        }
    }
}
