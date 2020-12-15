package com.gigti.xfinance.backend.others;

public class Constantes {

    //PARCHES
    public static final String INIT1 = "PI Back - 1 - Tipos";
    public static final String INIT2 = "PI Back - 2 - Objetos";
    public static final String INIT3 = "PI Back - 3 - TipoEmpresa";
    public static final String INIT4 = "PI Back - 4 - Roles";
    public static final String INIT5 = "PI Back - 5 - DEMO";
    public static final String INIT_MENU_GASTO = "PI Back - 6 - GASTO";

    //VIEWS-CLASS-NAME
    public static final String VIEW_C_ROOT = "";
    public static final String VIEW_C_MAIN                  = "com.gigti.xfinance.ui.MainLayout";
    public static final String VIEW_C_VENTA                 = "com.gigti.xfinance.ui.crud.pventa.PventaView";
    public static final String VIEW_C_PRODUCTO              = "com.gigti.xfinance.ui.crud.producto.ProductoView";
    public static final String VIEW_C_CATEGORIA             = "com.gigti.xfinance.ui.crud.categoria.CategoriaView";
    public static final String VIEW_C_COMPRAS               = "com.gigti.xfinance.ui.crud.compra.CompraView";
    public static final String VIEW_C_INVENTARIO_INICIAL    = "com.gigti.xfinance.ui.crud.inventarios.inicial.InvInicialView";
    public static final String VIEW_C_INVENTARIO_ACTUAL     = "com.gigti.xfinance.ui.crud.inventarios.actual.InvActualView";
    public static final String VIEW_C_USUARIOS              = "com.gigti.xfinance.ui.crud.usuario.UsuarioView";
    public static final String VIEW_C_ROL                   = "com.gigti.xfinance.ui.crud.usuario.rol.RolView";
    public static final String VIEW_C_VENTAS                = "com.gigti.xfinance.ui.crud.reportes.ventas.VentasView";
    public static final String VIEW_C_GANANCIAS_Y_PERDIDAS  = "com.gigti.xfinance.ui.crud.reportes.gyp.GanancisYPerdidasView";
    public static final String VIEW_C_EMPRESA_MASTER        = "com.gigti.xfinance.ui.crud.empresa.EmpresaMasterView";
    public static final String VIEW_C_CONFIG_PASSWORD       = "com.gigti.xfinance.ui.crud.configuracion.UserChangePasswordView";
    public static final String VIEW_C_CONFIG_PERFIL         = "com.gigti.xfinance.ui.crud.configuracion.PerfilView";
    public static final String VIEW_C_CONFIG_EMPRESA        = "com.gigti.xfinance.ui.crud.configuracion.DatosEmpresaView";
    public static final String VIEW_C_ADMIN_CLIENTE         = "com.gigti.xfinance.ui.crud.cliente.ClienteView";
    public static final String VIEW_C_ADMIN_PROVEEDOR       = "com.gigti.xfinance.ui.crud.proveedor.ProveedorView";
    public static final String VIEW_C_GASTO                 = "com.gigti.xfinance.ui.crud.gasto.GastoView";

    //public static final String VIEW_C_USUARIOADMIN = "usuarioAdmin";

    //VIEWS-ROUTE
    public static final String VIEW_R_LOGIN = "Login";
    public static final String VIEW_R_VENTA = "pventa";

    public static final String VIEW_R_PRODUCTO = "productos";
    public static final String VIEW_R_CATEGORIA = "categorias";
    public static final String VIEW_R_COMPRAS = "compras";

    public static final String VIEW_R_INVENTARIO_INICIAL = "invInicial";
    public static final String VIEW_R_INVENTARIO_ACTUAL = "invActual";

    public static final String VIEW_R_USUARIOS = "usuarios";
    public static final String VIEW_R_EMPRESA_MASTER = "empresaMaster";
    public static final String VIEW_R_EMPRESA = "empresa";
    public static final String VIEW_R_USUARIOADMIN = "usuarioAdmin";


    public static final String VIEW_R_FORGOT = "forgotPassword";
    public static final String VIEW_R_INVENTARIO = "inventario";

    public static final String VIEW_R_ROL = "roles";
    public static final String VIEW_R_REPORTS = "reports";
    public static final String VIEW_R_GANANCIAS_Y_PERDIDAS = "balance";
    public static final String VIEW_R_VENTAS = "ventas";
    public static final String VIEW_R_ADMIN_CLIENTE = "clientes";
    public static final String VIEW_R_ADMIN_PROVEEDOR = "proveedores";

