package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.mapper.ConvertEmpresa;
import com.gigti.xfinance.backend.repositories.*;
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
                //rolPermisoRepository.fin
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
            logger.log(Level.SEVERE, "deleteEmpresa: "+e.getMessage(),e);
        }
        return false;
    }

    @Transactional
    @Override
    public EmpresaDTO saveEmpresa(EmpresaDTO empresa) {
        try{
            Empresa empresaEnt = ConvertEmpresa.convertDtoToEntity(empresa);

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

            List<Rol> listaRolesOrigen = rolRepository.findAllByEmpresaAndPorDefectoAndNombreIsNotAndEliminadoFalse(null,true, "ROOT");

            List<Rol> listaRolesDestino = new ArrayList<>();
            for(Rol r : listaRolesOrigen){
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

            Rol rolAdmin = rolRepository.findByNombreAndEmpresaAndEliminado(Rol.ADMIN.getNombre(), empresaEnt, false);

            usuarioAdmin.setPersona(persona);
            usuarioAdmin.setRol(rolAdmin);
            usuarioAdmin.setTipoUsuario(TipoUsuario.ADMIN);
            //TODO crear metodo crear Password Aleatorio y Encriptar y Luego enviar por Email
            usuarioAdmin.setPasswordUsuario("1234");

            usuarioAdmin = usuarioRepository.save(usuarioAdmin);
            empresa.setUsuarioId(usuarioAdmin.getId());

            //Categoria de Productos
            CategoriaProducto categoria = new CategoriaProducto();
            categoria.setActivo(true);
            categoria.setDescripcion("Categoria x Defecto");
            categoria.setEliminado(false);
            categoria.setEmpresa(empresaEnt);
            categoria.setNombre("Normal");

            categoriaProductoRepository.save(categoria);

            return empresa;

        }catch(Exception e){
            logger.log(Level.SEVERE,"saveEmpresa: "+e.getMessage(), e);
            return null;
        }
    }
}
