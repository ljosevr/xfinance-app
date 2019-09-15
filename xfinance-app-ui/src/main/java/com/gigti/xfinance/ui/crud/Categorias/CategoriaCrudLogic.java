package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.notification.Notification;
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
        editarCategoria(null);
        // Hide and disable if not admin
        //TODO PERMISOS
//        if (!AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            view.setNewProductEnabled(false);
//        }
    }

    public void cancelCategoria() {
        setFragmentParameter("");
        view.clearSelection();
        view.showForm(false);
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
                nuevaCategoria();
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

    public void guardarCategoria(CategoriaProducto categoria) {
        boolean result = view.guardarCategoria(categoria);
        if(result){
            String typOperation = StringUtils.isBlank(categoria.getId()) ? " Creada" : " Actualizada";
            view.clearSelection();
            view.showSaveNotification(categoria.getNombre() +" "+typOperation);
            view.refresh();
            view.showForm(false);
            setFragmentParameter("");
        } else {
            Notification.show("Error al Guardar Categoria "+categoria.getNombre());
        }
    }

    public void eliminarCategoria(CategoriaProducto categoria) {
        view.clearSelection();
        boolean result = view.eliminarCategoria(categoria);

        if(result){
            view.showSaveNotification(categoria.getNombre() + " Eliminada");
            view.refresh();
            setFragmentParameter("");
        } else {
            Notification.show("Error al Eliminar Categoria "+categoria.getNombre());
        }
    }

    public void editarCategoria(CategoriaProducto categoria) {
        if (categoria == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(categoria.getId() + "");
        }
        view.editarCategoria(categoria);
    }

    public void nuevaCategoria() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editarCategoria(new CategoriaProducto());
    }

    public void rowSelected(CategoriaProducto categoria) {
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editarCategoria(categoria);
        }
    }

}
