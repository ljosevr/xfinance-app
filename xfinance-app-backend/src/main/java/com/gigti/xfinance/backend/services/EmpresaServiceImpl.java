package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.data.enums.TipoEmpresaEnum;
import com.gigti.xfinance.backend.data.enums.TipoUsuarioEnum;
import com.gigti.xfinance.backend.mapper.ConvertEmpresa;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImpl.class);
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

    @Autowired
    private ImpuestoRepository impuestoRepository;

    @Autowired
    private TipoMedidaRepository tipoMedidaRepository;

    @Autowired
    private TipoService tipoService;

    @Override
    public List<EmpresaDTO> findAll(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Empresa> empresas;
        List<EmpresaDTO> result = new ArrayList<>();
        if(filter == null || filter.isEmpty()) {
            empresas = empresaRepository.findByEliminadoIsFalseAndTipoEmpresaIs(TipoEmpresaEnum.NORMAL, pageable);
        } else  {
            empresas = empresaRepository.search(filter, TipoEmpresaEnum.NORMAL, pageable);
        }
        empresas.forEach(emp -> result.add(getData(emp)));

        return result;
    }

    private EmpresaDTO getData(Empresa emp) {
        if(emp != null){
            Usuario admin = usuarioRepository.findByEmpresaAndTipoUsuario(emp, TipoUsuarioEnum.ADMIN, true);
            EmpresaDTO empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(emp, admin);

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
                Usuario user = usuarioRepository.findByEmpresaAndTipoUsuario(empresa, TipoUsuarioEnum.ADMIN, true);
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
            Usuario user = usuarioRepository.findByEmpresaAndTipoUsuario(empresa, TipoUsuarioEnum.ADMIN, true);
            empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresa, user);
        }
        return empresaDTO;
    }
    @Override
    public List<EmpresaDTO> findByNombreOrDescripcion(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Empresa> listTemp = empresaRepository.search(filter, TipoEmpresaEnum.NORMAL, pageable);
        List<EmpresaDTO> listResult = new ArrayList<>();

        listTemp.forEach(empresa -> {
            Usuario user = usuarioRepository.findByEmpresaAndTipoUsuario(empresa, TipoUsuarioEnum.ADMIN, true);
            EmpresaDTO empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresa, user);
            listResult.add(empresaDTO);
        });

        return listResult;
    }

    @Override
    public Empresa findEmpresaDemo() {
        return empresaRepository.findByTipoEmpresa(TipoEmpresaEnum.DEMO);
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
            logger.error("Error: "+e.getMessage(),e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Empresa");
        }
        return response;
    }

    @Transactional
    public Response saveEmpresa2(EmpresaDTO empresa) {
        final String methodName = "saveEmpresa";
        logger.info("--> "+methodName);
        Response result = new Response();
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
                empresaEnt = ConvertEmpresa.convertDtoToEntity(empresa);
                if (empresaEnt.getFechaActivacion() == null) {
                    empresaEnt.setFechaActivacion(new Date());
                }
                if (empresaEnt.getTipoEmpresa() == null) {
                    empresaEnt.setTipoEmpresa(TipoEmpresaEnum.NORMAL);
                }
                if (empresaEnt.getTipoIde() == null) {
                    empresaEnt.setTipoIde(TipoIde.NIT);
                }
            }
            logger.info(empresaEnt.toString());
            Empresa empresaTemp = empresaRepository.findByNombreEmpresa(empresaEnt.getNombreEmpresa());
            if((empresaTemp == null && isNew) || (empresaTemp != null && !isNew)) {
                empresaTemp = empresaRepository.findByCodigoEmpresa(empresaEnt.getCodigoEmpresa());
                if(empresaTemp == null) {
                    empresaEnt = empresaRepository.save(empresaEnt);

                    empresa.setEmpresaId(empresaEnt.getId());
                    empresa.setFechaActivacion(empresaEnt.getFechaActivacion());

                    //Copiar Roles a Empresa
                    if (isNew) {
                        List<Rol> listaRolesOrigen = rolRepository.findAllByEmpresaAndPorDefectoAndNombreIsNotAndEliminadoFalse(null, true, "ROOT");

                        List<Rol> listaRolesDestino = new ArrayList<>();
                        Empresa finalEmpresaEnt = empresaEnt;
                        listaRolesOrigen.forEach(r -> {
                            Rol rol = new Rol();
                            rol.setFechaActivacion(new Date());
                            rol.setEmpresa(finalEmpresaEnt);
                            rol.setPorDefecto(r.isPorDefecto());
                            rol.setEliminado(r.isEliminado());
                            rol.setDescripcion(r.getDescripcion());
                            rol.setNombre(r.getNombre());
                            Set<Vista> newVistas = new HashSet<>(r.getVistas());
                            rol.setVistas(newVistas);

                            listaRolesDestino.add(rol);
                        });

                        rolRepository.saveAll(listaRolesDestino);
                    }

                    Usuario usuarioAdmin;
                    if (StringUtils.isNotBlank(empresa.getUsuarioId())) {
                        Optional<Usuario> optionalUsuario = usuarioRepository.findById(empresa.getUsuarioId());
                        if (optionalUsuario.isPresent()) {
                            usuarioAdmin = optionalUsuario.get();
                        } else {
                            isNew = true;
                            usuarioAdmin = new Usuario();
                        }
                    } else {
                        isNew = true;
                        usuarioAdmin = new Usuario();
                    }
                    if (isNew) {
                        //TODO crear metodo crear Password Aleatorio y Encriptar y Luego enviar por Email
                        usuarioAdmin.setPasswordUsuario(UtilsBackend.encrytPass("123456"));
                        usuarioAdmin.setAdminDefecto(true);

                        Rol rolAdmin = rolRepository.findByNombreAndEmpresaAndEliminado(Rol.ADMIN.getNombre(), empresaEnt, false);
                        usuarioAdmin.setRol(rolAdmin);
                        usuarioAdmin.setTipoUsuario(TipoUsuarioEnum.ADMIN);
                        usuarioAdmin.setActivo(empresa.isActivo());
                    }

                    usuarioAdmin.setActivo(empresa.isActivo());
                    usuarioAdmin.setNombreUsuario(empresa.getUsuarioNombre());

                    Persona persona;
                    //TODO Falta setear PersonaId
                    if (StringUtils.isNotBlank(empresa.getPersonaId())) {
                        Optional<Persona> optionalPersona = personaRepository.findById(empresa.getPersonaId());
                        if (optionalPersona.isPresent()) {
                            persona = optionalPersona.get();
                        } else {
                            isNew = true;
                            persona = new Persona();
                        }
                    } else {
                        isNew = true;
                        persona = new Persona();
                    }

                    persona.setTipoIde(empresa.getTipoIdePersona());
                    persona.setIdentificacion(empresa.getIdentificacionPersona());
                    persona.setPrimerNombre(empresa.getPrimerNombrePersona());
                    persona.setSegundoNombre(empresa.getSegundoNombrePersona());
                    persona.setPrimerApellido(empresa.getPrimerApellidoPersona());
                    persona.setSegundoApellido(empresa.getSegundoApellidoPersona());
                    persona.setEmpresa(empresaEnt);
                    persona.setTelefono(empresa.getTelefonoPersona());
                    persona.setDireccion(empresa.getDireccionPersona());

                    persona = personaRepository.save(persona);

                    usuarioAdmin.setPersona(persona);
                    usuarioAdmin.setEmail(empresa.getEmailPersona());

                    usuarioAdmin = usuarioRepository.save(usuarioAdmin);

                    empresa.setUsuarioId(usuarioAdmin.getId());

                    if (isNew) {
                        //Categoria de Productos
                        CategoriaProducto categoria = new CategoriaProducto();
                        categoria.setActivo(true);
                        categoria.setDescripcion("Categoria x Defecto");
                        categoria.setEliminado(false);
                        categoria.setEmpresa(empresaEnt);
                        categoria.setNombre("Normal");

                        categoriaProductoRepository.save(categoria);

                        //Impuestos
                        List<Impuesto> listImpuestos = new ArrayList<>();
                        listImpuestos.add(new Impuesto("No Aplica", BigDecimal.valueOf(0), "No Aplica Impuesto", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("5%", BigDecimal.valueOf(5), "Impuesto del 5%", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("19%", BigDecimal.valueOf(19), "Impuesto del 19%", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("Excluido", BigDecimal.ZERO, "Excluido de impuesto", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("Exento", BigDecimal.ZERO, "Exento de impuesto", true, false, empresaEnt, "SI"));

                        impuestoRepository.saveAll(listImpuestos);

                        //Tipos de Medidas
                        List<TipoMedida> tipoMedidasDefault = tipoService.findAllTiposMedidas(null);
                        Empresa finalEmpresaEnt1 = empresaEnt;
                        tipoMedidasDefault.forEach(t -> {
                            TipoMedida tm = new TipoMedida();
                            tm.setEmpresa(finalEmpresaEnt1);
                            tm.setSimbolo(t.getSimbolo());
                            tm.setNombre(t.getNombre());
                            tm.setEliminado(t.isEliminado());
                            tm.setActivo(t.isActivo());
                            tm.setDescripcion(t.getDescripcion());
                            tipoMedidaRepository.save(tm);
                        });
                    }
                    result.setObject(empresa);
                    result.setMessage("Empresa Guardada Correctamente");
                    result.setSuccess(true);
                } else {
                    result.setSuccess(false);
                    result.setMessage("Codigo de Emnpresa ya existe");
                }
            } else {
                result.setSuccess(false);
                result.setMessage("Nombre de Emnpresa ya existe");
            }
            logger.info("<-- "+methodName);
            return result;

        }catch(Exception e){
            logger.error("saveEmpresa: "+e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("Error al Crear Empresa");
            return result;
        }
    }

    @Transactional
    @Override
    public Response saveEmpresa(EmpresaDTO empresaDTO) {
        final String methodName = "saveEmpresa";
        logger.info("--> "+methodName);
        Response result = new Response();
        try{
            Empresa empresaEnt;
            boolean isNew;
            if(StringUtils.isNotBlank(empresaDTO.getEmpresaId())) {
                Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresaDTO.getEmpresaId());

                if(optionalEmpresa.isPresent()) {
                    isNew = false;
                } else {
                    isNew = true;
                }
            } else {
                isNew = true;
            }

            empresaEnt = ConvertEmpresa.convertDtoToEntity(empresaDTO);

            if(isNew) {
                
                if (empresaEnt.getFechaActivacion() == null) {
                    empresaEnt.setFechaActivacion(new Date());
                }
                if (empresaEnt.getTipoEmpresa() == null) {
                    empresaEnt.setTipoEmpresa(TipoEmpresaEnum.NORMAL);
                }
                if (empresaEnt.getTipoIde() == null) {
                    empresaEnt.setTipoIde(TipoIde.NIT);
                }
            }
            logger.info(empresaEnt.toString());
            Empresa empresaTemp = empresaRepository.findByNombreEmpresa(empresaEnt.getNombreEmpresa());
            if((empresaTemp == null && isNew) || (empresaTemp != null && !isNew)) {
                empresaTemp = empresaRepository.findByCodigoEmpresa(empresaEnt.getCodigoEmpresa());
                if(empresaTemp == null) {
                    empresaEnt = empresaRepository.save(empresaEnt);

                    empresaDTO.setEmpresaId(empresaEnt.getId());
                    empresaDTO.setFechaActivacion(empresaEnt.getFechaActivacion());

                    Usuario usuarioAdmin = setRolesPersonaAndUsuario(empresaDTO, empresaEnt, isNew);
                    empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresaEnt,usuarioAdmin);
                    empresaDTO.setUsuarioId(usuarioAdmin.getId());

                    if (isNew) {
                        //Categoria de Productos
                        CategoriaProducto categoria = new CategoriaProducto();
                        categoria.setActivo(true);
                        categoria.setDescripcion("Categoria x Defecto");
                        categoria.setEliminado(false);
                        categoria.setEmpresa(empresaEnt);
                        categoria.setNombre("Normal");

                        categoriaProductoRepository.save(categoria);

                        //Impuestos
                        List<Impuesto> listImpuestos = new ArrayList<>();
                        listImpuestos.add(new Impuesto("No Aplica", BigDecimal.valueOf(0), "No Aplica Impuesto", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("5%", BigDecimal.valueOf(5), "Impuesto del 5%", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("19%", BigDecimal.valueOf(19), "Impuesto del 19%", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("Excluido", BigDecimal.ZERO, "Excluido de impuesto", true, false, empresaEnt, "SI"));
                        listImpuestos.add(new Impuesto("Exento", BigDecimal.ZERO, "Exento de impuesto", true, false, empresaEnt, "SI"));

                        impuestoRepository.saveAll(listImpuestos);

                        //Tipos de Medidas
                        List<TipoMedida> tipoMedidasDefault = tipoService.findAllTiposMedidas(null);
                        Empresa finalEmpresaEnt1 = empresaEnt;
                        tipoMedidasDefault.forEach(t -> {
                            TipoMedida tm = new TipoMedida();
                            tm.setEmpresa(finalEmpresaEnt1);
                            tm.setSimbolo(t.getSimbolo());
                            tm.setNombre(t.getNombre());
                            tm.setEliminado(t.isEliminado());
                            tm.setActivo(t.isActivo());
                            tm.setDescripcion(t.getDescripcion());
                            tipoMedidaRepository.save(tm);
                        });
                    }
                    result.setObject(empresaDTO);
                    result.setMessage("Empresa Guardada Correctamente");
                    result.setSuccess(true);
                } else {
                    if(empresaTemp.getId() != empresaEnt.getId()) {
                        result.setSuccess(false);
                        result.setMessage("Codigo de Emnpresa ya existe");
                    } else {
                        empresaEnt = empresaRepository.save(empresaEnt);

                        Usuario usuarioAdmin = setRolesPersonaAndUsuario(empresaDTO, empresaEnt, isNew);

                        empresaDTO = ConvertEmpresa.convertEntityToDTOComplete(empresaEnt,usuarioAdmin);
                        empresaDTO.setUsuarioId(usuarioAdmin.getId());
                        empresaDTO.setFechaActivacion(empresaEnt.getFechaActivacion());

                        result.setObject(empresaDTO);
                        result.setMessage("Empresa Guardada Correctamente");
                        result.setSuccess(true);
                    }

                }
            } else {
                result.setSuccess(false);
                result.setMessage("Nombre de Emnpresa ya existe");
            }
            logger.info("<-- "+methodName);
            return result;

        }catch(Exception e){
            logger.error("saveEmpresa: "+e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("Error al Crear Empresa");
            return result;
        }
    }

    private Usuario setRolesPersonaAndUsuario(EmpresaDTO empresaDTO, Empresa empresaEnt, boolean isNew) throws NoSuchAlgorithmException {
        //Copiar Roles a Empresa
        if (isNew) {
            List<Rol> listaRolesOrigen = rolRepository.findAllByEmpresaAndPorDefectoAndNombreIsNotAndEliminadoFalse(null, true, "ROOT");

            List<Rol> listaRolesDestino = new ArrayList<>();
            Empresa finalEmpresaEnt = empresaEnt;
            listaRolesOrigen.forEach(r -> {
                Rol rol = new Rol();
                rol.setFechaActivacion(new Date());
                rol.setEmpresa(finalEmpresaEnt);
                rol.setPorDefecto(r.isPorDefecto());
                rol.setEliminado(r.isEliminado());
                rol.setDescripcion(r.getDescripcion());
                rol.setNombre(r.getNombre());
                Set<Vista> newVistas = new HashSet<>(r.getVistas());
                rol.setVistas(newVistas);

                listaRolesDestino.add(rol);
            });

            rolRepository.saveAll(listaRolesDestino);
        }

        Usuario usuarioAdmin;
        if (StringUtils.isNotBlank(empresaDTO.getUsuarioId())) {
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(empresaDTO.getUsuarioId());
            usuarioAdmin = optionalUsuario.orElseGet(Usuario::new);
        } else {
            usuarioAdmin = new Usuario();
        }
        if (isNew) {
            //TODO crear metodo crear Password Aleatorio y Encriptar y Luego enviar por Email
            usuarioAdmin.setPasswordUsuario(UtilsBackend.encrytPass("123456"));
            usuarioAdmin.setAdminDefecto(true);

            Rol rolAdmin = rolRepository.findByNombreAndEmpresaAndEliminado(Rol.ADMIN.getNombre(), empresaEnt, false);
            usuarioAdmin.setRol(rolAdmin);
            usuarioAdmin.setTipoUsuario(TipoUsuarioEnum.ADMIN);
            usuarioAdmin.setActivo(empresaDTO.isActivo());
        }

        usuarioAdmin.setActivo(empresaDTO.isActivo());
        usuarioAdmin.setNombreUsuario(empresaDTO.getUsuarioNombre());

        Persona persona;
        //TODO Falta setear PersonaId
        if (StringUtils.isNotBlank(empresaDTO.getPersonaId())) {
            Optional<Persona> optionalPersona = personaRepository.findById(empresaDTO.getPersonaId());
            persona = optionalPersona.orElseGet(Persona::new);
        } else {
            persona = new Persona();
        }

        persona.setTipoIde(empresaDTO.getTipoIdePersona());
        persona.setIdentificacion(empresaDTO.getIdentificacionPersona());
        persona.setPrimerNombre(empresaDTO.getPrimerNombrePersona());
        persona.setSegundoNombre(empresaDTO.getSegundoNombrePersona());
        persona.setPrimerApellido(empresaDTO.getPrimerApellidoPersona());
        persona.setSegundoApellido(empresaDTO.getSegundoApellidoPersona());
        persona.setEmpresa(empresaEnt);
        persona.setTelefono(empresaDTO.getTelefonoPersona());
        persona.setDireccion(empresaDTO.getDireccionPersona());

        persona = personaRepository.save(persona);

        usuarioAdmin.setPersona(persona);
        usuarioAdmin.setEmail(empresaDTO.getEmailPersona());

        usuarioAdmin = usuarioRepository.save(usuarioAdmin);

        return usuarioAdmin;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAllEmpresa(Empresa emp) {
        logger.info("--> deleteAllEmpresa");
        try {
            empresaRepository.delete(emp);
            empresaRepository.flush();
            logger.info("<-- deleteAllEmpresa");
            return true;
        }catch(Exception e) {
            logger.error("Error: "+e.getMessage(), e);
            return false;
        }
    }
}
