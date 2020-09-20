package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.data.enums.TipoEmpresaEnum;
import com.gigti.xfinance.backend.data.enums.TipoUsuarioEnum;
import com.gigti.xfinance.backend.others.Constantes;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


@Service
public class InitBackServiceImpl implements InitBackService {

    private static final Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private TipoIdeRepository tipoIdeRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ParcheRepository parcheRepository;
    @Autowired
    private VistaRepository vistaRepository;
    @Autowired
    private ImpuestoRepository impuestoRepository;
    @Autowired
    private TipoMedidaRepository tipoMedidaRepository;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaProductoService categoriaProductoService;
    @Autowired
    private VentaService ventaService;
    @Autowired
    private CompraService compraService;
    @Autowired
    private TipoService tipoService;
    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private ImpuestoService impuestoService;
    @Autowired
    private MovimientoService movimientoService;
    @Autowired
    private ProductoValorVentaService productoValorVentaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private RolService rolService;

    @Transactional
    @Override
    public void initParches() {
        initBackTipos();
        initBackObjetos();
        initBackTiposMedidas();
    }

    // Tipos de Datos
    private void initBackTipos() {
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
                Vista vista_producto = vistaRepository.save(new Vista(Constantes.VIEW_PRODUCTO ,null, null, 100, "COMPILE"));
                Vista vista_inventario = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO ,null, null, 200, "STOCK"));

                Vista vista_reportes = vistaRepository.save(new Vista(Constantes.VIEW_REPORTS,null, null, 600, "FILE_TEXT_O"));
                Vista vista_usuarios = vistaRepository.save(new Vista(Constantes.VIEW_USUARIO,null,null,700,"GROUP"));
                //Vista vista_empresa = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESA,Constantes.VIEW_R_EMPRESA, null, 800, "OFFICE"));
                //Vista vista_empresaMaster = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESAS,null, null, 900, null));
                Vista vista_config = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG,null, null, 1000, "TOOLS"));

                //100. Productos
                Vista vista_admin_prod = vistaRepository.save(new Vista(Constantes.VIEW_PRODUCTO_ADMIN,Constantes.VIEW_C_PRODUCTO, vista_producto, 101,"CUBE"));//COMPILE
                Vista vista_categoria = vistaRepository.save(new Vista(Constantes.VIEW_CATEGORIA,Constantes.VIEW_C_CATEGORIA, vista_producto, 102,"CUBES"));
                Vista vista_compras = vistaRepository.save(new Vista(Constantes.VIEW_COMPRAS,Constantes.VIEW_C_COMPRAS,vista_producto, 103, "CART_O"));

                //200. Inventarios
                Vista vista_invInicial = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO_INICIAL,Constantes.VIEW_C_INVENTARIO_INICIAL,vista_inventario,201, "STOCK"));
                Vista vista_invActual  = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO_ACTUAL,Constantes.VIEW_C_INVENTARIO_ACTUAL,vista_inventario,202, "STORAGE"));

                //300.Punto de Venta
                Vista vista_vender = vistaRepository.save(new Vista(Constantes.VIEW_REGISTRAR,Constantes.VIEW_C_VENTA, null, 301, "CASH"));//CASH

                //400.Clientes
                Vista vista_admin_cliente = vistaRepository.save(new Vista(Constantes.VIEW_ADMIN_CLIENTE,Constantes.VIEW_C_ADMIN_CLIENTE, null, 401, "HANDSHAKE"));//CASH

                //500.Proveedores
                Vista vista_admin_proveedor = vistaRepository.save(new Vista(Constantes.VIEW_ADMIN_PROVEEDOR,Constantes.VIEW_C_ADMIN_PROVEEDOR, null, 501, "CLUSTER"));//CASH

                //600.Reportes
                Vista vista_balance = vistaRepository.save(new Vista(Constantes.VIEW_GANANCIAS_Y_PERDIDAS,Constantes.VIEW_C_GANANCIAS_Y_PERDIDAS,vista_reportes, 601,"CHART_LINE"));
                Vista vista_ventas = vistaRepository.save(new Vista(Constantes.VIEW_VENTAS,Constantes.VIEW_C_VENTAS,vista_reportes, 602,"TRENDING_UP"));

                //700.Usuarios
                Vista vista_admin_usu = vistaRepository.save(new Vista(Constantes.VIEW_USUARIO_ADMIN,Constantes.VIEW_C_USUARIOS,vista_usuarios,701, "MALE"));
                Vista vista_rol = vistaRepository.save(new Vista(Constantes.VIEW_ROL,Constantes.VIEW_C_ROL,vista_usuarios, 702,"USER_CARD"));

                //900.Empresa Master
                Vista vista_admin_empMaster = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESA_ADMIN,Constantes.VIEW_C_EMPRESA_MASTER, null, 901, "OFFICE"));

                //1000. Config
                Vista vista_config_perfil = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG_PERFIL,Constantes.VIEW_C_CONFIG_PERFIL,vista_config, 1001, "COG"));
                Vista vista_config_empresa = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG_EMPRESA,Constantes.VIEW_C_CONFIG_EMPRESA,vista_config, 1002,"COGS"));
                Vista vista_config_password = vistaRepository.save(new Vista(Constantes.VIEW_CONFIG_PASSWORD,Constantes.VIEW_C_CONFIG_PASSWORD,vista_config, 1003, "PASSWORD"));
                logger.info("Vistas");

                //Roles
                //1. ROOT
                //Empresa Master
                Rol.ROOT.setVistas(new HashSet<>());
                //Rol.ROOT.getVistas().add(vista_empresaMaster);
                Rol.ROOT.getVistas().add(vista_admin_empMaster);
                //Config
                Rol.ROOT.getVistas().add(vista_config);
                Rol.ROOT.getVistas().add(vista_config_perfil);
                Rol.ROOT.getVistas().add(vista_config_password);
                Rol.ROOT.setFechaActivacion(new Date());
                rolRepository.save(Rol.ROOT);
                logger.info("Rol Root");

                //2. ADMIN
                Rol.ADMIN.setVistas(new HashSet<>());
                //Producto
                Rol.ADMIN.getVistas().add(vista_producto);
                Rol.ADMIN.getVistas().add(vista_admin_prod);
                Rol.ADMIN.getVistas().add(vista_categoria);
                Rol.ADMIN.getVistas().add(vista_compras);
                //Inventario
                Rol.ADMIN.getVistas().add(vista_inventario);
                Rol.ADMIN.getVistas().add(vista_invInicial);
                Rol.ADMIN.getVistas().add(vista_invActual);
                //Punto de Venta
                Rol.ADMIN.getVistas().add(vista_vender);
                //Clientes
                Rol.ADMIN.getVistas().add(vista_admin_cliente);
                //Proveedores
                Rol.ADMIN.getVistas().add(vista_admin_proveedor);
                //Reportes
                Rol.ADMIN.getVistas().add(vista_reportes);
                Rol.ADMIN.getVistas().add(vista_balance);
                Rol.ADMIN.getVistas().add(vista_ventas);
                //Usuarios
                Rol.ADMIN.getVistas().add(vista_usuarios);
                Rol.ADMIN.getVistas().add(vista_admin_usu);
                Rol.ADMIN.getVistas().add(vista_rol);
                //Config
                Rol.ADMIN.getVistas().add(vista_config);
                Rol.ADMIN.getVistas().add(vista_config_perfil);
                Rol.ADMIN.getVistas().add(vista_config_empresa);
                Rol.ADMIN.getVistas().add(vista_config_password);
                Rol.ADMIN.setFechaActivacion(new Date());
                rolRepository.save(Rol.ADMIN);
                logger.info("Rol Admin");

                //3.VENDEDOR
                Rol.VENDEDOR.setVistas(new HashSet<>());
                Rol.VENDEDOR.getVistas().add(vista_vender);
                //Inventario
                Rol.VENDEDOR.getVistas().add(vista_inventario);
                Rol.VENDEDOR.getVistas().add(vista_invActual);
                //Clientes
                Rol.VENDEDOR.getVistas().add(vista_admin_cliente);
                //Reportes
                Rol.VENDEDOR.getVistas().add(vista_reportes);
                Rol.VENDEDOR.getVistas().add(vista_ventas);
                //Config
                Rol.VENDEDOR.getVistas().add(vista_config);
                Rol.VENDEDOR.getVistas().add(vista_config_perfil);
                Rol.VENDEDOR.getVistas().add(vista_config_password);
                Rol.VENDEDOR.setFechaActivacion(new Date());
                rolRepository.save(Rol.VENDEDOR);
                logger.info("Rol Vendedor");

                //4.AUXILIAR
                Rol.AUXILIAR.setVistas(new HashSet<>());
                //Puntos de Venta
                Rol.AUXILIAR.getVistas().add(vista_vender);
                //producto
                Rol.AUXILIAR.getVistas().add(vista_producto);
                Rol.AUXILIAR.getVistas().add(vista_admin_prod);
                Rol.AUXILIAR.getVistas().add(vista_categoria);
                Rol.AUXILIAR.getVistas().add(vista_compras);
                //Inventario
                Rol.AUXILIAR.getVistas().add(vista_inventario);
                Rol.AUXILIAR.getVistas().add(vista_invActual);
                //Clientes
                Rol.AUXILIAR.getVistas().add(vista_admin_cliente);
                //Proveedores
                Rol.AUXILIAR.getVistas().add(vista_admin_proveedor);
                //Reportes
                Rol.AUXILIAR.getVistas().add(vista_reportes);
                Rol.AUXILIAR.getVistas().add(vista_ventas);
                //config
                Rol.AUXILIAR.getVistas().add(vista_config);
                Rol.AUXILIAR.getVistas().add(vista_config_perfil);
                Rol.AUXILIAR.getVistas().add(vista_config_password);
                Rol.AUXILIAR.setFechaActivacion(new Date());
                rolRepository.save(Rol.AUXILIAR);
                logger.info("Rol Auxiliar");

                parche = new Parche(Constantes.INIT1,java.sql.Date.valueOf(LocalDate.now()),true, null);
                parcheRepository.save(parche);
                logger.info("Parche creado");
                logger.info("<-- initBackTipos");
            }
        } catch (Exception e) {
            logger.error("Error al Crear InitBackend - Tipos: " + e.getMessage(), e);
        }
    }

    private void initBackObjetos() {
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
                        "3006520012",
                        "calle 191A#11A-92",
                        emp
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
                        rolRoot,
                        TipoUsuarioEnum.ROOT,
                        "ljosevr@gmail.com"
                );

                usuarioService.saveUsuario(userRoot);
                logger.info("Usuario Root Creado");

                parche = new Parche(Constantes.INIT2,java.sql.Date.valueOf(LocalDate.now()),true, null);
                logger.info("Parche Creado");
                parcheRepository.save(parche);
            }
            logger.info("<-- initBackObjetos");
        }catch(Exception e){
            logger.error("Error al Crear InitBackend - Objetos: "+e.getMessage(), e);
        }
    }

    // Tipos de Medidas
    private void initBackTiposMedidas() {
        logger.info("--> initBackTipos");
        try {

            TipoMedida tm1 = new TipoMedida("UNIDAD", "Unid","",true, false,null,"");
            tipoMedidaRepository.save(tm1);

            TipoMedida tm2 = new TipoMedida("PAQUETE", "Pq","",true, false,null,"");
            tipoMedidaRepository.save(tm2);

            TipoMedida tm3 = new TipoMedida("CAJA", "Cja","",true, false,null,"");
            tipoMedidaRepository.save(tm3);

            TipoMedida tm4 = new TipoMedida("KILO", "kg","",true, false,null,"");
            tipoMedidaRepository.save(tm4);

            TipoMedida tm5 = new TipoMedida("LIBRA", "lb","",true, false,null,"");
            tipoMedidaRepository.save(tm5);

            TipoMedida tm6 = new TipoMedida("GRAMOS", "gr","",true, false,null,"");
            tipoMedidaRepository.save(tm6);

            TipoMedida tm7 = new TipoMedida("LITRO", "l","",true, false,null,"");
            tipoMedidaRepository.save(tm7);

            TipoMedida tm8 = new TipoMedida("MILI LITRO", "ml","",true, false,null,"");
            tipoMedidaRepository.save(tm8);

            TipoMedida tm9 = new TipoMedida("METRO", "m","",true, false,null,"");
            tipoMedidaRepository.save(tm9);

            TipoMedida tm10 = new TipoMedida("CENTIMETRO", "cm" ,"",true, false,null,"");
            tipoMedidaRepository.save(tm10);

            TipoMedida tm11 = new TipoMedida("GALÓN", "Gl" ,"",true, false,null,"");
            tipoMedidaRepository.save(tm11);

        } catch (Exception e) {
            logger.error("Error al Crear InitBackend - Tipos Medidas: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void initBackDemo() {
        logger.info("--> initBackDemo");
        try{
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT5, null);
            //Eliminar Datos Demo
            boolean ejecutar = deleteDemo(parche);
            //Crear Datos Demo
            createDemo(ejecutar);
            logger.info("<-- initBackDemo");
        }catch(Exception e){
            logger.error("Error al Crear InitBackDemo: "+e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void createDemo(boolean ejecutar) {
        logger.info("--> createDemo");
        Usuario usuarioDemo;
        Parche parche;
        if (ejecutar) {

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
            empresa.setActivo(true);
            empresa.setUsuarioNombre("demo");
            empresa.setNombreEmpresa("Demo S.A.S");
            empresa.setEliminado(false);
            empresa.setTipoEmpresa(TipoEmpresaEnum.DEMO);
            empresa.setDireccion("Wakanda");
            empresa.setIdentificacion("800900700600");

            Response responseEmpresa = empresaService.saveEmpresa(empresa);

            if(responseEmpresa.isSuccess()){
                empresa = (EmpresaDTO) responseEmpresa.getObject();
                logger.info("Empresa DEMO Creada");
                usuarioDemo = usuarioService.findByName("demo");
                //Crear Productos
                //Consultar Empresa byId
                Empresa emp = empresaRepository.findById(empresa.getEmpresaId()).orElse(null);
                //Consultar Impuestos
                Pageable pageable = PageRequest.of(0, 5);
                List<Impuesto> impuestos = impuestoRepository.findByEmpresaAndEliminadoIsFalse(emp, pageable);

                //Consultar Categorias
                CategoriaProducto categoria = categoriaProductoService.findByNombreOrDescripcion("Normal", emp, 0, 10).get(0);
                //Tipos de Medidas
                List<TipoMedida> tipos =  tipoService.findAllTiposMedidas(emp);
                if(emp != null) {
                    for(int i = 0; i < 200 ; i++) {
                        int item = i + 1;
                        Producto producto = new Producto();
                        producto.setActivo(true);
                        producto.setCategoria(categoria);
                        producto.setCodigoBarra(StringUtils.leftPad((item + 1) + "", 10, "0"));
                        producto.setDescripcion("Producto Demo " + (item + 1));
                        producto.setEmpresa(emp);
                        Double aleatorio = Math.floor(Math.random()*5);
                        producto.setImpuesto(impuestos.get(aleatorio.intValue()));
                        producto.setNombreProducto("Producto # " + (item + 1));
                        aleatorio = Math.floor(Math.random()*11);
                        producto.setTipoMedida(tipos.get(aleatorio.intValue()));
                        producto.setControlarStock(item % 4 == 0);
                        producto.setPrecioVenta(BigDecimal.valueOf((item * 100 * 2) * 1.20 ));
                        producto.setPrecioCosto(BigDecimal.valueOf(item * 100 * 2));
                        producto.setCantidadInicial(BigDecimal.valueOf((item + 1) * 2));

                        productoService.saveProduct(producto, usuarioDemo);

                    }

                    Date fechaEjecucion = java.sql.Date.valueOf(LocalDate.now());
                    parche = new Parche(Constantes.INIT5,fechaEjecucion,true, null);
                    logger.info("Parche Demo Creado");
                    parcheRepository.save(parche);

                } else {
                    logger.info("Error al Buscar Empresa");
                }
            } else {
                logger.info("Error al crear Empresa DEMO");
            }

        }
        logger.info("<-- createDemo");
    }

    private boolean deleteDemo(Parche parche) {
        logger.info("--> deleteDemo");
        boolean ejecutar;
        if(parche != null) {
            logger.info("Fecha Ejecucion Parche: " + parche.getFechaEjecucion());
            LocalDate fechaParche;
            fechaParche = parche.getFechaEjecucion().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            logger.info("Fecha Ejecucion Parche: " + fechaParche);
            LocalDate fecha5DaysBack = LocalDate.now().minusDays(5);
            logger.info("Fecha 5 Days Back: " + fecha5DaysBack);
            if (fechaParche.isBefore(fecha5DaysBack)) {
                ejecutar = true;
                //Eliminar Informacion Anterior
                Empresa emp = empresaService.findEmpresaDemo();
                //usuarioDemo = usuarioService.findByName("demo");
                //Productos
                List<Producto> productosList = productoService.findAllByEmpresa(emp);
                //Ventas
                if (ventaService.deleteAllVentas(emp, productosList)) {
                    //Compras
                    if (compraService.deleteAllCompras(emp, productosList)) {
                        //Inventarios
                        if (inventarioService.deleteAllInventarios(emp, productosList)) {
                            //Tipos
                            if (tipoService.deleteAllTipos(emp)) {
                                //Movimientos
                                if (movimientoService.deleteAllByProductos(productosList)) {
                                    //Producto Valor Venta
                                    if (productoValorVentaService.deleteAllByProductos(productosList)) {
                                        //productos
                                        if (productoService.deleteAllByEmpresa(emp)) {
                                            //Categorias
                                            if (categoriaProductoService.deleteAllByEmpresa(emp)) {
                                                //Impuestos
                                                if (impuestoService.deleteAllByEmpresa(emp)) {
                                                    //Clientes
                                                    if (clienteService.deleteAllByEmpresa(emp)) {
                                                        //Proveedores
                                                        if (proveedorService.deleteAllByEmpresa(emp)) {
                                                            //Usuario
                                                            if (usuarioService.deleteAllUsuariosByEmpresa(emp)) {
                                                                //Roles
                                                                if (rolService.deleteAllByEmpresa(emp)) {
                                                                    //empresa
                                                                    if (!empresaService.deleteAllEmpresa(emp)) {
                                                                        ejecutar = false;
                                                                    }
                                                                } else {
                                                                    ejecutar = false;
                                                                }
                                                            } else {
                                                                ejecutar = false;
                                                            }
                                                        } else {
                                                            ejecutar = false;
                                                        }
                                                    } else {
                                                        ejecutar = false;
                                                    }
                                                } else {
                                                    ejecutar = false;
                                                }
                                            } else {
                                                ejecutar = false;
                                            }
                                        } else {
                                            ejecutar = false;
                                        }
                                    } else {
                                        ejecutar = false;
                                    }
                                } else {
                                    ejecutar = false;
                                }
                            } else {
                                ejecutar = false;
                            }
                        } else {
                            ejecutar = false;
                        }
                    } else {
                        ejecutar = false;
                    }
                } else {
                    ejecutar = false;
                }
            } else {
                ejecutar = false;
            }
        } else {
            ejecutar = true;
        }
        logger.info("<-- deleteDemo");
        return ejecutar;
    }
}
