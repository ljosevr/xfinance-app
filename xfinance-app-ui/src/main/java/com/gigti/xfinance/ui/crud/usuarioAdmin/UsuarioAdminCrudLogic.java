/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuarioAdmin;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.backend.services.IusuarioService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UsuarioAdminCrudLogic implements Serializable {

   /* private UsuarioAdminCrudView view;
    private IusuarioService iusuarioService;
    //private static Usuario usuario;
    private static Empresa empresa;
    private String filterText = "";

    public UsuarioAdminCrudLogic(IusuarioService iusuarioService, UsuarioAdminCrudView simpleCrudView) {
        this.iusuarioService = iusuarioService;
        view = simpleCrudView;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public void init() {
        editProducto(null);
    }

    public boolean access() {
        // Hide and disable if not admin
        return empresa != null;
    }

    void cancelUsuario() {
        setFragmentParameter("");
        view.clearSelection();
        view.showForm(false);
    }

    private void setFragmentParameter(String usuarioId) {
        String fragmentParameter;
        if (productId == null || productId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = productId;
        }
    }

    public void enter(String productId) {
        if (productId != null && !productId.isEmpty()) {
            if (productId.equals("new")) {
                newProducto();
            } else {
                Producto producto = findProducto(productId);
                view.selectRow(producto);
            }
        } else {
            view.showForm(false);
        }
    }

    private Producto findProducto(String productId) {
        return iProductoService.findById(productId);
    }

    void saveProducto(Producto producto) {
        String typOperation = StringUtils.isBlank(producto.getId()) ? " Creado" : " Actualizada";
        producto.setEmpresa(empresa);
        producto = iProductoService.saveProduct(producto, CurrentUser.get());
        if (producto != null) {
            view.refresh(producto);
            view.clearSelection();
            view.showSaveNotification(producto.getNombreProducto() + " " + typOperation);
            view.showForm(false);
            setFragmentParameter("");
        } else {
            Notification.show("Error al Guardar Producto " + producto.getNombreProducto());
        }
    }

    void deleteProducto(Producto producto) {
        view.clearSelection();
        if (iProductoService.delete(producto.getId())) {
            view.refresh();
            view.showSaveNotification("Producto: " + producto.getNombreProducto() + " Eliminado");
            List<Producto> lista = (List<Producto>) view.getGrid().getDataProvider();
            if(lista.remove(producto)){
                setFragmentParameter("");
            } else{
                view.showError("Error al Eliminar Producto "+producto.getNombreProducto()+ " De la tabla");
                view.refresh();
            }
        } else {
            view.showError("Error al Eliminar Producto " + producto.getNombreProducto());
        }
    }

    void editProducto(Producto producto) {
        if (producto == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(producto.getId() + "");
        }
        view.editProducto(producto);
    }


    void newProducto() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProducto(new Producto());
    }

    void rowSelected(Producto producto) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editProducto(producto);
        }
    }

    public List<Producto> findAll() {
        return iProductoService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
    }

    public List<CategoriaProducto> findAllCategoria() {
        //TODO aplicar al Filtro si es Activo O INACTIVO
        return icategoriaProductoService.findActivoOrInactivo(true, empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
    }

    public List<Producto> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            view.refresh();
            return null;
        }
        this.filterText = filterText.trim();
        return iProductoService.findByNombreProducto(empresa, filterText);
    }*/
}
