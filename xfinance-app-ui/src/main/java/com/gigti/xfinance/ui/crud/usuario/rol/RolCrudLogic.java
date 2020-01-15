package com.gigti.xfinance.ui.crud.usuario.rol;

import com.gigti.xfinance.backend.TipoMedidaEnum;
import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.producto.ProductoCrudView;
import com.gigti.xfinance.ui.util.ICrudLogic;
import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RolCrudLogic { //implements Serializable, ICrudLogic {
//
//    private RolView view;
//    private UsuarioService usuarioService;
//    private static Empresa empresa;
//    private String filterText = "";
//
//    public RolCrudLogic(UsuarioService usuarioService, RolView simpleCrudView) {
//        this.usuarioService = usuarioService;
//        view = simpleCrudView;
//        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
//    }
//
//    public void init() {
//        editar(null);
//    }
//
//    public boolean acceder(){
//        return empresa != null;
//    }
//
//    public void cancelar() {
//        view.clearSelection();
//        view.showForm(false);
//    }
//
//    public void enter(String rolId) {
//        if (rolId != null && !rolId.isEmpty()) {
//            if (rolId.equals("new")) {
//                nuevo();
//            } else {
//                Rol rol = find(rolId);
//                view.selectRow(rol);
//            }
//        } else {
//            view.showForm(false);
//        }
//    }
//
//    @Override
//    public Rol find(String id) {
//        return usuarioService.findRolById(id, empresa, true);
//    }
//
//    @Override
//    public void guardar(Object object) {
//        Rol rol = (Rol) object;
//        String typOperation = StringUtils.isBlank(rol.getId()) ? " Creado" : " Actualizado";
//        rol.setEmpresa(empresa);
//        rol = usuarioService.saveRol(rol);
//        if(rol != null){
//            view.refresh(rol);
//            view.clearSelection();
//            view.showSaveNotification(rol.getNombre() +" "+typOperation);
//            //view.refresh();
//            view.showForm(false);
//            setFragmentParameter("");
//        } else {
//            view.showError("Error al Guardar Categoria "+rol.getNombre());
//        }
//    }
//
//    @Override
//    public void eliminar(Object object) {
//
//    }
//
//    @Override
//    public void editar(Object object) {
//
//    }
//
//    private Producto findProducto(String productId) {
//        return productoService.findById(productId);
//    }
//
//    void saveProducto(Producto producto) {
//        String typOperation = StringUtils.isBlank(producto.getId()) ? " Creado" : " Actualizada";
//        producto.setEmpresa(empresa);
//        producto = productoService.saveProduct(producto, CurrentUser.get());
//        if (producto != null) {
//            view.refresh(producto);
//            view.clearSelection();
//            view.showSaveNotification(producto.getNombreProducto() + " " + typOperation);
//            view.showForm(false);
//            setFragmentParameter("");
//        } else {
//            Notification.show("Error al Guardar Producto " + producto.getNombreProducto());
//        }
//    }
//
//    void deleteProducto(Producto producto) {
//        view.clearSelection();
//        if (productoService.delete(producto.getId())) {
//            view.refresh();
//            view.showSaveNotification("Producto: " + producto.getNombreProducto() + " Eliminado");
//            List<Producto> lista = (List<Producto>) view.getGrid().getDataProvider();
//            if (lista.remove(producto)) {
//                setFragmentParameter("");
//            } else {
//                view.showError("Error al Eliminar Producto " + producto.getNombreProducto() + " De la tabla");
//                view.refresh();
//            }
//        } else {
//            view.showError("Error al Eliminar Producto " + producto.getNombreProducto());
//        }
//    }
//
//    void edit(Producto producto) {
//        if (producto == null) {
//            setFragmentParameter("");
//        } else {
//            setFragmentParameter(producto.getId() + "");
//        }
//        view.editProducto(producto);
//    }
//
//
//    void nuevo() {
//        view.clearSelection();
//        setFragmentParameter("new");
//        view.editProducto(new Producto());
//    }
//
//    @Override
//    public void rowSelected(Object object) {
//
//    }
//
//    void rowSelected(Producto producto) {
//        if (AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            editProducto(producto);
//        }
//    }
//
//    public List<Producto> findAll() {
//        return productoService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
//    }
//
//    public List<CategoriaProducto> findAllCategoria() {
//        //TODO aplicar al Filtro si es Activo O INACTIVO
//        return categoriaProductoService.findActivoOrInactivo(true, empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
//    }
//
//    public List<Producto> setFilter(String filterText) {
//        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
//        if (Objects.equals(this.filterText, filterText.trim())) {
//            view.refresh();
//            return null;
//        }
//        this.filterText = filterText.trim();
//        return productoService.findByNombreProducto(empresa, filterText);
//    }
//
//    public List<TipoMedidaEnum> getAllTipoMedidaEnum() {
//        return productoService.getAllTipoMedidaEnum();
//    }
//}

}
