package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.data.enums.TipoEmpresaEnum;
import com.gigti.xfinance.backend.data.enums.TipoMedidaEnum;
import com.gigti.xfinance.backend.data.enums.TipoUsuarioEnum;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.UtilsBackend;
import com.gigti.xfinance.backend.repositories.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class InitBackServiceImpl implements InitBackService {

    private static final java.util.logging.Logger logger = Logger.getLogger(InitBackServiceImpl.class.getName());

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private TipoIdeRepository tipoIdeRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ParcheRepository parcheRepository;
    @Autowired
    private VistaRepository vistaRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private CategoriaProductoService categoriaProductoService;

    @Autowired
    private ImpuestoRepository impuestoRepository;

    // Tipos de Datos
    @Transactional
    @Override
    public void initBackTipos() {
        logger.info("--> initBackTipos");
        try {
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT1, null);
            if (parche == null) {
                //Tipo Ide
                tipoIdeRepository.save(TipoIde.CEDULA);
                tipoIdeRepository.save(TipoIde.EXTRANJERIA);
                tipoIdeRepository.save(TipoIde.NIT);
                tipoIdeRepository.save(TipoIde.TIDENTIDAD);
                logger.info("Tipos IDE");

                //Vistas
                Vista vista_venta = vistaRepository.save(new Vista(Constantes.VIEW_PVENTA,null,null, 1, null));//CASH
                Vista vista_producto = vistaRepository.save(new Vista(Constantes.VIEW_PRODUCTO ,null, null, 2, null));
                Vista vista_usuarios = vistaRepository.save(new Vista(Constantes.VIEW_USUARIO,null,null,3,null));

                //PERFIL
                Vista vista_empresa = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESA,Constantes.VIEW_R_EMPRESA, null, 4, "OFFICE"));

                Vista vista_empresaMaster = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESAS,null, null, 5, null));
                Vista vista_reportes = vistaRepository.save(new Vista(Constantes.VIEW_REPORTS,null, null, 6, null));

                Vista vista_config = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG,null, null, 7, null));

                //1.Punto de Venta
                Vista vista_vender = vistaRepository.save(new Vista(Constantes.VIEW_REGISTRAR,Constantes.VIEW_R_VENTA, vista_venta, 11, "CASH"));//CASH

                //2. Productos
                Vista vista_admin_prod = vistaRepository.save(new Vista(Constantes.VIEW_PRODUCTO_ADMIN,Constantes.VIEW_R_PRODUCTO, vista_producto, 21,"CUBE"));//COMPILE
                Vista vista_categoria = vistaRepository.save(new Vista(Constantes.VIEW_CATEGORIA,Constantes.VIEW_R_CATEGORIA, vista_producto, 22,"CUBES"));
                Vista vista_compras = vistaRepository.save(new Vista(Constantes.VIEW_COMPRAS,Constantes.VIEW_R_COMPRAS,vista_producto, 23, "CART_O"));
                Vista vista_invInicial = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO_INICIAL,Constantes.VIEW_R_INVENTARIO_INICIAL,vista_producto,24, "STOCK"));
                Vista vista_invActual = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO_ACTUAL,Constantes.VIEW_R_INVENTARIO_ACTUAL,vista_producto,25, "STORAGE"));

                //3.Usuarios
                Vista vista_admin_usu = vistaRepository.save(new Vista(Constantes.VIEW_USUARIO_ADMIN,Constantes.VIEW_R_USUARIOS,vista_usuarios,31, "GROUP"));
                Vista vista_rol = vistaRepository.save(new Vista(Constantes.VIEW_ROL,Constantes.VIEW_R_ROL,vista_usuarios, 32,"DOCTOR"));

                //5.Empresa Master
                Vista vista_admin_empMaster = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESA_ADMIN,Constantes.VIEW_R_EMPRESA_MASTER, vista_empresaMaster, 51, "OFFICE"));
                Vista vista_usuario_emp = vistaRepository.save(new Vista(Constantes.VIEW_EMP_USU_ADMIN,Constantes.VIEW_R_USUARIOADMIN,vista_empresaMaster, 52, "GROUP"));

                //6.Reportes
                Vista vista_invHoy = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO,Constantes.VIEW_R_INVENTARIO,vista_reportes, 61, "STORAGE"));
                Vista vista_balance = vistaRepository.save(new Vista(Constantes.VIEW_GANANCIAS_Y_PERDIDAS,Constantes.VIEW_R_GANANCIAS_Y_PERDIDAS,vista_reportes, 62,"CHART_LINE"));

                //7. Config
                Vista vista_config_perfil = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG_PERFIL,Constantes.VIEW_R_CONFIG_PERFIL,vista_config, 63, "COG"));
                Vista vista_config_empresa = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG_EMPRESA,Constantes.VIEW_R_CONFIG_EMPRESA,vista_config, 64,"COGS"));
                Vista vista_config_password = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG_PASSWORD,Constantes.VIEW_R_CONFIG_PASSWORD,vista_config, 65, "PASSWORD"));

                logger.info("Vistas");

                //Roles
                //1. ROOT
                Rol.ROOT.setVistas(new HashSet<>());
                Rol.ROOT.getVistas().add(vista_empresaMaster);
                Rol.ROOT.getVistas().add(vista_admin_empMaster);
                Rol.ROOT.getVistas().add(vista_usuario_emp);
                Rol.ROOT.getVistas().add(vista_config);
                Rol.ROOT.getVistas().add(vista_config_perfil);
                Rol.ROOT.getVistas().add(vista_config_password);
                Rol.ROOT.setFechaActivacion(new Date());
                rolRepository.save(Rol.ROOT);

                logger.info("Rol Root");

                //2. ADMIN
                Rol.ADMIN.setVistas(new HashSet<>());
                Rol.ADMIN.getVistas().add(vista_venta);
                Rol.ADMIN.getVistas().add(vista_vender);
                Rol.ADMIN.getVistas().add(vista_producto);
                Rol.ADMIN.getVistas().add(vista_admin_prod);
                Rol.ADMIN.getVistas().add(vista_categoria);
                Rol.ADMIN.getVistas().add(vista_compras);
                Rol.ADMIN.getVistas().add(vista_invInicial);
                Rol.ADMIN.getVistas().add(vista_invActual);
                Rol.ADMIN.getVistas().add(vista_usuarios);
                Rol.ADMIN.getVistas().add(vista_admin_usu);
                Rol.ADMIN.getVistas().add(vista_rol);
                Rol.ADMIN.getVistas().add(vista_reportes);
                Rol.ADMIN.getVistas().add(vista_invHoy);
                Rol.ADMIN.getVistas().add(vista_balance);
                Rol.ADMIN.getVistas().add(vista_config);
                Rol.ADMIN.getVistas().add(vista_config_perfil);
                Rol.ADMIN.getVistas().add(vista_config_empresa);
                Rol.ADMIN.getVistas().add(vista_config_password);
                Rol.ADMIN.setFechaActivacion(new Date());
                rolRepository.save(Rol.ADMIN);
                logger.info("Rol Admin");

                //3.VENDEDOR
                Rol.VENDEDOR.setVistas(new HashSet<>());
                Rol.VENDEDOR.getVistas().add(vista_venta);
                Rol.VENDEDOR.getVistas().add(vista_vender);
                Rol.VENDEDOR.getVistas().add(vista_producto);
                Rol.VENDEDOR.getVistas().add(vista_invActual);
                Rol.VENDEDOR.getVistas().add(vista_config);
                Rol.VENDEDOR.getVistas().add(vista_config_perfil);
                Rol.VENDEDOR.getVistas().add(vista_config_password);
                Rol.VENDEDOR.setFechaActivacion(new Date());
                rolRepository.save(Rol.VENDEDOR);
                logger.info("Rol Vendedor");

                //4.AUXILIAR
                Rol.AUXILIAR.setVistas(new HashSet<>());
                Rol.AUXILIAR.getVistas().add(vista_venta);
                Rol.AUXILIAR.getVistas().add(vista_vender);
                Rol.AUXILIAR.getVistas().add(vista_producto);
                Rol.AUXILIAR.getVistas().add(vista_admin_prod);
                Rol.AUXILIAR.getVistas().add(vista_categoria);
                Rol.AUXILIAR.getVistas().add(vista_invActual);
                Rol.AUXILIAR.getVistas().add(vista_config);
                Rol.AUXILIAR.getVistas().add(vista_config_perfil);
                Rol.AUXILIAR.getVistas().add(vista_config_password);
                //Rol.AUXILIAR.getVistas().add(vista_compras);
                Rol.AUXILIAR.setFechaActivacion(new Date());
                rolRepository.save(Rol.AUXILIAR);
                logger.info("Rol Auxiliar");

                parche = new Parche(Constantes.INIT1,java.sql.Date.valueOf(LocalDate.now()),true, null);
                parcheRepository.save(parche);
                logger.info("Parche creado");
                logger.info("<-- initBackTipos");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al Crear InitBackend - Tipos: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public void initBackObjetos() {
        logger.info("--> initBackObjetos");
        try{
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT2, null);
            if (parche == null) {

                //Compañia GigTi
                Empresa emp = new Empresa(
                        TipoIde.NIT,
                        "900800",
                        "Gig Ti S.A.S",
                        "Calle 191A # 11A-92",
                        true,
                        java.sql.Date.valueOf(LocalDate.now()),
                        null,
                        TipoEmpresaEnum.ROOT, "GIG");

                emp = empresaRepository.save(emp);
                logger.info("Empresa Root Creado");

                Persona persona = new Persona(
                        TipoIde.CEDULA,
                        "73008913",
                        "Luis",
                        "José",
                        "Villarreal",
                        "Rincón",
                        java.sql.Date.valueOf(LocalDate.of(1985, 6, 11)),
                        "ljosevr@gmail.com",
                        "3006520012",
                        "calle 191A#11A-92"
                );

                persona = personaRepository.save(persona);
                logger.info("Persona Root Creado");

                //Usuario Root
                Rol rolRoot = rolRepository.findByNombreAndEmpresaAndEliminado(Rol.ROOT.getNombre(), null, false);

                String pass = UtilsBackend.encrytPass("jose410Angel");
                Usuario userRoot = new Usuario(
                        "Root",
                        pass,
                        true,
                        persona,
                        emp,
                        rolRoot,
                        TipoUsuarioEnum.ROOT
                );

                usuarioRepository.save(userRoot);
                logger.info("Usuario Root Creado");

                parche = new Parche(Constantes.INIT2,java.sql.Date.valueOf(LocalDate.now()),true, null);
                logger.info("Parche Creado");
                parcheRepository.save(parche);
            }
            logger.info("<-- initBackObjetos");
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error al Crear InitBackend - Objetos: "+e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public void initBackDemo() {
        logger.info("--> initBackDemo");
        try{
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT5, null);
            if (parche == null) {

                EmpresaDTO empresa = new EmpresaDTO();
                empresa.setCodigoEmpresa("DEMO");
                empresa.setEmailPersona("ljosevr3@gmail.com");
                empresa.setTelefonoPersona("3006600000");
                empresa.setDireccionPersona("Calle Espectacular");
                empresa.setSegundoApellidoPersona("Rogers");
                empresa.setPrimerApellidoPersona("Stark");
                empresa.setPrimerNombrePersona("Tony");
                empresa.setSegundoNombrePersona("Steve");
                empresa.setIdentificacionPersona("0011223344");
                empresa.setTipoIdePersona(TipoIde.CEDULA);
                empresa.setActivoUsuario(true);
                empresa.setUsuarioNombre("demo");
                empresa.setNombreEmpresa("Demo S.A.S");
                empresa.setEliminado(false);
                empresa.setTipoEmpresa(TipoEmpresaEnum.NORMAL);
                empresa.setDireccion("Wakanda");
                empresa.setIdentificacion("800900700600");
                empresa = empresaService.saveEmpresa(empresa);

                if(empresa != null){
                    logger.info("Empresa DEMO Creada");

                    //Crear Productos
                    //Consultar Empresa byId
                    Empresa emp = empresaRepository.findById(empresa.getEmpresaId()).orElse(null);
                    //Consultar Impuestos
                    Pageable pageable = PageRequest.of(0, 5);
                    List<Impuesto> impuestos = impuestoRepository.findByEmpresaAndEliminadoIsFalse(emp, pageable);

                    //Consultar Categorias
                    CategoriaProducto categoria = categoriaProductoService.findByNombreOrDescripcion("Normal", emp, 0, 10).get(0);
                    //Tipos de Medidas
                    TipoMedidaEnum[] tipos =  TipoMedidaEnum.values();
                    if(emp != null) {
                        for(int i = 0; i < 50 ; i++) {
                            Producto producto = new Producto();
                            producto.setActivo(true);
                            producto.setCategoria(categoria);
                            producto.setCodigoBarra(StringUtils.leftPad((i + 1) + "", 10, "0"));
                            producto.setDescripcion("Producto Demo " + (i + 1));
                            producto.setEmpresa(emp);
                            Double aleatorio = Math.floor(Math.random()*5);
                            producto.setImpuesto(impuestos.get(aleatorio.intValue()));
                            producto.setNombreProducto("Producto # " + (i + 1));
                            aleatorio = Math.floor(Math.random()*8);
                            producto.setTipoMedida(tipos[aleatorio.intValue()]);

                            producto = productoRepository.save(producto);

                            //Inventario Actual
                            InventarioActual actual = new InventarioActual();
                            actual.setInfinite(false);
                            actual.setFechaActualizacion(new Date());
                            actual.setCantidad(BigDecimal.valueOf((i + 1) * 2));
                            actual.setProducto(producto);

                            inventarioActualRepository.save(actual);
                        }

                        parche = new Parche(Constantes.INIT5,java.sql.Date.valueOf(LocalDate.now()),true, null);
                        logger.info("Parche Demo Creado");
                        parcheRepository.save(parche);

                    } else {
                        logger.info("Error al Buscar Empresa");
                    }
                } else {
                    logger.info("Error al crear Empresa DEMO");
                }

            }
            logger.info("<-- initBackObjetos");
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error al Crear InitBackend - Objetos: "+e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
