package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Factura;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.PventaDTO;

import java.util.List;

public interface PventaService {

    public List<PventaDTO> find50MostImportant(Empresa empresa, int page, int size);

    public List<PventaDTO> findAll(Empresa empresa, int page, int size);

    public Factura registerSell(Usuario usuario, List<PventaDTO> listVenta);

}
