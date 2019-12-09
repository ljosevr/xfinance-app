package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;

@Service
public class IinitBackServiceImpl implements IinitBackService {

    Logger logger = LoggerFactory.getLogger(IinitBackServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TipoIdeRepository tipoIdeRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ParcheRepository parcheRepository;
    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;
    @Autowired
    private VistaRepository vistaRepository;
    //private PasswordEncoder passwordEncoder;


    // Metodo para crear Init del Backend
    // Tipos de Datos
    @Transactional
    @Override
    public void initBackTipos() {
        logger.debug("--> initBackTipos");
        try {
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT1, null);
            if (parche == null) {
                //Tipo Ide
                tipoIdeRepository.save(TipoIde.CEDULA);
                tipoIdeRepository.save(TipoIde.EXTRANJERIA);
                tipoIdeRepository.save(TipoIde.NIT);
                tipoIdeRepository.save(TipoIde.TIDENTIDAD);
                logger.debug("--> Tipos IDE");

                //Tipo Usuarios
                tipoUsuarioRepository.save(TipoUsuario.ADMIN);
                tipoUsuarioRepository.save(TipoUsuario.NORMAL);
                tipoUsuarioRepository.save(TipoUsuario.ROOT);
                tipoUsuarioRepository.save(TipoUsuario.SELLER);
                logger.debug("--> Tipos Usu");
                //Tipo Empresa
                tipoEmpresaRepository.save(TipoEmpresa.ROOT);
                tipoEmpresaRepository.save(TipoEmpresa.NORMAL);
                logger.debug("--> Tipos EMP");

                //Vistas
                Vista vista_venta = vistaRepository.save(new Vista(Constantes.VIEW_PVENTA,null,null, 1, null));//CASH
                Vista vista_producto = vistaRepository.save(new Vista(Constantes.VIEW_PRODUCTO ,null, null, 2, null));
                Vista vista_usuarios = vistaRepository.save(new Vista(Constantes.VIEW_USUARIO,null,null,3,null));
                //PERFIL
                Vista vista_empresa = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESA,Constantes.VIEW_R_EMPRESA, null, 4, "OFFICE"));

                Vista vista_empresaMaster = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESAS,null, null, 5, null));
                Vista vista_reportes = vistaRepository.save(new Vista(Constantes.VIEW_REPORTS,null, null, 6, null));

                //1.Punto de Venta
                Vista vista_vender = vistaRepository.save(new Vista(Constantes.VIEW_REGISTRAR,Constantes.VIEW_R_VENTA, vista_venta, 11, "CASH"));//CASH

                //2. Productos
                Vista vista_admin_prod = vistaRepository.save(new Vista(Constantes.VIEW_PRODUCTO_ADMIN,Constantes.VIEW_R_PRODUCTO, vista_producto, 21,"CUBE"));//COMPILE
                Vista vista_categoria = vistaRepository.save(new Vista(Constantes.VIEW_CATEGORIA,Constantes.VIEW_R_CATEGORIA, vista_producto, 22,"CUBES"));
                Vista vista_compras = vistaRepository.save(new Vista(Constantes.VIEW_COMPRAS,Constantes.VIEW_R_COMPRAS,vista_producto, 23, "CART_O"));
                Vista vista_invInicial = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO_INICIAL,Constantes.VIEW_R_INVENTARIO_INICIAL,vista_producto,24, "STOCK"));

                //3.Usuarios
                Vista vista_admin_usu = vistaRepository.save(new Vista(Constantes.VIEW_USUARIO_ADMIN,Constantes.VIEW_R_USUARIOS,vista_usuarios,31, "GROUP"));
                Vista vista_rol = vistaRepository.save(new Vista(Constantes.VIEW_ROL,Constantes.VIEW_R_ROL,vista_usuarios, 32,"DOCTOR"));

                //5.Empresa Master
                Vista vista_admin_empMaster = vistaRepository.save(new Vista(Constantes.VIEW_EMPRESA_ADMIN,Constantes.VIEW_R_EMPRESA_MASTER, vista_empresaMaster, 51, "OFFICE"));
                Vista vista_usuario_emp = vistaRepository.save(new Vista(Constantes.VIEW_EMP_USU_ADMIN,Constantes.VIEW_R_USUARIOADMIN,vista_empresaMaster, 52, "GROUP"));

                //6.Reportes
                Vista vista_invHoy = vistaRepository.save(new Vista(Constantes.VIEW_INVENTARIO,Constantes.VIEW_R_INVENTARIO,vista_reportes, 61, "STORAGE"));
                Vista vista_balance = vistaRepository.save(new Vista(Constantes.VIEW_GANANCIAS_Y_PERDIDAS,Constantes.VIEW_R_GANANCIAS_Y_PERDIDAS,vista_reportes, 62,"CHART_LINE"));

                logger.debug("--> Vistas");

                //Roles
                //1. ROOT
                Rol.ROOT.setVistas(new HashSet<>());
                Rol.ROOT.getVistas().add(vista_empresaMaster);
                Rol.ROOT.getVistas().add(vista_admin_empMaster);
                Rol.ROOT.getVistas().add(vista_usuario_emp);
                rolRepository.save(Rol.ROOT);

                logger.debug("--> Rol Root");

                //2. ADMIN
                Rol.ADMIN.setVistas(new HashSet<>());
                Rol.ADMIN.getVistas().add(vista_venta);
                Rol.ADMIN.getVistas().add(vista_vender);
                Rol.ADMIN.getVistas().add(vista_producto);
                Rol.ADMIN.getVistas().add(vista_admin_prod);
                Rol.ADMIN.getVistas().add(vista_categoria);
                Rol.ADMIN.getVistas().add(vista_compras);
                Rol.ADMIN.getVistas().add(vista_invInicial);
                Rol.ADMIN.getVistas().add(vista_usuarios);
                Rol.ADMIN.getVistas().add(vista_admin_usu);
                Rol.ADMIN.getVistas().add(vista_rol);
                Rol.ADMIN.getVistas().add(vista_reportes);
                Rol.ADMIN.getVistas().add(vista_invHoy);
                Rol.ADMIN.getVistas().add(vista_balance);
                rolRepository.save(Rol.ADMIN);
                logger.debug("--> Rol Admin");

                //3.VENDEDOR
                Rol.VENDEDOR.setVistas(new HashSet<>());
                Rol.VENDEDOR.getVistas().add(vista_venta);
                Rol.VENDEDOR.getVistas().add(vista_vender);
                rolRepository.save(Rol.VENDEDOR);
                logger.debug("--> Rol Vendedor");

                //4.AUXILIAR
                Rol.AUXILIAR.setVistas(new HashSet<>());
                Rol.AUXILIAR.getVistas().add(vista_producto);
                Rol.AUXILIAR.getVistas().add(vista_admin_prod);
                Rol.AUXILIAR.getVistas().add(vista_categoria);
                Rol.AUXILIAR.getVistas().add(vista_compras);
                rolRepository.save(Rol.AUXILIAR);
                logger.debug("--> Rol Auxiliar");

                parche = new Parche(Constantes.INIT1,java.sql.Date.valueOf(LocalDate.now()),true, null);
                parcheRepository.save(parche);
                logger.debug("--> Parche creado");

                logger.debug("<-- initBackTipos");
            }
        } catch (Exception e) {
            logger.error("Error al Crear InitBackend - Tipos: " + e.getMessage(), e);
        }
    }
    //Usuarios Root y Admin
    // Dummys de Pruebas ETC.
    @Transactional
    @Override
    public void initBackObjetos() {
        logger.debug("--> initBackObjetos");
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
                        TipoEmpresa.ROOT, 0L);

                emp = empresaRepository.save(emp);
                logger.debug("--> Empresa Root Creado");

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
                logger.debug("--> Persona Root Creado");

                //Usuario Root
                Rol rolRoot = rolRepository.findByNombreAndEmpresa(Rol.ROOT.getNombre(), null);
                Usuario userRoot = new Usuario(
                        "Root",
                        //passwordEncoder.encode("1234"),
                        "1234",
                        true,
                        persona,
                        emp,
                        rolRoot,
                        TipoUsuario.ROOT
                );

                usuarioRepository.save(userRoot);
                logger.debug("--> Usuario Root Creado");

                parche = new Parche(Constantes.INIT2,java.sql.Date.valueOf(LocalDate.now()),true, null);
                logger.debug("--> Parche Creado");
                parcheRepository.save(parche);
            }
            logger.debug("<-- initBackObjetos");
        }catch(Exception e){
            logger.error("Error al Crear InitBackend - Objetos: "+e.getMessage(), e);
        }
    }
}
