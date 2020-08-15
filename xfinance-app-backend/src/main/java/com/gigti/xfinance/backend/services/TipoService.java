package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.TipoIde;
import com.gigti.xfinance.backend.data.TipoMedida;

import java.util.List;

public interface TipoService {

    List<TipoIde> getTiposIdentificacion();

    List<TipoMedida> findAllTiposMedidas(Empresa empresa);

    boolean deleteAllTipos(Empresa emp);
}
