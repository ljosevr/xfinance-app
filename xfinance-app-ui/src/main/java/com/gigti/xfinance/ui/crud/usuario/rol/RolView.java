package com.gigti.xfinance.ui.crud.usuario.rol;

import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.services.ProductoService;
import com.gigti.xfinance.backend.services.CategoriaProductoService;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = Constantes.VIEW_R_ROL, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_MAIN)
public class RolView extends HorizontalLayout {

    private RolGrid grid;
    private RolForm form;
    private TextField filter;

    private RolCrudLogic viewLogic;
    private List<Rol> listaRoles;
    private VerticalLayout barAndGridLayout;

    @Autowired
    public RolView(UsuarioService usuarioService) {

    }
}
