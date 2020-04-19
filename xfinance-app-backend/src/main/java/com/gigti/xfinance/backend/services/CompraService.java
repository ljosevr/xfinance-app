package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;

import java.time.LocalDate;
import java.util.List;

public interface CompraService {
    int count(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd);
    List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, int page, int size);

    Response saveCompra(Compra compra, Empresa empresa, Usuario usuario);
}
