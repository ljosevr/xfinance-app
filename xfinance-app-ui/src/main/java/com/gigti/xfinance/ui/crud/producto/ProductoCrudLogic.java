/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class ProductoCrudLogic implements Serializable {

    private ProductoCrudView view;
    private IProductoService iProductoService;
    private IcategoriaProductoService icategoriaProductoService;
    private static Empresa empresa;
    private String filterText = "";

    public ProductoCrudLogic(IProductoService iProductoService, IcategoriaProductoService icategoriaService, ProductoCrudView simpleCrudView) {
        this.iProductoService = iProductoService;
        this.icategoriaProductoService = icategoriaService;
        view = simpleCrudView;
        empresa = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public void init() {
        editProducto(null);
    }

    public boolean access() {
        // Hide and disable if not admin
        return empresa != null;
        //TODO permisos
//        if (!AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            view.setNewProductEnabled(false);
//        }
        //return true;
    }

    void cancelProducto() {
        setFragmentParameter("");
        view.clearSelection();
        view.showForm(false);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String productId) {
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
            List<CategoriaProducto> lista = (List<CategoriaProducto>) view.getGrid().getDataProvider();
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
    }
}
