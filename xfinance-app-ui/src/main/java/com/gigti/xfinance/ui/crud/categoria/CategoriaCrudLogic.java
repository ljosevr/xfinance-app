package com.gigti.xfinance.ui.crud.categoria;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudLogic;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CategoriaCrudLogic implements Serializable, ICrudLogic {

    private CategoriaView view;
    private CategoriaProductoService categoriaProductoService;
    private static Empresa empresa;
    private String filterText = "";

    public CategoriaCrudLogic(CategoriaProductoService iService, CategoriaView simpleCrudView) {
        categoriaProductoService = iService;
        view = simpleCrudView;
        empresa  = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public void init() {
        editar(null);
    }

    public boolean acceder(){
        return empresa != null;
    }

    public void cancelar() {
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
                nuevo();
            } else {
                CategoriaProducto categoria = find(categoriaId);
                view.selectRow(categoria);
            }
        } else {
            view.showForm(false);
        }
    }

    public CategoriaProducto find(String id) {
        return categoriaProductoService.findById(id);
    }

    public void guardar(Object object) {
        CategoriaProducto categoria = (CategoriaProducto) object;
        String typOperation = StringUtils.isBlank(categoria.getId()) ? " Creada" : " Actualizada";
        categoria.setEmpresa(empresa);
        categoria = categoriaProductoService.saveCategoria(categoria);
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

    public void eliminar(Object object) {
        CategoriaProducto categoria = (CategoriaProducto) object;
        view.clearSelection();
        if(categoriaProductoService.deleteCategoria(categoria.getId())){
            view.showSaveNotification("Categoria: "+categoria.getNombre() + " Eliminada");
            if(view.getItemsGrid().remove(categoria)){
                setFragmentParameter("");
                view.getGrid().setItems(view.getItemsGrid());
            } else{
                view.showError("Error al Eliminar Categoria "+categoria.getNombre()+ " De la tabla");
                view.refresh();
            }
        } else {
            view.showError("Error al Eliminar Categoria "+categoria.getNombre());
        }
    }

    public void editar(Object object) {
        CategoriaProducto categoria = (CategoriaProducto) object;
        if (categoria == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(categoria.getId() + "");
        }
        view.editCategoria(categoria);
    }

    public void nuevo() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editCategoria(new CategoriaProducto());
    }

    public void rowSelected(Object object) {
        CategoriaProducto categoria = (CategoriaProducto) object;
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editar(categoria);
        }
    }

    public List<CategoriaProducto> findAll() {
        //TODO aplicar al Filtro si es Activo O INACTIVO
        return categoriaProductoService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
    }

    public List<CategoriaProducto> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            view.refresh();
            return null;
        }
        this.filterText = filterText.trim();
        return categoriaProductoService.findByNombreOrDescripcion(filterText, empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
    }
}
