package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.HandledException;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.time.LocalDate;
import java.util.List;

public interface CompraService {
    int count(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd);
    List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, int page, int size);
    List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, OffsetBasedPageRequest offsetBasedPageRequest);

    Response saveCompra(Compra compra, Empresa empresa, Usuario usuario) throws HandledException;

    Response delete(String id);

    boolean deleteAllCompras(Empresa emp, List<Producto> productosList);
}
