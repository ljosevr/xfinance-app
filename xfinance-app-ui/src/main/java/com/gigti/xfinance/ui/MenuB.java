package com.gigti.xfinance.ui;

import com.gigti.xfinance.ui.authentication.AccessControlFactory;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaView;
import com.gigti.xfinance.ui.crud.Empresa.EmpresaView;
import com.gigti.xfinance.ui.crud.producto.ProductoCrudView;
import com.gigti.xfinance.ui.crud.pventa.PventaView;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class MenuB extends MenuBar {

    private MenuItem m_ventas;
    private MenuItem m_productos;
    private MenuItem m_usuarios;
    private MenuItem m_empresas;
    private MenuItem m_salir;

    public MenuB(){
        setClassName("menu-bar");
        addThemeVariants(MenuBarVariant.LUMO_PRIMARY);

        m_ventas = addItem(new RouterLink("Ventas", PventaView.class));
        m_productos = addItem("Productos");
        m_usuarios = addItem("Usuarios");
        m_empresas = addItem("Empresas");
        m_salir = addItem("Salir", event -> signOut());


        SubMenu sm_productos = m_productos.getSubMenu();
        //sm_productos.addItem(new RouterLink("Administrar", ProductoCrudView.class));
        sm_productos.addItem(new RouterLink("Administrar", ProductoCrudView.class));//"Administrar", event -> getUI().get().navigate(ProductoCrudView.class));
        sm_productos.addItem("Compras",event -> getUI().get().navigate("adminProd"));
        sm_productos.addItem(new RouterLink("Categoria", CategoriaView.class));//event -> getUI().get().navigate(CategoriaView_CrudUI.class));
        sm_productos.addItem("Inventario Hoy",event -> getUI().get().navigate("adminProd"));
        sm_productos.addItem("Inv. Inicial",event -> getUI().get().navigate("adminProd"));

        SubMenu sm_usuarios = m_usuarios.getSubMenu();
        sm_usuarios.addItem("Administrar",event -> getUI().get().navigate("adminUserView"));
        sm_usuarios.addItem("Roles",event -> getUI().get().navigate("rolView"));
        sm_usuarios.addItem("Permisos",event -> getUI().get().navigate("permisoView"));

        SubMenu sm_empresas = m_empresas.getSubMenu();
        sm_empresas.addItem(new RouterLink("Administrar", EmpresaView.class));
        sm_empresas.addItem("Usuario Admin",event -> getUI().get().navigate("adminProd"));

        addItem(m_ventas);
        addItem(m_productos);
        addItem(m_usuarios);
        addItem(m_empresas);
        addItem(m_salir);
    }

    private void signOut(){
        Dialog dialog = new Dialog(new Label("¿Está Seguro de Salir?"));

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        NativeButton confirmButton = new NativeButton("Confirmar", event -> {
            AccessControlFactory.getInstance().createAccessControl().signOut();
            dialog.close();
        });
        NativeButton cancelButton = new NativeButton("Cancelar", event -> {
            dialog.close();
        });
        dialog.add(confirmButton, cancelButton);
        dialog.open();
    }

    public MenuItem getM_ventas() {
        return m_ventas;
    }

    public MenuItem getM_productos() {
        return m_productos;
    }

    public MenuItem getM_usuarios() {
        return m_usuarios;
    }

    public MenuItem getM_salir() {
        return m_salir;
    }

    public MenuItem getM_empresas() {
        return m_empresas;
    }
}
