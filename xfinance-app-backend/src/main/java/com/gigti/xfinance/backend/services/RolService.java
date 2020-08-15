package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;

public interface RolService {

    boolean deleteAllByEmpresa(Empresa empresa);
}