    public static final String VIEW_R_CONFIG_PERFIL = "perfil";
    public static final String VIEW_R_CONFIG_EMPRESA = "dataEmpresa";
    public static final String VIEW_R_CONFIG_PASSWORD = "changePass";
    public static final String VIEW_R_REGISTRO = "registro";
    public static final String VIEW_R_GASTO = "gasto";

    //VIEW-NAME
    public static final String VIEW_MAIN = "Tu Inventario Seguro";
    public static final String VIEW_LOGIN = "Login";
    public static final String VIEW_PVENTA = "Punto de Venta";
    public static final String VIEW_REGISTRAR = "Registrar Venta";
    public static final String VIEW_PRODUCTO = "Productos";
    public static final String VIEW_INVENTARIO = "Inventarios";
    public static final String VIEW_CATEGORIA = "Categorias Productos";
    public static final String VIEW_CLIENTES = "Clientes";
    public static final String VIEW_PROVEEDORES = "Proveedores";
    public static final String VIEW_ADMIN_CLIENTE = "Administrar Clientes";
    public static final String VIEW_ADMIN_PROVEEDOR = "Administrar Proveedores";

    public static final String VIEW_PRODUCTO_ADMIN = "Administrar Productos";
    public static final String VIEW_COMPRAS = "Administrar Compras";
    public static final String VIEW_INVENTARIO_INICIAL = "Inventario Inicial";
    public static final String VIEW_INVENTARIO_ACTUAL = "Inventario Actual";

    public static final String VIEW_USUARIO = "Usuarios";
    public static final String VIEW_USUARIO_ADMIN = "Administrar Usuarios";
    public static final String VIEW_ROL = "Roles";

    public static final String VIEW_EMPRESA = "Empresa";
    public static final String VIEW_EMPRESAS = "Empresas";
    public static final String VIEW_EMPRESA_ADMIN = "Administrar Empresas";
    //public static final String VIEW_EMP_USU_ADMIN = "Usuarios Admin Empresa";

    public static final String VIEW_REPORTS = "Reportes";

    public static final String VIEW_VENTAS = "Ventas";
    public static final String VIEW_GANANCIAS_Y_PERDIDAS = "Ganancias y Perdidas";
    public static final String VIEW_CONFIG_PERFIL = "Perfil";
    public static final String VIEW_CONFIG_EMPRESA = "Datos Empresa";
    public static final String VIEW_CONFIG_PASSWORD = "Cambiar Mi Clave";

    public static final String VIEW_CONFIG = "Configuración";

    public static final String VIEW_RECOVER_PASS = "Recuperar Password";

    public static final String VIEW_REGISTRO_EMPRESA = "Registrar Empresa";
    public static final String VIEW_GASTO = "Registrar Gasto";

    //Paginador
    public static final int PAGE_SIZE_10 = 10;
    public static final int PAGE_SIZE_20 = 20;
    public static final int PAGINATOR_SIZE = 3;

    //Subtitles
    public static final String CREATE_PRODUCT = "Crear Producto";
    public static final String EDIT_PRODUCT = "Editar Producto";
    public static final String DELETE_PRODUCTO = "Eliminar Producto";
    public static final String CREATE_CATEGORY = "Crear Categoría";
    public static final String EDIT_CATEGORY = "Editar Categoría";
    public static final String CREATE_BUY = "Registrar Compra";
    public static final String EDIT_BUY = "Editar Compra";
    public static final String CREATE_PROVEEDOR = "Crear Proveedor";
    public static final String EDIT_PROVEEDOR = "Editar Proveedor";
    public static final String EDIT_CLIENT = "Editar Cliente";
    public static final String CREATE_CLIENT = "Crear Cliente";
    public static final String EDIT_USER = "Editar Usuario";
    public static final String CREATE_USER = "Crear Usuario";
    public static final String DELETE_USER = "Eliminar Usuario";
    public static final String DELETE_CATEGORIA = "Eliminar Categoria";
    public static final String CREATE_INV_INICIAL = "Crear Inventario Inicial";
    public static final String EDIT_INV_INICIAL = "Editar Inventario Inicial";
    public static final String VIEW_VENTA = "Detalle Venta";
    public static final String CREATE_ROL = "Crear Rol";
    public static final String EDIT_ROL = "Editar Rol";
    public static final String DELETE_ROL = "Eliminar Rol";
    public static final String CREATE_GASTO = "Crear Gasto";
    public static final String EDIT_GASTO = "Editar Gasto";
    public static final String DELETE_GASTO = "Eliminar Gasto";

}
