package com.gigti.xfinance.ui.crud.empresa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.dto.EmpresaDTO;
import com.gigti.xfinance.backend.services.EmpresaService;
import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.ICrudLogic;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EmpresaMasterCrudLogic implements Serializable, ICrudLogic {

    private EmpresaMasterView view;
    private EmpresaService empresaService;
    private static Empresa empresa;
    private String filterText = "";

    public EmpresaMasterCrudLogic(EmpresaService iService, EmpresaMasterView simpleCrudView) {
        this.empresaService = iService;
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
                EmpresaDTO empresa = find(id);
                view.selectRow(empresa);
            }
        } else {
            view.showForm(false);
        }
    }

    public EmpresaDTO find(String id) {
        return empresaService.findById(id);
    }

    @Override
    public void guardar(Object object) {
        EmpresaDTO empresa = (EmpresaDTO) object;
        String typeOperation = StringUtils.isBlank(empresa.getEmpresaId()) ? " Creada" : " Actualizada";
        empresa = empresaService.saveEmpresa(empresa);
        if(empresa != null){
            view.refresh(empresa);
            view.clearSelection();
            view.showSaveNotification(empresa.getNombreEmpresa() +" "+typeOperation);
            view.showForm(false);
            setFragmentParameter("");
        } else {
            view.showError("Error al Guardar Empresa "+empresa.getNombreEmpresa());
        }
    }

    public void eliminar(Object object) {
        EmpresaDTO empresa = (EmpresaDTO) object;
        view.clearSelection();
        if(empresaService.deleteEmpresa(empresa.getEmpresaId())){
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
        EmpresaDTO empresa = (EmpresaDTO) object;
        if (empresa == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(empresa.getEmpresaId() + "");
        }
        view.edit(empresa);
    }

    public void nuevo() {
        view.clearSelection();
        setFragmentParameter("new");
        view.edit(new EmpresaDTO());
    }

    public void rowSelected(Object object) {
        EmpresaDTO empresa = (EmpresaDTO) object;
        if (AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(CurrentUser.get())) {
            editar(empresa);
        }
    }

    public List<EmpresaDTO> findAll() {
        //TODO aplicar al Filtro si es Activo O INACTIVO
        return empresaService.findAll(view.getGrid().getPage(), view.getGrid().getPageSize());
    }

    public List<EmpresaDTO> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            view.refresh();
            return null;
        }
        this.filterText = filterText.trim();
        return empresaService.findByNombreOrDescripcion(filterText, view.getGrid().getPage(), view.getGrid().getPageSize());
    }
}
