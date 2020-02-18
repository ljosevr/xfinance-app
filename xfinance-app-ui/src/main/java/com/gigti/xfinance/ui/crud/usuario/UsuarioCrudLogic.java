/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.usuario;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.util.ICrudLogic;

import java.io.Serializable;
import java.util.List;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class UsuarioCrudLogic implements Serializable, ICrudLogic {

    private UsuarioView view;
    private UsuarioService usuarioService;
    private static Empresa empresa;
    private String filterText = "";

    public UsuarioCrudLogic(UsuarioService usuarioService, UsuarioView simpleCrudView) {
        this.usuarioService = usuarioService;
        view = simpleCrudView;

    }

    @Override
    public void init() {

    }

    @Override
    public boolean acceder() {
        return false;
    }

    @Override
    public void cancelar() {

    }

    @Override
    public void enter(String id) {

    }

    @Override
    public Object find(String id) {
        return null;
    }

    @Override
    public void guardar(Object object) {

    }

    @Override
    public void eliminar(Object object) {

    }

    @Override
    public void editar(Object object) {

    }

    @Override
    public void nuevo() {

    }

    @Override
    public void rowSelected(Object object) {

    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List setFilter(String filterText) {
        return null;
    }

//    @Override
//    public void init() {
//        view.getFilter().focus();
//        //view.getForm().close();
//    }
//
//    @Override
//    public boolean acceder() {
//        return empresa != null;
//    }
//
//    @Override
//    public void cancelar() {
//        clearSelection();
//        showForm(false);
//    }
//
//    @Override
//    public void enter(String id) {
//        if (id != null && !id.isEmpty()) {
//            if (id.equals("new")) {
//                nuevo();
//            } else {
//                Usuario usuario = find(id);
//                view.selectRow(usuario);
//            }
//        } else {
//            showForm(false);
//        }
//    }
//
//    @Override
//    public Usuario find(String id) {
//        return usuarioService.findUsuarioById(id);
//    }
//
//    @Override
//    public void guardar(Object object) {
//        Usuario usuario = (Usuario) object;
//        String typOperation = StringUtils.isBlank(usuario.getId()) ? " Creado" : " Actualizado";
//        usuario.setEmpresa(empresa);
//        usuario = usuarioService.saveUsuario(usuario);
//        if(usuario != null){
//            refresh(usuario);
//            clearSelection();
//            view.showSaveNotification(usuario.getNombreUsuario() +" "+typOperation);
//            showForm(false);
//        } else {
//            view.showError("Error al Guardar Usuario: "+usuario.getNombreUsuario());
//        }
//    }
//
//    @Override
//    public void eliminar(Object object) {
//        Usuario usuario = (Usuario) object;
//        clearSelection();
//        if(usuarioService.deleteUsuario(usuario.getId())){
//            view.showSaveNotification("Usuario: "+usuario.getNombreUsuario() + " Eliminado");
//            if(view.getItemsGrid().remove(usuario)){
//                view.getGrid().setItems(view.getItemsGrid());
//            } else{
//                view.showError("Error al Eliminar Usuario "+usuario.getNombreUsuario()+ " De la tabla");
//                refresh();
//            }
//        } else {
//            view.showError("Error al Eliminar Usuario "+usuario.getNombreUsuario());
//        }
//    }
//
//    @Override
//    public void editar(Object object) {
//        view.getForm().editUsuario(object != null ? (Usuario) object : null);
//        showForm(true);
//    }
//
//    @Override
//    public void nuevo() {
//        clearSelection();
//        editar(null);
//    }
//
//    @Override
//    public void rowSelected(Object object) {
//        Usuario usuario = (Usuario) object;
//        if (AccessControlFactory.getInstance().createAccessControl()
//                .isUserInRole(CurrentUser.get())) {
//            editar(usuario);
//        }
//    }
//
//    @Override
//    public List<Usuario> findAll() {
//        //TODO aplicar al Filtro si es Activo O INACTIVO
//        return usuarioService.findAll(empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
//    }
//
//    public List<Rol> findAllRoles() {
//        //TODO aplicar al Filtro si es Activo O INACTIVO
//        return usuarioService.findAllRol(empresa, false);
//    }
//
//    @Override
//    public List<Usuario> setFilter(String filterText) {
//        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
//        if (Objects.equals(this.filterText, filterText.trim())) {
//            refresh();
//            return null;
//        }
//        this.filterText = filterText.trim();
//        return usuarioService.findByNombreUsuario(filterText, empresa, view.getGrid().getPage(), view.getGrid().getPageSize());
//    }
//
//    public void showForm(boolean show) {
//        if(show){
//            view.getForm().open();
//        }else{
//            view.getFilter().focus();
//            view.getForm().close();
//        }
//    }
//
//    public void clearSelection() {
//        view.getGrid().getSelectionModel().deselectAll();
//    }
//
//    public void refresh() {
//        //view.setListaUsuarios(findAll());
//        view.getGrid().setItems(findAll());
//    }
//
//    public void refresh(Usuario usuario) {
//        for(Iterator<Usuario> it = view.getListaUsuarios().iterator(); it.hasNext();){
//            Usuario p = it.next();
//            if(p.getId().equals(usuario.getId())) {
//                it.remove();
//                view.getListaUsuarios().remove(p);
//                break;
//            }
//        }
//        view.getListaUsuarios().add(usuario);
//        view.getGrid().setItems(view.getListaUsuarios());
//        view.getGrid().refresh(usuario);
//    }
}
