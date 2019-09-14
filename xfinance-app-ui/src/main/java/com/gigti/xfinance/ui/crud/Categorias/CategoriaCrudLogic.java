package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class CategoriaCrudLogic implements Serializable {

    private CategoriaView view;
    private IcategoriaProductoService icategoriaProductoService;

    public CategoriaCrudLogic(@Autowired IcategoriaProductoService iservice, CategoriaView simpleCrudView) {
    //public CategoriaCrudLogic(CategoriaView simpleCrudView) {
        this.icategoriaProductoService = iservice;
        view = simpleCrudView;
    }

    public void init() {
        editCategoria(null);
        // Hide and disable if not admin
        //TODO PERMISOS
//        if (!AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            view.setNewProductEnabled(false);
//        }
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String categoriaId) {
        String fragmentParameter;
        if (categoriaId == null || categoriaId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = categoriaId;
        }

        //UI.getCurrent().navigate(CategoriaView.class, fragmentParameter);
    }

    public void enter(String categoriaId) {
        if (categoriaId != null && !categoriaId.isEmpty()) {
            if (categoriaId.equals("new")) {
                newCategoria();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    CategoriaProducto categoria = findCategoria(categoriaId);
                    view.selectRow(categoria);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private CategoriaProducto findCategoria(String productId) {
        return icategoriaProductoService.findById(productId);
    }

    public void saveCategoria(CategoriaProducto categoria) {
        view.clearSelection();
        view.updateCategoria(categoria);
        setFragmentParameter("");
        view.showSaveNotification(categoria.getNombre() + (StringUtils.isBlank(categoria.getId()) ? " created" : " updated"));
        //TODO
        //view.showSaveNotification("" + (categoria.getId().isBlank() ? " created" : " updated"));
    }

    public void deleteCategoria(CategoriaProducto categoria) {
//        view.clearSelection();
//        view.removeProduct(product);
//        setFragmentParameter("");
//        view.showSaveNotification(product.getProductName() + " removed");
    }

    public void editCategoria(CategoriaProducto categoria) {
        if (categoria == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(categoria.getId() + "");
        }
        view.editCategoria(categoria);
    }

    public void newCategoria() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editCategoria(new CategoriaProducto());
    }

    public void rowSelected(CategoriaProducto categoria) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editCategoria(categoria);
        }
    }

}
