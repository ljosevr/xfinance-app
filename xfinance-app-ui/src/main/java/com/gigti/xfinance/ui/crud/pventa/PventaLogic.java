package com.gigti.xfinance.ui.crud.pventa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.crud.Categorias.CategoriaView;

import java.io.Serializable;

public class PventaLogic implements Serializable {

    private CategoriaView view;
    private IcategoriaProductoService icategoriaProductoService;
    private static Empresa empresa;
    private String filterText = "";

}
