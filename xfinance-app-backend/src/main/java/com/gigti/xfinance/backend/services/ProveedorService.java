package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Proveedor;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.util.List;

public interface ProveedorService {

    Response save(Proveedor proveedor, Usuario usuario);
    Response delete(Proveedor proveedor, Usuario usuario);
    Response findById(String id);
    List<Proveedor> find(String filter, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);
    int countSearch(String filterText, Empresa empresa);

}
