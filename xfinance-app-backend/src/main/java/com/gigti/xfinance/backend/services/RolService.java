package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.Arrays;
import java.util.List;

public interface RolService {

    boolean deleteAllByEmpresa(Empresa empresa);

    List<Rol> findAll(String value, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);

    int count(String filterText, Empresa empresa);

    Response delete(String id);

    Response save(Rol rol);

    List<Vista> findAllVistas();
}
