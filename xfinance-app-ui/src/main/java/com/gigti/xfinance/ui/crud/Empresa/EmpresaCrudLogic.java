package com.gigti.xfinance.ui.crud.Empresa;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.IEmpresaService;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudLogic;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EmpresaCrudLogic implements Serializable, ICrudLogic {

    private EmpresaView view;
    private IEmpresaService iEmpresaService;
    private static Empresa empresa;
    private String filterText = "";

    public EmpresaCrudLogic(IEmpresaService iService, EmpresaView simpleCrudView) {
        this.iEmpresaService = iService;
        view = simpleCrudView;
        empresa  = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
    }

    public void init() {
        editar(null);
    }

    public boolean acceder(){
        // Hide and disable if not admin
        return empresa != null;
        //TODO permisos
//        if (!AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            view.setNewProductEnabled(false);
//        }
        //return true;
    }

    public void cancelar() {
        setFragmentParameter("");
        view.clearSelection();
        view.showForm(false);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String id) {
        String fragmentParameter;
        if (id == null || id.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = id;
        }
    }

    public void enter(String id) {
        if (id != null && !id.isEmpty()) {
            if (id.equals("new")) {
                nuevo();
            } else {
                Empresa empresa = find(id);
                view.selectRow(empresa);
            }
        } else {
            view.showForm(false);
        }
    }

    public Empresa find(String id) {
        return iEmpresaService.findById(id);
    }

    @Override
    public void guardar(Object object) {
        Empresa empresa = (Empresa) object;
        String typeOperation = StringUtils.isBlank(empresa.getId()) ? " Creada" : " Actualizada";
        empresa = iEmpresaService.saveEmpresa(empresa);
        if(empresa != null){
            view.refresh(empresa);
            view.clearSelection();
            view.showSaveNotification(empresa.getNombreEmpresa() +" "+typeOperation);
            //view.refresh();
            view.showForm(false);
            setFragmentParameter("");
        } else {
            view.showError("Error al Guardar Empresa "+empresa.getNombreEmpresa());
        }
    }

    public void eliminar(Object object) {
        Empresa empresa = (Empresa) object;
        view.clearSelection();
        if(iEmpresaService.deleteEmpresa(empresa.getId())){
            view.showSaveNotification("Empresa: "+empresa.getNombreEmpresa() + " Eliminada");
            if(view.getItemsGrid().remove(empresa)){
                setFragmentParameter("");
                view.getGrid().setItems(view.getItemsGrid());
            } else{
                view.showError("Error al Eliminar Empresa "+empresa.getNombreEmpresa()+ " De la tabla");
                view.refresh();
            }
        } else {
            view.showError("Error al Eliminar Empresa "+empresa.getNombreEmpresa());
        }
    }

    public void editar(Object object) {
        Empresa empresa = (Empresa) object;
        if (empresa == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(empresa.getId() + "");
        }
        view.edit(empresa);
    }

    public void nuevo() {
        view.clearSelection();
        setFragmentParameter("new");
        view.edit(new Empresa());
    }

    public void rowSelected(Object object) {
        Empresa empresa = (Empresa) object;
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editar(empresa);
        }
    }

    public List<Empresa> findAll() {
        //TODO aplicar al Filtro si es Activo O INACTIVO
        return iEmpresaService.findAll(view.getGrid().getPage(), view.getGrid().getPageSize());
    }

    public List<Empresa> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            view.refresh();
            return null;
        }
        this.filterText = filterText.trim();
        return iEmpresaService.findByNombreOrDescripcion(filterText, view.getGrid().getPage(), view.getGrid().getPageSize());
    }
}
