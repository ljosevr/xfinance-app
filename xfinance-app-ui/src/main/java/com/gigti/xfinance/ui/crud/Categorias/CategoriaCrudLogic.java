package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CategoriaCrudLogic implements Serializable {

    private CategoriaView view;
    private IcategoriaProductoService icategoriaProductoService;
    private static Empresa empresa;
    private String filterText = "";

    public CategoriaCrudLogic(IcategoriaProductoService iService, CategoriaView simpleCrudView) {
        icategoriaProductoService = iService;
        view = simpleCrudView;
        empresa  = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public void init() {
        editCategoria(null);
    }

    public boolean access(){
        // Hide and disable if not admin
        return empresa != null;
        //TODO permisos
//        if (!AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            view.setNewProductEnabled(false);
//        }
        //return true;
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
    }

    public void enter(String categoriaId) {
        if (categoriaId != null && !categoriaId.isEmpty()) {
            if (categoriaId.equals("new")) {
                newCategoria();
            } else {
                CategoriaProducto categoria = findCategoria(categoriaId);
                view.selectRow(categoria);
            }
        } else {
            view.showForm(false);
        }
    }

    private CategoriaProducto findCategoria(String categoriaId) {
        return icategoriaProductoService.findById(categoriaId);
    }

    public void saveCategoria(CategoriaProducto categoria) {

        String typOperation = StringUtils.isBlank(categoria.getId()) ? " Creada" : " Actualizada";
        categoria.setEmpresa(empresa);
        categoria = icategoriaProductoService.saveCategoria(categoria);
        if(categoria != null){
            view.refresh(categoria);
            view.clearSelection();
            view.showSaveNotification(categoria.getNombre() +" "+typOperation);
            //view.refresh();
            view.showForm(false);
            setFragmentParameter("");
        } else {
            view.showError("Error al Guardar Categoria "+categoria.getNombre());
        }
    }

    public void deleteCategoria(CategoriaProducto categoria) {
        view.clearSelection();
        if(icategoriaProductoService.deleteCategoria(categoria.getId())){
            view.showSaveNotification("Categoria: "+categoria.getNombre() + " Eliminada");
            List<CategoriaProducto> lista = (List<CategoriaProducto>) view.getGrid().getDataProvider();
            if(lista.remove(categoria)){
                setFragmentParameter("");
                //TODO mejorar
                view.getGrid().setItems(lista);
                //refresh(categoria);
            } else{
                view.showError("Error al Eliminar Categoria "+categoria.getNombre()+ " De la tabla");
                view.refresh();
            }
        } else {
            view.showError("Error al Eliminar Categoria "+categoria.getNombre());
        }
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

    public List<CategoriaProducto> findAll() {
        //TODO aplicar al Filtro si es Activo O INACTIVO
        return icategoriaProductoService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
    }

    public List<CategoriaProducto> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            view.refresh();
            return null;
        }
        this.filterText = filterText.trim();
        return icategoriaProductoService.findByNombreOrDescripcion(filterText, empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
    }
}
