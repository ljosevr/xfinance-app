/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.crud.inventario.InventarioCrudView;
import com.vaadin.flow.component.UI;
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
    @Autowired
    private IProductoService iProductoService;

    public ProductoCrudLogic(ProductoCrudView simpleCrudView) {
        view = simpleCrudView;
    }

    public void init() {
        editProduct(null);
        // Hide and disable if not admin
        if (!AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            view.setNewProductEnabled(false);
        }
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
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

        UI.getCurrent().navigate(InventarioCrudView.class, fragmentParameter);
    }

    public void enter(String productId) {
        if (productId != null && !productId.isEmpty()) {
            if (productId.equals("new")) {
                newProduct();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    Producto producto = findProduct(productId);
                    view.selectRow(producto);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Producto findProduct(String productId) {
        return iProductoService.findById(productId).orElse(null);
    }

    public void     saveProduct(Producto producto) {
//        boolean newProduct = product.isNewProduct();
//        view.clearSelection();
//        view.updateProduct(product);
//        setFragmentParameter("");
//        view.showSaveNotification(product.getProductName()
//                + (newProduct ? " created" : " updated"));
    }

    public void deleteProduct(Producto producto) {
//        view.clearSelection();
//        view.removeProduct(product);
//        setFragmentParameter("");
//        view.showSaveNotification(product.getProductName() + " removed");
    }

    public void editProduct(Producto producto) {
//        if (product == null) {
//            setFragmentParameter("");
//        } else {
//            setFragmentParameter(product.getId() + "");
//        }
//        view.editProduct(product);
    }

    public void newProduct() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProduct(new Producto());
    }

    public void rowSelected(Producto producto) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editProduct(producto);
        }
    }
}
