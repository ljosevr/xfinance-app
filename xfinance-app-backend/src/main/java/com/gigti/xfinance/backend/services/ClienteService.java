package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;
import java.util.List;

public interface ClienteService {
    Response save(Cliente cliente, Usuario usuario);
    Response delete(Cliente cliente, Usuario usuario);
    Response findById(String id);
    List<Cliente> find(String filter, Empresa empresa, OffsetBasedPageRequest offsetBasedPageRequest);
    int countSearch(String filterText, Empresa empresa);
    boolean deleteAllByEmpresa(Empresa empresa);
}
