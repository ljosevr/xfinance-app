package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.data.dto.PventaDTO;

import java.util.List;

public interface VentaService {

    public List<PventaDTO> findAll(String filter, Empresa empresa, int page, int size);

    public int count(Empresa empresa);

    public Venta registrarVenta(Usuario usuario, List<PventaDTO> listVenta);

}
