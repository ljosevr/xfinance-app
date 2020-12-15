package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Gasto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.HandledException;
import com.gigti.xfinance.backend.others.Response;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GastoService {
    int count(Empresa empresa, LocalDate dateStart, LocalDate dateEnd);
    List<Gasto> findAll(Empresa empresa, LocalDate dateStart, LocalDate dateEnd, OffsetBasedPageRequest offsetBasedPageRequest);

    Response save(Gasto gasto, Usuario usuario);

    Response delete(String id);

    BigDecimal getGastoTotal(Empresa empresa, LocalDate dateStart, LocalDate dateEnd);
}
