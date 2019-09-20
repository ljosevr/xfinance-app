/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaView;
import com.gigti.xfinance.ui.crud.inventario.InventarioCrudView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class ProductoCrudLogic implements Serializable {

    private ProductoCrudView view;
    private IProductoService iProductoService;
    private IcategoriaProductoService iCategoriaService;

    public ProductoCrudLogic(@Autowired IProductoService iServiceProd, @Autowired IcategoriaProductoService iServiceCat, ProductoCrudView simpleCrudView) {
        view = simpleCrudView;
        this.iProductoService = iServiceProd;
        this.iCategoriaService = iServiceCat;
    }

    public void init() {
        editProducto(null);
        // Hide and disable if not admin
        //TODO permisos
//        if (!AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            view.setNewProductEnabled(false);
//        }
    }

    public void cancelProducto() {
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
                try {
                    Producto producto = findProducto(productId);
                    view.selectRow(producto);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Producto findProducto(String productId) {
        return iProductoService.findById(productId);
    }

    public void saveProducto(Producto producto) {
        boolean result = view.saveProducto(producto);
        if(result){
            String typOperation = StringUtils.isBlank(producto.getId()) ? " Creada" : " Actualizada";
            view.clearSelection();
            view.showSaveNotification(producto.getNombreProducto() +" "+typOperation);
            view.refresh();
            view.showForm(false);
            setFragmentParameter("");
        } else {
            Notification.show("Error al Guardar Producto "+producto.getNombreProducto());
        }
    }

    public void deleteProducto(Producto producto) {
        view.clearSelection();
        boolean result = view.deleteProducto(producto);

        if(result){
            view.showSaveNotification(producto.getNombreProducto() + " Eliminado");
            view.refresh();
            setFragmentParameter("");
        } else {
            Notification.show("Error al Eliminar Producto "+producto.getNombreProducto());
        }
    }

    public void editProducto(Producto producto) {
        if (producto == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(producto.getId() + "");
        }
        view.editProducto(producto);
    }

    public void newProducto() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProducto(new Producto());
    }

    public void rowSelected(Producto producto) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editProducto(producto);
        }
    }
}
