package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Collection;

@Route(value = "categoriaUI",layout = MainLayout.class)
@RouteAlias(value = "categoriasUI",layout = MainLayout.class)
@RouteAlias(value = "cateUI",layout = MainLayout.class)
public class CategoriaView_CrudUI extends VerticalLayout {

    public static final String VIEW_NAME = "categoria";
    public static final String VIEW_TITLE = "Categorias Productos";
    //private GridCrud<CategoriaProducto> view = new GridCrud<>(CategoriaProducto.class, new HorizontalSplitCrudLayout());
    private GridCrud<CategoriaProducto> view = new GridCrud<>(CategoriaProducto.class);
    private IcategoriaProductoService icategoriaProductoService;


    public CategoriaView_CrudUI(@Autowired IcategoriaProductoService iService) {
        //if(CurrentUser.get() != null) {
            this.icategoriaProductoService = iService;
            view.getCrudFormFactory().setUseBeanValidation(true);
            configureCrud();
            listenersCrud();
            structureView();
//        }else{
//            AccessControlFactory.getInstance().createAccessControl().signOut();
        //}
    }

    private void structureView(){
        H3 title = new H3(VIEW_TITLE);
        title.setClassName("titleView");
        setSpacing(false);
        setMargin(true);
        //setSizeUndefined();
        add(title,view);
    }

    private void configureCrud(){

//        view.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD, "nombre", "descripcion");
//        view.getCrudFormFactory().setVisibleProperties(CrudOperation.READ, "nombre", "descripcion");
//        view.getCrudFormFactory().setVisibleProperties(CrudOperation.UPDATE, "nombre", "descripcion");
//        view.getCrudFormFactory().setVisibleProperties(CrudOperation.DELETE, "id", "nombre", "descripcion");

//        view.getGrid().setColumns("nombre", "descripcion");
//        view.getGrid().getColumnByKey("nombre").setHeader("Nombre");
//        view.getGrid().getColumnByKey("descripcion").setHeader("Descripción");

    }

    private void listenersCrud(){
        view.setCrudListener(new CrudListener<>() {
            @Override
            public Collection<CategoriaProducto> findAll() {
                return icategoriaProductoService.findAll(CurrentUser.get().getEmpresa());
            }
            @Override
            public CategoriaProducto add(CategoriaProducto categoria) {
                categoria = guardar(categoria);
                //view.refreshGrid();
                return categoria;
            }

            @Override
            public CategoriaProducto update(CategoriaProducto categoria) {
                categoria = guardar(categoria);
                //view.refreshGrid();
                return categoria;
            }

            @Override
            public void delete(CategoriaProducto categoria) {
                categoria.setEmpresa(CurrentUser.get().getEmpresa());
                boolean result = icategoriaProductoService.eliminarCategoria(categoria.getId());
                if(!result){
                    Notification.show("Categoria No pudo ser eliminada");
                }
                //view.refreshGrid();
            }
        });
    }

    private CategoriaProducto guardar(CategoriaProducto categoria){
        if(CurrentUser.get() != null){
            categoria.setEmpresa(CurrentUser.get().getEmpresa());
                return icategoriaProductoService.guardarCategoria(categoria);
        }else{
            Notification.show("Error en la sesión");
            return null;
        }
    }
}
